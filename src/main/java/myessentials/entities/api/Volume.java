package myessentials.entities.api;

import com.google.gson.*;
import myessentials.MyEssentialsCore;
import myessentials.chat.api.IChatFormat;
import myessentials.json.api.SerializerTemplate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

import java.lang.reflect.Type;

/**
 * A rectangular shaped volume.
 */
public class Volume implements IChatFormat {

    public final int dim;
    public final int minX, minY, minZ;
    public final int maxX, maxY, maxZ;

    public Volume(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int dim) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.dim = dim;
    }

    public Volume(Position pos, int range) {
        this(pos.xi() - range, pos.yi() - range, pos.zi() - range,
                pos.xi() + range, pos.yi() + range, pos.zi() + range, pos.dim());
    }

    public Volume(Position from, Position to) {
        this(   Math.min(from.xi(), to.xi()),
                Math.min(from.yi(), to.yi()),
                Math.min(from.zi(), to.zi()),
                Math.max(from.xi(), to.xi()),
                Math.max(from.yi(), to.yi()),
                Math.max(from.zi(), to.zi()), from.dim());
        if (from.dim() != to.dim()) {
            throw new RuntimeException("Different dim in Volume initialization!");
        }
    }

    public Volume(ChunkPos pos) {
        this(pos.x << 4, 0, pos.z << 4, (pos.x << 4) + 15, 255, (pos.z << 4) + 15, pos.dim);
    }

    public Position getStartPos() {
        return new Position(minX, minY, minZ, dim);
    }

    public Position getEndPos() {
        return new Position(maxX, maxY, maxZ, dim);
    }

    public ChunkPos getStartChunk() {
        return new ChunkPos(minX >> 4, minZ >> 4, dim);
    }

    public ChunkPos getEndChunk() {
        return new ChunkPos(maxX >> 4, maxZ >> 4, dim);
    }

    public int getVolumeAmount() {
        return (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
    }

    public int getLengthX() {
        return maxX - minX + 1;
    }

    public int getLengthY() {
        return maxY - minY + 1;
    }

    public int getLengthZ() {
        return maxZ - minZ + 1;
    }

    public boolean contains(Position pos) {
        return pos.dim() == dim
                && minX <= pos.xi() && pos.xi() <= maxX
                && minY <= pos.yi() && pos.yi() <= maxY
                && minZ <= pos.zi() && pos.zi() <= maxZ;
    }

    public Volume offset(int offsetX, int offsetY, int offsetZ) {
        return new Volume(minX + offsetX, minY + offsetY, minZ + offsetZ, maxX + offsetX, maxY + offsetY, maxZ + offsetZ, dim);
    }

    public Volume translate(EnumFacing direction) {
        Volume volume;
        switch (direction) {
            case DOWN:
                volume = new Volume(minX, -maxZ, minY, maxX, minZ, maxY, dim);
                break;
            case UP:
                volume = new Volume(minX, minZ, minY, maxX, maxZ, maxY, dim);
                break;
            case NORTH:
                volume = new Volume(minX, minY, - maxZ, maxX, maxY, minZ, dim);
                break;
            case WEST:
                volume = new Volume(- maxZ, minY, minX, minZ, maxY, maxX, dim);
                break;
            case EAST:
                volume = new Volume(minZ, minY, minX, maxZ, maxY, maxX, dim);
                break;
            case SOUTH:
                // The translation on South is already the correct one.
                volume = this;
                break;
            default:
                volume = this;
                break;
        }
        return volume;
    }

    public Volume intersect(Volume other) {
        if (dim != other.dim) {
            throw new RuntimeException("Intersecting Volumes in two different dimensions");
        }
        if (other.maxX >= minX && other.minX <= maxX &&
                other.maxY >= minY && other.minY <= maxY &&
                other.maxZ >= minZ && other.minZ <= maxZ) {

            int x1, y1, z1, x2, y2, z2;

            x1 = (minX < other.minX) ? other.minX : minX;
            y1 = (minY < other.minY) ? other.minY : minY;
            z1 = (minZ < other.minZ) ? other.minZ : minZ;
            x2 = (maxX > other.maxX) ? other.maxX : maxX;
            y2 = (maxY > other.maxY) ? other.maxY : maxY;
            z2= (maxZ > other.maxZ) ? other.maxZ : maxZ;

            return new Volume(x1, y1, z1, x2, y2, z2, dim);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Volume) {
            Volume other = (Volume)obj;
            return other.minX == minX && other.minY == minY && other.minZ == minZ && other.maxX == maxX && other.maxY == maxY && other.maxZ == maxZ;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return toChatMessage().getUnformattedText();
    }

    @Override
    public ITextComponent toChatMessage() {
        return MyEssentialsCore.instance.LOCAL.getLocalization("myessentials.format.volume", minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static class Serializer extends SerializerTemplate<Volume> {

        @Override
        public void register(GsonBuilder builder) {
            builder.registerTypeAdapter(Volume.class, this);
        }

        @Override
        public JsonElement serialize(Volume volume, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray json = new JsonArray();
            json.add(new JsonPrimitive(volume.minX));
            json.add(new JsonPrimitive(volume.minY));
            json.add(new JsonPrimitive(volume.minZ));
            json.add(new JsonPrimitive(volume.maxX));
            json.add(new JsonPrimitive(volume.maxY));
            json.add(new JsonPrimitive(volume.maxZ));
            json.add(new JsonPrimitive(volume.dim));
            return json;
        }

        @Override
        public Volume deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            return new Volume(jsonArray.get(0).getAsInt(), jsonArray.get(1).getAsInt(), jsonArray.get(2).getAsInt(),
                    jsonArray.get(3).getAsInt(), jsonArray.get(4).getAsInt(), jsonArray.get(5).getAsInt(), jsonArray.get(6).getAsInt());
        }
    }
}
