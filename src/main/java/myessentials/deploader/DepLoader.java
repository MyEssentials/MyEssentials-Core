package myessentials.deploader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DepLoader {
    private static final FilenameFilter jarFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name)
        {
            return name.endsWith(".jar");
        }
    };

    private File modsDir, vModsDir, rootDir;
    private Map<String, Dependency> depMap;
    private Downloader downloader;
    private LaunchClassLoader cl;

    public DepLoader(File rootDir, LaunchClassLoader cl) {
        String mcVer = (String) FMLInjectionData.data()[4];
        File mcDir = (File) FMLInjectionData.data()[6];
        this.rootDir = rootDir;
        this.cl = cl;

        modsDir = new File(mcDir, "mods");
        vModsDir = new File(mcDir, "mods/" + mcVer);
        if (!vModsDir.exists())
            vModsDir.mkdirs();

        depMap = new HashMap<String, Dependency>();
        downloader = new Downloader(rootDir);
    }

    public void load() {
        scanMods();
        if (depMap.isEmpty())
            return;

        for (Dependency dep : depMap.values()) {
            downloader.load(dep);
        }
        injectDeps();
    }

    private void injectDeps() {
        if (!rootDir.exists())
            rootDir.mkdirs();

        for (File f : rootDir.listFiles(jarFilter)) {
            if (f == null) continue;

            try {
                cl.addURL(f.toURI().toURL());
                System.out.println("[MyEssentials-Core] Loaded library file " + f.getAbsolutePath());
            } catch(MalformedURLException e) {
                throw new RuntimeException("[MyEssentials Core] Could not add library file " + f.getAbsolutePath() + ", there may be a class loading problem");
            }
        }
    }

    private List<File> modFiles() {
        List<File> list = new LinkedList<File>();
        list.addAll(Arrays.asList(modsDir.listFiles()));
        list.addAll(Arrays.asList(vModsDir.listFiles()));
        return list;
    }

    private void scanMods() {
        for (File file : modFiles()) {
            if (!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip"))
                continue;

            scanDepInfo(file);
        }
    }

    private void scanDepInfo(File file) {
        try {
            ZipFile zip = new ZipFile(file);
            ZipEntry e = zip.getEntry("deps.info");
            if (e != null)
                parseDepInfo(zip.getInputStream(e));
            zip.close();
        } catch(Exception e) {
            System.err.println("[MyEssentials-Core] Failed to load deps.info from " + file.getName() + " as JSON");
            e.printStackTrace();
        }
    }

    private void parseDepInfo(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        JsonElement root = new JsonParser().parse(reader);
        if (root.isJsonArray()) {
            parseJsonArray(root.getAsJsonArray());
        } else {
            parseJson(root.getAsJsonObject());
        }
        reader.close();
    }

    private void parseJsonArray(JsonArray root) {
        for (JsonElement node : root) {
            if (node.isJsonObject()) {
                parseJson(node.getAsJsonObject());
            } else {
                System.out.println("[MyEssentials-Core] Invalid node in deps.info!");
            }
        }
    }

    private void parseJson(JsonObject node) {
        boolean obfuscated = false;
        try {
            obfuscated = ((LaunchClassLoader) Loader.class.getClassLoader()).getClassBytes("net.minecraft.world.World") == null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        String testClass = node.get("class").getAsString();
        if (DepLoader.class.getResource("/" + testClass.replace(".", "/") + ".class") != null)
            return;

        String repo = node.get("repo").getAsString();
        String filename = node.get("file").getAsString();
        if (!obfuscated && node.has("dev"))
            filename = node.get("dev").getAsString();

        boolean coreLib = node.has("coreLib") && node.get("coreLib").getAsBoolean();

        Pattern pattern = null;
        try {
            if (node.has("pattern"))
                pattern = Pattern.compile(node.get("pattern").getAsString());
        } catch (PatternSyntaxException e) {
            System.err.println("[MyEssentials-Core] Invalid filename pattern: "+node.get("pattern"));
            e.printStackTrace();
        }
        if (pattern == null)
            pattern = Pattern.compile("(\\w+).*?([\\d\\.]+)[-\\w]*\\.[^\\d]+");

        VersionedFile versionedFile = new VersionedFile(filename, pattern);
        if (!versionedFile.matches())
            throw new RuntimeException("[MyEssentials-Core] Invalid filename format for dependency: " + filename);

        addDep(new Dependency(repo, versionedFile, coreLib));
    }

    private void addDep(Dependency dep) {
        if (mergeNew(depMap.get(dep.getName()), dep)) {
            depMap.put(dep.getName(), dep);
        }
    }

    private boolean mergeNew(Dependency oldDep, Dependency newDep) {
        if (oldDep == null)
            return true;

        Dependency newest = newDep.getVersion().compareTo(oldDep.getVersion()) > 0 ? newDep : oldDep;
        newest.setCoreLib(newDep.isCoreLib() || oldDep.isCoreLib());

        return newest == newDep;
    }
}
