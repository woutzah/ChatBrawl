package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import be.woutzah.chatbrawl.utils.FireworkCreator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public abstract class Race {

  private Long duration;
  private int raceChance;
  private ChatBrawl plugin;
  private boolean useFirework;
  private boolean isEnabled;
  private RewardRandomizer rewardRandomizer;
  private ConfigurationSection commandRewardSection;
  private HashMap<String, Integer> commandRewardsMap;

  public Race(
      ChatBrawl plugin,
      Long duration,
      int raceChance,
      boolean useFirework,
      boolean isEnabled,
      ConfigurationSection commandRewardSection) {
    this.duration = duration * 20;
    this.raceChance = raceChance;
    this.plugin = plugin;
    this.useFirework = useFirework;
    this.isEnabled = isEnabled;
    this.commandRewardsMap = new HashMap<>();
    this.commandRewardSection = commandRewardSection;
    this.rewardRandomizer = new RewardRandomizer(plugin);
    fillCommandRewards();
  }

  public void shootFireWorkIfEnabledAsync(Player player) {
    if (useFirework) {
      Bukkit.getServer()
          .getScheduler()
          .runTaskLater(
              plugin,
                  () -> FireworkCreator.spawnFirework(player),
              0);
    }
  }

  public void shootFireWorkIfEnabled(Player player) {
    if (useFirework) {
      FireworkCreator.spawnFirework(player);
    }
  }

  public void fillCommandRewards() {
    try {
      for (String commandEntry :
          Objects.requireNonNull(commandRewardSection).getKeys(false)) {
        String command = commandRewardSection.getString(commandEntry + ".command");
        int commandChance = commandRewardSection.getInt(commandEntry + ".chance");
        if (command == null) {
          throw new RaceException("the command string for a reward is missing in the config!");
        } else if (commandChance == 0) {
          throw new RaceException("the command chance for a reward is missing in the config!");
        }
        commandRewardsMap.put(command, commandChance);
      }

    } catch (RaceException e) {
      RaceException.handleConfigException(getPlugin(), e);
    }
  }

  public Long getDuration() {
    return duration;
  }

  public int getChance() {
    return raceChance;
  }

  public RewardRandomizer getRewardRandomizer() {
    return rewardRandomizer;
  }

  public HashMap<String, Integer> getCommandRewardsMap() {
    return commandRewardsMap;
  }

  public ChatBrawl getPlugin() {
    return plugin;
  }

  public boolean isEnabled() {
    return isEnabled;
  }
}
