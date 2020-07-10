package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import be.woutzah.chatbrawl.utils.FireworkCreator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class Race {

  private Long duration;
  private int raceChance;
  private ChatBrawl plugin;
  private boolean useFirework;
  private boolean isEnabled;
  private RewardRandomizer rewardRandomizer;
  private ConfigurationSection commandRewardSection;
  private HashMap<List<String>, Integer> commandRewardsMap;
  private List<String> ignoredCommandsList;

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
    fillIgnoredCommands();
  }

  private void fillIgnoredCommands() {
    this.ignoredCommandsList = new ArrayList<>();
    this.ignoredCommandsList = plugin.getConfig().getStringList("ignored-commands");
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
        List<String> commands = commandRewardSection.getStringList(commandEntry + ".commands");
        if (commands.isEmpty()) {
          throw new RaceException("the commandlist for a reward is missing in the config!");
        }
        int commandChance = commandRewardSection.getInt(commandEntry + ".chance");
        if (commandChance == 0) {
          throw new RaceException("the command chance for rewards is missing in the config!");
        }
        commandRewardsMap.put(commands, commandChance);
      }
    } catch (RaceException e) {
      RaceException.handleConfigException(plugin, e);
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

  public HashMap<List<String>, Integer> getCommandRewardsMap() {
    return commandRewardsMap;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public List<String> getIgnoredCommandsList() {
    return ignoredCommandsList;
  }
}
