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

public class FishRace extends Race {

  private ChatBrawl plugin;
  private HashMap<Material, Integer> fishMap;
  private HashMap<UUID, Integer> playerScores;
  private ItemStack currentItemStack;
  private List<Material> fishableList;
  private FileConfiguration fishRaceConfig;
  private String fishraceName;
  private List<String> fishraceStart;
  private String fishraceEnd;
  private List<String> fishraceWinner;
  private String fishraceWinnerPersonal;

  public FishRace(ChatBrawl plugin, FileConfiguration config) {
    super(
        plugin,
        config.getLong("fishrace.duration"),
        config.getInt("fishrace.chance"),
        config.getBoolean("fishrace.enable-firework"),
        config.getBoolean("fishrace.enabled"),
        config.getConfigurationSection("fishrace.rewards.commands"));
    this.plugin = plugin;
    this.fishRaceConfig = config;
    this.playerScores = new HashMap<>();
    this.fishMap = new HashMap<>();
    this.fishableList = new ArrayList<>();
    fillFishableList();
    getFishFromConfig();
    initializeLanguageEntries();
  }

  private void initializeLanguageEntries() {
    this.fishraceName = fishRaceConfig.getString("language.fishrace-name");
    this.fishraceStart = fishRaceConfig.getStringList("language.fishrace-start");
    this.fishraceEnd = fishRaceConfig.getString("language.fishrace-ended");
    this.fishraceWinner = fishRaceConfig.getStringList("language.fishrace-winner");
    this.fishraceWinnerPersonal = fishRaceConfig.getString("language.fishrace-winner-personal");
  }

  private void getFishFromConfig() {
    try {
      ConfigurationSection configSection =
          fishRaceConfig.getConfigurationSection("fishrace.fish");
      for (String materialString :
          Objects.requireNonNull(configSection).getKeys(false)) {
        Material material = Material.getMaterial(materialString);
        if (material == null) {
          throw new RaceException("Invalid material type in fish race: " + materialString);
        }
        if (!fishableList.contains(material)) {
          throw new RaceException("Material is not a fish in fish race: " + materialString);
        }
        int amount = configSection.getInt(materialString);
        if (amount == 0) {
          amount = 1;
        }
        fishMap.put(material, amount);
      }
    } catch (RaceException e) {
      RaceException.handleConfigException(plugin, e);
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

  public String getFishraceName() {
    return fishraceName;
  }

  public List<String> getFishraceStart() {
    return fishraceStart;
  }

  public String getFishraceEnd() {
    return fishraceEnd;
  }

  public List<String> getFishraceWinner() {
    return fishraceWinner;
  }

  public String getFishraceWinnerPersonal() {
    return fishraceWinnerPersonal;
  }
}
