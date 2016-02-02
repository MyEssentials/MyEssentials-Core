package myessentials.test.json;


import junit.framework.Assert;
import myessentials.entities.api.Volume;
import myessentials.test.MECTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonConfigTest extends MECTest {

    private List<Volume> volumes = new ArrayList<Volume>();
    private FakeJsonConfig config;

    @Before
    public void shouldInitBlockPosArray() {
        volumes.add(new Volume(1, 1, 1, 5, 5, 5));
        volumes.add(new Volume(-10, -1, -1, 10, 1, 1));
        volumes.add(new Volume(-500, -500, -500, 1000, 1000, 0));
    }

    @Before
    public void shouldInstantiateConfig() {
        // REF: This file path will work in this directory but, if moved, it might not work
        config = new FakeJsonConfig("config/tests/TestConfig.json");
        // REF: You should be able to create a file with no list (similar to how the init method works)
        config.create(new ArrayList<Volume>());
    }

    @Test
    public void shouldReadFromFile() {
        List<Volume> readValues = config.read();
        int i = 0;
        for (Volume volume : readValues) {
            Assert.assertEquals("One of the volumes read was not expected", volumes.get(i++), volume);
        }
    }

    @Test
    public void shouldWriteToFile() {
        // REF: There's code duplication in the create and write method.
        // REF: There's ambiguity between updating and writing in the write method
        config.create(volumes);
        List<Volume> readValues = config.read();
        int i = 0;
        for (Volume volume : readValues) {
            Assert.assertEquals("One of the block positions written was not expected", volumes.get(i++), volume);
        }
    }
}
