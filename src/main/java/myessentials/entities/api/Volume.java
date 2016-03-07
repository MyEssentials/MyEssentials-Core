package myessentials.entities.api;

import com.google.gson.*;
import myessentials.MyEssentialsCore;
import myessentials.chat.api.ChatFormat;
import myessentials.json.api.SerializerTemplate;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Type;

/**
 * A rectangular shaped volume.
 */
public class Volume extends ChatFormat {

    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;

    public Volume(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public Volume translate(ForgeDirection direction) {
        Volume volume = this;
        switch (direction) {
            case DOWN:
                volume = new Volume(volume.getMinX(), -volume.getMaxZ(), volume.getMinY(), volume.getMaxX(), volume.getMinZ(), volume.getMaxY());
                break;
            case UP:
                volume = new Volume(volume.getMinX(), volume.getMinZ(), volume.getMinY(), volume.getMaxX(), volume.getMaxZ(), volume.getMaxY());
                break;
            case NORTH:
                volume = new Volume(volume.getMinX(), volume.getMinY(), - volume.getMaxZ(), volume.getMaxX(), volume.getMaxY(), volume.getMinZ());
                break;
            case WEST:
                volume = new Volume(- volume.getMaxZ(), volume.getMinY(), volume.getMinX(), volume.getMinZ(), volume.getMaxY(), volume.getMaxX());
                break;
            case EAST:
                volume = new Volume(volume.getMinZ(), volume.getMinY(), volume.getMinX(), volume.getMaxZ(), volume.getMaxY(), volume.getMaxX());
                break;
            case SOUTH:
                // The translation on South is already the correct one.
                break;
            case UNKNOWN:
                break;
        }
        return volume;
    }

    public Volume intersect(Volume other) {
        if (other.getMaxX() >= minX && other.getMinX() <= maxX &&
                other.getMaxY() >= minY && other.getMinY() <= maxY &&
                other.getMaxZ() >= minZ && other.getMinZ() <= maxZ) {

            int x1, y1, z1, x2, y2, z2;

            x1 = (minX < other.getMinX()) ? other.getMinX() : minX;
            y1 = (minY < other.getMinY()) ? other.getMinY() : minY;
            z1 = (minZ < other.getMinZ()) ? other.getMinZ() : minZ;
            x2 = (maxX > other.getMaxX()) ? other.getMaxX() : maxX;
            y2 = (maxY > other.getMaxY()) ? other.getMaxY() : maxY;
            z2= (maxZ > other.getMaxZ()) ? other.getMaxZ() : maxZ;

            return new Volume(x1, y1, z1, x2, y2, z2);
        }
        return null;
    }

    public int getVolumeAmount() {
        return (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
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
    public IChatComponent toChatMessage(boolean shortened) {
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
            json.add(new JsonPrimitive(volume.getMinX()));
            json.add(new JsonPrimitive(volume.getMinY()));
            json.add(new JsonPrimitive(volume.getMinZ()));
            json.add(new JsonPrimitive(volume.getMaxX()));
            json.add(new JsonPrimitive(volume.getMaxY()));
            json.add(new JsonPrimitive(volume.getMaxZ()));
            return json;
        }

        @Override
        public Volume deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            return new Volume(jsonArray.get(0).getAsInt(), jsonArray.get(1).getAsInt(), jsonArray.get(2).getAsInt(),
                    jsonArray.get(3).getAsInt(), jsonArray.get(4).getAsInt(), jsonArray.get(5).getAsInt());
        }
    }
}
