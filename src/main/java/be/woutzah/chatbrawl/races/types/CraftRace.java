package be.woutzah.chatbrawl.races.types;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.races.Race;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CraftRace extends Race {

    private final ChatBrawl plugin;
    private HashMap<Material, Integer> craftsMap;
    private HashMap<UUID, Integer> playerScores;
    private ItemStack currentItemStack;
    private FileConfiguration craftRaceConfig;
    private String craftraceName;
    private List<String> craftraceStart;
    private String craftraceEnd;
    private List<String> craftraceWinner;
    private String craftraceWinnerPersonal;
    private String craftraceActionBar;

    public CraftRace(ChatBrawl plugin, FileConfiguration config) {
        super(
                plugin,
                config.getLong("craftrace.duration"),
                config.getInt("craftrace.chance"),
                config.getBoolean("craftrace.enable-firework"),
                config.getBoolean("craftrace.enabled"),
                config.getConfigurationSection("craftrace.rewards.commands")
        );
        this.craftRaceConfig = config;
        this.plugin = plugin;
        this.craftsMap = new HashMap<>();
        this.playerScores = new HashMap<>();
        getCraftsFromConfig();
        initializeLanguageEntries();
    }

    private void initializeLanguageEntries() {
        this.craftraceName = craftRaceConfig.getString("language.craftrace-name");
        this.craftraceStart = craftRaceConfig.getStringList("language.craftrace-start");
        this.craftraceEnd = craftRaceConfig.getString("language.craftrace-ended");
        this.craftraceWinner = craftRaceConfig.getStringList("language.craftrace-winner");
        this.craftraceWinnerPersonal = craftRaceConfig.getString("language.craftrace-winner-personal");
        this.craftraceActionBar = craftRaceConfig.getString("language.craftrace-actionbar-start");
    }

    private void getCraftsFromConfig() {
        try {
            final ConfigurationSection configSection = craftRaceConfig.getConfigurationSection("craftrace.items");

            for (String materialString : Objects.requireNonNull(configSection).getKeys(false)) {
                materialString = materialString.toUpperCase();
                final Material material = Material.getMaterial(materialString);

                if (material == null) {
                    throw new RaceException("Invalid material type in craft race: " + materialString);
                }

                craftsMap.put(material, Math.max(1, configSection.getInt(materialString)));
            }
        } catch (RaceException e) {
            RaceException.handleConfigException(plugin, e);
        }
    }

    public void fillOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach(p -> playerScores.put(p.getUniqueId(), 0));
    }

    public void removeOnlinePlayers() {
        playerScores.clear();
    }

    public void generateNewMaterialPair() {
        Object[] crafts = craftsMap.keySet().toArray();
        Object key = crafts[new Random().nextInt(crafts.length)];
        currentItemStack = new ItemStack((Material) key, craftsMap.get(key));
    }

    public HashMap<UUID, Integer> getPlayerScores() {
        return playerScores;
    }

    public ItemStack getCurrentItemStack() {
        return currentItemStack;
    }

    public String getCraftraceName() {
        return craftraceName;
    }

    public List<String> getCraftraceStart() {
        return craftraceStart;
    }

    public String getCraftraceEnd() {
        return craftraceEnd;
    }

    public List<String> getCraftraceWinner() {
        return craftraceWinner;
    }

    public String getCraftraceWinnerPersonal() {
        return craftraceWinnerPersonal;
    }

    public String getCraftraceActionBar() {
        return craftraceActionBar;
    }
}
