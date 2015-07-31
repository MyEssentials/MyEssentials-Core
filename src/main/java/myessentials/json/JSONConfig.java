package myessentials.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import myessentials.MyEssentialsCore;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for all JSON configs.
 */
public abstract class JSONConfig<T> {

    /**
     * The path to the file used.
     */
    protected final String path, name;
    protected final Gson gson;
    protected Type gsonType;


    public JSONConfig(String path, String name) {
        this.path = path;
        this.name = name;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Initializes everything.
     */
    public void init() {
        File file = new File(path);

        File parent = file.getParentFile();
        if(!parent.exists() && !parent.mkdirs()){
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
        List<T> items = new ArrayList<T>();
        if(!file.exists() || file.isDirectory())
            create(items);
        else {
            read();
        }
    }

    /**
     * Creates the file if it doesn't exist with the initial given items
     */
    protected void create(List<T> initialItems) {
        try {
            Writer writer = new FileWriter(path);
            gson.toJson(initialItems, gsonType, writer);
            writer.close();
            MyEssentialsCore.instance.LOG.info("Created new " + name + " file successfully!");
        } catch (IOException ex) {
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            MyEssentialsCore.instance.LOG.error("Failed to create " + name + " file!");
        }
    }

    /**
     * Writes the given list to the file, completely overwriting it
     */
    public void write(List<T> items) {
        try {
            Writer writer = new FileWriter(path);
            gson.toJson(items, gsonType, writer);
            writer.close();
            MyEssentialsCore.instance.LOG.info("Updated the " + name + " file successfully!");
        } catch (IOException ex) {
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            MyEssentialsCore.instance.LOG.error("Failed to update " + name + " file!");
        }
    }

    /**
     * Reads and returns the validated items.
     */
    protected List<T> read() {
        List<T> items = new ArrayList<T>();

        try {
            Reader reader = new FileReader(path);
            items = gson.fromJson(reader, gsonType);
            reader.close();
            MyEssentialsCore.instance.LOG.info("Loaded " + name + " successfully!");
        } catch (IOException ex) {
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            MyEssentialsCore.instance.LOG.error("Failed to read from " + name + " file!");
        }

        if(!validate(items))
            write(items);

        return items;
    }

    /**
     * Checks for validity and modifies the given list so that is valid.
     */
    protected boolean validate(List<T> items) {
        return true;
    }
}
