package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class FoodRace extends Race {
    private HashMap<Material, Integer> foodMap;
    private HashMap<UUID, Integer> playerScores;
    private ItemStack currentItemStack;

    public FoodRace(ChatBrawl plugin) {
        super(
                plugin,
                plugin.getConfig().getLong("foodrace.duration"),
                plugin.getConfig().getInt("foodrace.chance"),
                plugin.getConfig().getBoolean("foodrace.enable-firework"),
                plugin.getConfig().getBoolean("foodrace.enabled"),
                plugin.getConfig().getConfigurationSection("foodrace.rewards.commands"));
        this.foodMap = new HashMap<>();
        this.playerScores = new HashMap<>();
        getFoodFromConfig();
    }

    private void getFoodFromConfig() {
        try {
            ConfigurationSection configSection =
                    getPlugin().getConfig().getConfigurationSection("foodrace.food");
            for (String materialString :
                    Objects.requireNonNull(configSection).getKeys(false)) {
                Material material = Material.getMaterial(materialString);
                int amount = configSection.getInt(materialString);
                if (material == null) {
                    throw new RaceException("Invalid material type in food race: " + materialString);
                }
                if (!material.isEdible()) {
                    throw new RaceException("This material is not edible in foodrace: " + materialString);
                }
                if (amount == 0) {
                    amount = 1;
                }
                foodMap.put(material, amount);
            }
        } catch (RaceException e) {
            RaceException.handleConfigException(getPlugin(), e);
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
}
