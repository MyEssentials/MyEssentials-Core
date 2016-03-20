package myessentials.json.api;

import com.google.gson.Gson;
import myessentials.MyEssentialsCore;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * An abstract class for all JSON configs.
 * Instantiating the gson and gsonType is needed.
 * Methods are usually overriden but called inside their overriden method.
 */
public abstract class JsonConfig<T, L extends List<T>> {

    /**
     * The path to the file used.
     */
    protected final String path, name;
    protected Gson gson;
    protected Type gsonType;

    public JsonConfig(String path, String name) {
        this.path = path;
        this.name = name;
    }

    protected abstract L newList();

    public void init() {
        init(newList());
    }

    /**
     * Initializes everything.
     */
    public void init(L items) {
        File file = new File(path);

        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
        if (!file.exists() || file.isDirectory()) {
            create(items);
        } else {
            read();
        }
    }

    /**
     * Creates the file if it doesn't exist with the initial given items
     */
    public void create(L initialItems) {
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
    public void write(L items) {
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
    public L read() {
        L items = null;

        try {
            Reader reader = new FileReader(path);
            items = gson.fromJson(reader, gsonType);
            reader.close();
            MyEssentialsCore.instance.LOG.info("Loaded " + name + " successfully!");
        } catch (IOException ex) {
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            MyEssentialsCore.instance.LOG.error("Failed to read from " + name + " file!");
        }

        if (!validate(items)) {
            write(items);
        }

        return items;
    }

    /**
     * Checks for validity and modifies the given list so that is valid.
     */
    public boolean validate(L items) {
        return true;
    }

    public String getName() {
        return this.name;
    }
}
