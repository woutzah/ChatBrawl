package be.woutzah.chatbrawl.files;

import be.woutzah.chatbrawl.ChatBrawl;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class FileManager
{
    public FileConfiguration languageConfig;
    public File languageFile;
    private ChatBrawl plugin;
    private String name;

    public FileManager(ChatBrawl plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public void init() {
        setup();
        saveLanguageConfig();
        reloadLanguageConfig();
    }

    public void setup() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        this.languageFile = new File(this.plugin.getDataFolder(), this.name);

        if (!this.languageFile.exists()) {
            this.plugin.saveResource(this.name, false);
            Bukkit.getServer()
                    .getConsoleSender()
                    .sendMessage("[ChatBrawl] " + ChatColor.GREEN + "The " + this.name + " file has been created.");
        }






        this.languageConfig = YamlConfiguration.loadConfiguration(this.languageFile);
    }

    public void saveLanguageConfig() {
        try {
            this.languageConfig.save(this.languageFile);
            Bukkit.getServer()
                    .getConsoleSender()
                    .sendMessage("[ChatBrawl] " + ChatColor.GREEN + "The " + this.name + " file has been saved");





        }
        catch (IOException e) {
            Bukkit.getServer()
                    .getConsoleSender()
                    .sendMessage("[ChatBrawl] " + ChatColor.RED + "The " + this.name + " file could not be saved");
        }
    }

    public void reloadLanguageConfig() {
        this.languageConfig = YamlConfiguration.loadConfiguration(this.languageFile);
        Bukkit.getServer()
                .getConsoleSender()
                .sendMessage("[ChatBrawl] " + ChatColor.GREEN + "The " + this.name + " file has been reloaded.");
    }







    public FileConfiguration getLanguageConfig() { return this.languageConfig; }
}
