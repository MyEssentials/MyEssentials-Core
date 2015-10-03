package myessentials.simple_config;

public class ConfigExample extends ConfigTemplate {

    public static final ConfigExample instance = new ConfigExample();

    public String yolo = "";

    @Override
    public void reload() {
        yolo = config.getString("yolo", "Category", yolo, "This is a bland comment");
    }
}
