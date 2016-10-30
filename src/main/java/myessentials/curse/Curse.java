package myessentials.curse;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public final class Curse {
    private Curse() {
    }

    /**
     * Gets CurseModInfo using the mcf widget api. http://widget.mcf.li/
     * @param projectid The CurseForge project ID
     * @return The CurseModInfo object
     * @throws IOException
     */
    public static CurseModInfo getModInfo(String projectid) throws IOException {
        URL url = new URL("http://widget.mcf.li/mc-mods/minecraft/" + projectid + ".json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        return (new Gson()).fromJson(reader, CurseModInfo.class);
    }

    /**
     * Returns a direct download link of the file for the modid
     * @param projectid The CurseForge
     * @param downloadid The download id
     * @return The download URL
     * @throws MalformedURLException
     */
    public static URL getDownloadURL(String projectid, String downloadid) throws MalformedURLException {
        return new URL("http://minecraft.curseforge.com/mc-mods/" + projectid + "/files/" + downloadid + "/download");
    }
}
