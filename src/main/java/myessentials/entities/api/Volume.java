package myessentials.entities.api;

import com.google.gson.*;
import myessentials.json.api.SerializerTemplate;

import java.lang.reflect.Type;

/**
 * A rectangular shaped volume.
 */
public class Volume {

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
