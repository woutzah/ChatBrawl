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

public class BlockRace extends Race {

    private HashMap<Material, Integer> blocksMap;
    private HashMap<UUID, Integer> playerScores;
    private ItemStack currentItemStack;

    public BlockRace(ChatBrawl plugin) {
        super(
                plugin,
                plugin.getBlockraceConfig().getLong("blockrace.duration"),
                plugin.getBlockraceConfig().getInt("blockrace.chance"),
                plugin.getBlockraceConfig().getBoolean("blockrace.enable-firework"),
                plugin.getBlockraceConfig().getBoolean("blockrace.enabled"),
                plugin.getBlockraceConfig().getConfigurationSection("blockrace.rewards.commands"));
        this.blocksMap = new HashMap<>();
        this.playerScores = new HashMap<>();
        getBlocksFromConfig();
    }

    private void getBlocksFromConfig() {
        try {
            ConfigurationSection configSection =
                    getPlugin().getBlockraceConfig().getConfigurationSection("blockrace.blocks");
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
}
