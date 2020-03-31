package be.woutzah.chatbrawl.files;

import be.woutzah.chatbrawl.ChatBrawl;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private final ChatBrawl plugin;

    public FileManager(ChatBrawl plugin){
        this.plugin = plugin;
    }

    public FileConfiguration loadFile(String path, boolean fromJar){
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

    public void saveFile(String path, FileConfiguration input){
        File file = new File(plugin.getDataFolder(), path);
        try {
            input.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}