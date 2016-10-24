package myessentials.curse;

import java.util.Map;

public class CurseModInfo {
    private String title;
    private String game;
    private String category;
    private String url;
    private String thumbnail;
    private String[] authors;
    // TODO Downloads Counters
    private int favorites;
    private int likes;
    private String updated_at;
    private String created_at;
    private String project_url;
    private String release_type;
    private String license;
    private VersionInfo download;
    private Map<String, VersionInfo[]> versions;

    public VersionInfo[] getVersions(String mcVersion) {
        return versions.get(mcVersion);
    }

    public VersionInfo getNewestVersion(String mcVersion) {
        VersionInfo newest = null;
        for (VersionInfo versionInfo : getVersions(mcVersion)) {
            if (newest == null || versionInfo.getId() > newest.getId()) {
                newest = versionInfo;
            }
        }
        return newest;
    }

    public String getTitle() {
        return title;
    }

    public String getGame() {
        return game;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String[] getAuthors() {
        return authors;
    }

    public int getFavorites() {
        return favorites;
    }

    public int getLikes() {
        return likes;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getProject_url() {
        return project_url;
    }

    public String getRelease_type() {
        return release_type;
    }

    public String getLicense() {
        return license;
    }

    public VersionInfo getDownload() {
        return download;
    }
}
