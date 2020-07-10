package be.woutzah.chatbrawl.races.types;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.races.Race;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BlockRace extends Race {

    private ChatBrawl plugin;
    private HashMap<Material, Integer> blocksMap;
    private HashMap<UUID, Integer> playerScores;
    private ItemStack currentItemStack;
    private FileConfiguration blockRaceConfig;
    private String blockraceName;
    private List<String> blockraceStart;
    private String blockraceEnd;
    private List<String> blockraceWinner;
    private String blockraceWinnerPersonal;

    public BlockRace(ChatBrawl plugin, FileConfiguration config) {
        super(
                plugin,
                config.getLong("blockrace.duration"),
                config.getInt("blockrace.chance"),
                config.getBoolean("blockrace.enable-firework"),
                config.getBoolean("blockrace.enabled"),
                config.getConfigurationSection("blockrace.rewards.commands"));
        this.plugin = plugin;
        this.blockRaceConfig = config;
        this.blocksMap = new HashMap<>();
        this.playerScores = new HashMap<>();
        getBlocksFromConfig();
        initializeLanguageEntries();
    }

    private void initializeLanguageEntries() {
        this.blockraceName = blockRaceConfig.getString("language.blockrace-name");
        this.blockraceStart = blockRaceConfig.getStringList("language.blockrace-start");
        this.blockraceEnd = blockRaceConfig.getString("language.blockrace-ended");
        this.blockraceWinner = blockRaceConfig.getStringList("language.blockrace-winner");
        this.blockraceWinnerPersonal = blockRaceConfig.getString("language.blockrace-winner-personal");
    }

    private void getBlocksFromConfig() {
        try {
            ConfigurationSection configSection =
                    blockRaceConfig.getConfigurationSection("blockrace.blocks");
            for (String materialString :
                    Objects.requireNonNull(configSection).getKeys(false)) {
                Material material = Material.getMaterial(materialString);
                int amount = configSection.getInt(materialString);
                if (material == null) {
                    throw new RaceException("Invalid material type in block race: " + materialString);
                }
                if (amount == 0) {
                    amount = 1;
                }
                blocksMap.put(material, amount);
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
        Object[] blocks = blocksMap.keySet().toArray();
        Object key = blocks[new Random().nextInt(blocks.length)];
        currentItemStack = new ItemStack((Material) key, blocksMap.get(key));
    }

    public HashMap<UUID, Integer> getPlayerScores() {
        return playerScores;
    }

    public ItemStack getCurrentItemStack() {
        return currentItemStack;
    }

    public String getBlockraceName() {
        return blockraceName;
    }

    public List<String> getBlockraceStart() {
        return blockraceStart;
    }

    public String getBlockraceEnd() {
        return blockraceEnd;
    }

    public List<String> getBlockraceWinner() {
        return blockraceWinner;
    }

    public String getBlockraceWinnerPersonal() {
        return blockraceWinnerPersonal;
    }
}
