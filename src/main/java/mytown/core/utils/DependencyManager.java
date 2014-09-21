package mytown.core.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Manages downloading and injecting dependencies into the classpath
 *
 * @author Joe Goett, AfterWind
 */
public class DependencyManager {
    /**
     * Describes what a dependency is
     */
    public static class Dep {
        private final String filename;
        private final String downloadUrl;

        public Dep(String filename, String downloadUrl) {
            this.filename = filename;
            this.downloadUrl = downloadUrl;
        }
    }

    /**
     * Downloads all the given dependencies into the classpath
     * @param deps List of all dependencies
     */
    public static void downloadDependencies(Dep[] deps) {
        for (Dep dep : deps) {
            downloadDependency(dep.filename, dep.downloadUrl);
        }
    }

    /**
     * Downloads and injects the given dependency into the classpath
     * @param filename Filename of file to inject
     * @param downloadURL URL to download from
     */
    public static void downloadDependency(final String filename, final String downloadURL) {
        File f = new File(filename);
        f.getParentFile().mkdirs();
        if (!f.exists() || !f.isFile()) {
            try {
                download(filename, downloadURL);
            } catch(IOException ex) {
                ex.printStackTrace(); // TODO Log this instead
            }
        }
        inject(f);
    }

    /**
     * Downloads the filename from the given url
     * @param filename Filename to be saved to
     * @param urlString URL to download from
     * @throws IOException
     */
    private static void download(final String filename, final String urlString) throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }

    /**
     * Injects the file into the classpath
     * @param file File to inject
     */
    private static void inject(File file) {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;

        try {
            URL u = file.toURL();
            Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, u);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
