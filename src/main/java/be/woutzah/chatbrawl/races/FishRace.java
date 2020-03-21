package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FishRace extends Race {

  private HashMap<Material, Integer> fishMap;
  private HashMap<UUID, Integer> playerScores;
  private ItemStack currentItemStack;
  private List<Material> fishableList;

  public FishRace(ChatBrawl plugin) {
    super(
        plugin,
        plugin.getConfig().getLong("fishrace.duration"),
        plugin.getConfig().getInt("fishrace.chance"),
        plugin.getConfig().getBoolean("fishrace.enable-firework"),
        plugin.getConfig().getBoolean("fishrace.enabled"),
        plugin.getConfig().getConfigurationSection("fishrace.rewards.commands"));
    this.playerScores = new HashMap<>();
    this.fishMap = new HashMap<>();
    this.fishableList = new ArrayList<>();
    fillFishableList();
    getFishFromConfig();
  }

  private void getFishFromConfig() {
    try {
      ConfigurationSection configSection =
          getPlugin().getConfig().getConfigurationSection("fishrace.fish");
      for (String materialString :
          Objects.requireNonNull(configSection).getKeys(false)) {
        Material material = Material.getMaterial(materialString);
        int amount = configSection.getInt(materialString);
        if (material == null) {
          throw new RaceException("Invalid material type in fish race: " + materialString);
        } else if (!fishableList.contains(material)) {
          throw new RaceException("Material is not a fish in fish race: " + materialString);
        } else if (amount == 0) {
          amount = 1;
        }
        fishMap.put(material, amount);
      }
    } catch (RaceException e) {
      RaceException.handleConfigException(getPlugin(), e);
    }
  }

  private void fillFishableList() {
    fishableList.add(Material.COD);
    fishableList.add(Material.PUFFERFISH);
    fishableList.add(Material.SALMON);
    fishableList.add(Material.TROPICAL_FISH);
  }

  public void fillOnlinePlayers() {
    Bukkit.getOnlinePlayers().forEach(p -> playerScores.put(p.getUniqueId(), 0));
  }

  public void removeOnlinePlayers() {
    playerScores.clear();
  }

  public void generateNewMaterialPair() {
    Object[] blocks = fishMap.keySet().toArray();
    Object key = blocks[new Random().nextInt(blocks.length)];
    currentItemStack = new ItemStack((Material) key, fishMap.get(key));
  }

  public HashMap<UUID, Integer> getPlayerScores() {
    return playerScores;
  }

  public ItemStack getCurrentItemStack() {
    return currentItemStack;
  }
}
