package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class HuntRace extends Race {

  private HashMap<EntityType, Integer> huntMap;
  private HashMap<UUID, Integer> playerScores;
  private EntityType currentEntityType;
  private int currentAmount;

  public HuntRace(ChatBrawl plugin) {
    super(
        plugin,
        plugin.getHuntraceConfig().getLong("huntrace.duration"),
        plugin.getHuntraceConfig().getInt("huntrace.chance"),
        plugin.getHuntraceConfig().getBoolean("huntrace.enable-firework"),
        plugin.getHuntraceConfig().getBoolean("huntrace.enabled"),
        plugin.getHuntraceConfig().getConfigurationSection("huntrace.rewards.commands"));
    this.playerScores = new HashMap<>();
    this.huntMap = new HashMap<>();
    getMobsFromConfig();
  }

  private void getMobsFromConfig() {
    try {
      ConfigurationSection configSection =
          getPlugin().getHuntraceConfig().getConfigurationSection("huntrace.mobs");
      for (String entityString :
          Objects.requireNonNull(configSection).getKeys(false)) {
        EntityType entityType = EntityType.valueOf(entityString.toUpperCase());
        int amount = configSection.getInt(entityString);
        if (!entityType.isAlive()) {
          throw new RaceException("Entity is not a mob in hunt race: " + entityString);
        } else if (amount == 0) {
          amount = 1;
        }
        huntMap.put(entityType, amount);
      }
    } catch (RaceException e) {
      RaceException.handleConfigException(getPlugin(), e);
    } catch (IllegalArgumentException e) {
      RaceException.handleConfigException(
          getPlugin(), new RaceException("Invalid mob name in hunt race!"));
    }
  }

  public void generateNewMobPair() {
    Object[] mobs = huntMap.keySet().toArray();
    Object key = mobs[new Random().nextInt(mobs.length)];
    currentEntityType = EntityType.valueOf(key.toString());
    currentAmount = huntMap.get(key);
  }

  public void fillOnlinePlayers() {
    Bukkit.getOnlinePlayers().forEach(p -> playerScores.put(p.getUniqueId(), 0));
  }

  public void removeOnlinePlayers() {
    playerScores.clear();
  }

  public EntityType getCurrentEntityType() {
    return currentEntityType;
  }

  public int getCurrentAmount() {
    return currentAmount;
  }

  public HashMap<UUID, Integer> getPlayerScores() {
    return playerScores;
  }
}
