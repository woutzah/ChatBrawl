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

public class CraftRace extends Race {

    private HashMap<Material, Integer> craftsMap;
    private HashMap<UUID, Integer> playerScores;
    private ItemStack currentItemStack;

    public CraftRace(ChatBrawl plugin) {
        super(
                plugin,
                plugin.getConfig().getLong("craftrace.duration"),
                plugin.getConfig().getInt("craftrace.chance"),
                plugin.getConfig().getBoolean("craftrace.enable-firework"),
                plugin.getConfig().getBoolean("craftrace.enabled"),
                plugin.getConfig().getConfigurationSection("craftrace.rewards.commands"));
        this.craftsMap = new HashMap<>();
        this.playerScores = new HashMap<>();
        getCraftsFromConfig();
    }

    private void getCraftsFromConfig() {
        try {
            ConfigurationSection configSection =
                    getPlugin().getConfig().getConfigurationSection("craftrace.items");
            for (String materialString :
                    Objects.requireNonNull(configSection).getKeys(false)) {
                Material material = Material.getMaterial(materialString);
                int amount = configSection.getInt(materialString);
                if (material == null) {
                    throw new RaceException("Invalid material type in craft race: " + materialString);
                }
                if (amount == 0) {
                    amount = 1;
                }
                craftsMap.put(material, amount);
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
}
