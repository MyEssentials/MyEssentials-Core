package myessentials.test.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import junit.framework.Assert;
import myessentials.test.MECTest;
import org.junit.Test;

public class SerializerTest extends MECTest {

    @Test
    public void shouldSerializeObject() {

        GsonBuilder builder = new GsonBuilder();
        new FakeSerializableObject.Serializer().register(builder);
        Gson gson = builder.create();

        FakeSerializableObject obj = new FakeSerializableObject();
        obj.x = 10;
        obj.y = 55;

        JsonObject json = (JsonObject) gson.toJsonTree(obj);
        Assert.assertEquals("X component of the JsonObject was not serialized properly", 10, json.get("x").getAsInt());
        Assert.assertEquals("Y component of the JsonObject was not serialized properly", 55, json.get("y").getAsInt());
    }

    @Test
    public void shouldDeserializeObject() {

        GsonBuilder builder = new GsonBuilder();
        new FakeSerializableObject.Serializer().register(builder);
        Gson gson = builder.create();

        JsonObject json = new JsonObject();
        json.addProperty("x", 19);
        json.addProperty("y", 220);

        FakeSerializableObject obj = gson.fromJson(json, FakeSerializableObject.class);
        Assert.assertEquals("X component of the obejct was not deserialized properly", 19, obj.x);
        Assert.assertEquals("Y component of the obejct was not deserialized properly", 220, obj.y);
    }

    @Test
    public void shouldSerializeObjectWithChildSerializer() {

        GsonBuilder builder = new GsonBuilder();
        new FakeSerializableParentObject.Serializer().register(builder);
        Gson gson = builder.create();

        FakeSerializableParentObject objParent = new FakeSerializableParentObject();
        FakeSerializableObject obj = new FakeSerializableObject();
        obj.x = 50;
        obj.y = 1100;
        objParent.z = -1;
        objParent.obj = obj;

        JsonObject json = (JsonObject)gson.toJsonTree(objParent);
        Assert.assertEquals("Z component of parent object was not serialized properly", -1, json.get("z").getAsInt());
        Assert.assertTrue("Child component of parent object was not serailized properly", json.has("child"));
        Assert.assertEquals("X component of child object was not serializer properly", 50, json.get("child").getAsJsonObject().get("x").getAsInt());
        Assert.assertEquals("Y component of child object was not serializer properly", 1100, json.get("child").getAsJsonObject().get("y").getAsInt());
    }

    @Test
    public void shouldDeserializeObjectWithChildSerializer() {

        GsonBuilder builder = new GsonBuilder();
        new FakeSerializableParentObject.Serializer().register(builder);
        Gson gson = builder.create();

        JsonObject json = new JsonObject();
        JsonObject jsonChild = new JsonObject();
        jsonChild.addProperty("x", 30);
        jsonChild.addProperty("y", -2);
        json.add("child", jsonChild);
        json.addProperty("z", 100);

        FakeSerializableParentObject obj = gson.fromJson(json, FakeSerializableParentObject.class);
        Assert.assertEquals("Z component of parent object was not deserialized properly", 100, obj.z);
        Assert.assertNotNull("Child component of parent object was not deserialized properly", obj.obj);
        Assert.assertEquals("X component of child object was not deserialized properly", 30, obj.obj.x);
        Assert.assertEquals("Y component of child object was not deserialized properly", -2, obj.obj.y);
    }

}
