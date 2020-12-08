package be.woutzah.chatbrawl.files;

import be.woutzah.chatbrawl.ChatBrawl;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final ChatBrawl plugin;

    public FileManager(ChatBrawl plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FileConfiguration loadFile(String path, boolean fromJar) {
        FileConfiguration config = null;

        try {
            File file = new File(plugin.getDataFolder(), path);
            file.getParentFile().mkdirs();

            if (!file.exists()) {
                if (fromJar) {
                    plugin.saveResource(path, false);
                } else {
                    file.createNewFile();
                }
            }

            config = new YamlConfiguration();
            config.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    public void saveFile(String path, FileConfiguration input) {
        try {
            input.save(new File(plugin.getDataFolder(), path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}