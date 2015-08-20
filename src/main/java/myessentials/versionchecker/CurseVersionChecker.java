package myessentials.versionchecker;

import cpw.mods.fml.common.Loader;
import myessentials.curse.Curse;
import myessentials.curse.CurseModInfo;
import myessentials.curse.VersionInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurseVersionChecker implements Runnable {
    private String projectid;
    private double currentVersion;
    private Pattern versionRegex;
    private int delay;
    private boolean canRun = false;

    public CurseVersionChecker(String projectid, String currentVersion, String versionRegex, int delay) {
        this.projectid = projectid;
        this.currentVersion = stringToVersion(currentVersion);
        this.versionRegex = Pattern.compile(versionRegex);
        this.delay = delay;

        this.canRun = true;
    }

    @Override
    public void run() {
        try {
            while (canRun) {
                Thread.sleep(delay * 1000);
                CurseModInfo modInfo = Curse.getModInfo(projectid);
                VersionInfo newest = modInfo.getNewestVersion(Loader.MC_VERSION);
                Matcher matcher = versionRegex.matcher(newest.getName());
                String versionString = matcher.group(2);
                if (stringToVersion(versionString) > this.currentVersion) {
                }
            }
        } catch(Exception e) {
            // TODO Handle safely
        }
    }

    private double stringToVersion(String versionStr) {
        return Double.parseDouble(versionStr.replace(".", ""));
    }
}
