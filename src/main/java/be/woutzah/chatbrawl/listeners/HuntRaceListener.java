package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class HuntRaceListener implements Listener {

  private ChatBrawl plugin;

  public HuntRaceListener(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void checkMobsKilled(EntityDeathEvent event) {
    if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.hunt)) {
      if (event.getEntity().getKiller() != null) {
        Entity mob = event.getEntity();
        Player player = event.getEntity().getKiller();
        UUID uuid = event.getEntity().getKiller().getUniqueId();
        if (mob.getType().equals(plugin.getHuntRace().getCurrentEntityType())) {
          int currentAmount = plugin.getHuntRace().getPlayerScores().get(uuid);
          currentAmount++;
          plugin.getHuntRace().getPlayerScores().put(uuid, currentAmount);
          if (plugin.getHuntRace().getPlayerScores().get(uuid)
              == plugin.getHuntRace().getCurrentAmount()) {
            plugin
                .getServer()
                .broadcastMessage(
                    plugin.getPrinter().getAnnounceHuntWinner(player));
            if (!plugin.getPrinter().getPersonalHuntWinner().isEmpty()) {
              player.sendMessage(plugin.getPrinter().getPersonalHuntWinner());
            }
            plugin.getHuntRace().shootFireWorkIfEnabled(player);
            plugin
                .getFishRace()
                .getRewardRandomizer()
                .executeRandomCommand(plugin.getFishRace().getCommandRewardsMap(), player);
            plugin.getRaceCreator().getHuntRaceTask().cancel();
            plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
            plugin.getHuntRace().removeOnlinePlayers();
          }
        }
      }
    }
  }

  @EventHandler
  public void addPlayerFishRace(PlayerJoinEvent event) {
    if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.hunt)) {
      if (!plugin.getHuntRace().getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
        plugin.getHuntRace().getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
      }
    }
  }
}
