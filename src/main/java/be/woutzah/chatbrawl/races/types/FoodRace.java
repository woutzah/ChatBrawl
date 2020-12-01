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

public class FoodRace extends Race {

    private ChatBrawl plugin;
    private HashMap<Material, Integer> foodMap;
    private HashMap<UUID, Integer> playerScores;
    private ItemStack currentItemStack;
    private FileConfiguration foodRaceConfig;
    private String foodraceName;
    private List<String> foodraceStart;
    private String foodraceEnd;
    private List<String> foodraceWinner;
    private String foodraceWinnerPersonal;
    private String foodraceActionBar;

    public FoodRace(ChatBrawl plugin, FileConfiguration config) {
        super(
                plugin,
                config.getLong("foodrace.duration"),
                config.getInt("foodrace.chance"),
                config.getBoolean("foodrace.enable-firework"),
                config.getBoolean("foodrace.enabled"),
                config.getConfigurationSection("foodrace.rewards.commands"));
        this.plugin = plugin;
        this.foodRaceConfig = config;
        this.foodMap = new HashMap<>();
        this.playerScores = new HashMap<>();
        getFoodFromConfig();
        initializeLanguageEntries();
    }

    private void initializeLanguageEntries() {
        this.foodraceName = foodRaceConfig.getString("language.foodrace-name");
        this.foodraceStart = foodRaceConfig.getStringList("language.foodrace-start");
        this.foodraceEnd = foodRaceConfig.getString("language.foodrace-ended");
        this.foodraceWinner = foodRaceConfig.getStringList("language.foodrace-winner");
        this.foodraceWinnerPersonal = foodRaceConfig.getString("language.foodrace-winner-personal");
        this.foodraceActionBar = foodRaceConfig.getString("language.foodrace-actionbar-start");
    }

    private void getFoodFromConfig() {
        try {
            ConfigurationSection configSection =
                    foodRaceConfig.getConfigurationSection("foodrace.food");
            for (String materialString :
                    Objects.requireNonNull(configSection).getKeys(false)) {
                materialString = materialString.toUpperCase();
                Material material = Material.getMaterial(materialString);
                if (material == null) {
                    throw new RaceException("Invalid material type in food race: " + materialString);
                }
                if (!material.isEdible()) {
                    throw new RaceException("This material is not edible in foodrace: " + materialString);
                }
                int amount = configSection.getInt(materialString);
                if (amount == 0) {
                    amount = 1;
                }
                foodMap.put(material, amount);
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

    public void generateNewFoodPair() {
        Object[] crafts = foodMap.keySet().toArray();
        Object key = crafts[new Random().nextInt(crafts.length)];
        currentItemStack = new ItemStack((Material) key, foodMap.get(key));
    }

    public HashMap<UUID, Integer> getPlayerScores() {
        return playerScores;
    }

    public ItemStack getCurrentItemStack() {
        return currentItemStack;
    }

    public String getFoodraceName() {
        return foodraceName;
    }

    public List<String> getFoodraceStart() {
        return foodraceStart;
    }

    public String getFoodraceEnd() {
        return foodraceEnd;
    }

    public List<String> getFoodraceWinner() {
        return foodraceWinner;
    }

    public String getFoodraceWinnerPersonal() {
        return foodraceWinnerPersonal;
    }

    public String getFoodraceActionBar() {
        return foodraceActionBar;
    }
}
