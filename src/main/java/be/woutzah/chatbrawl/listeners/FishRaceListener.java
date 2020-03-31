package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class FishRaceListener implements Listener {

  private ChatBrawl plugin;

  public FishRaceListener(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void checkFishedObjects(PlayerFishEvent event) {
    if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.fish)) {
      if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
        Item fish = (Item) event.getCaught();
        ItemStack fishStack = fish.getItemStack();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (fishStack.getType().equals(plugin.getFishRace().getCurrentItemStack().getType())) {
          int currentAmount = plugin.getFishRace().getPlayerScores().get(uuid);
          currentAmount++;
          plugin.getFishRace().getPlayerScores().put(uuid, currentAmount);
          if (plugin.getFishRace().getPlayerScores().get(uuid)
              == plugin.getFishRace().getCurrentItemStack().getAmount()) {
            plugin
                .getServer()
                .broadcastMessage(
                    plugin.getPrinter().getAnnounceFishWinner(player));
            if (!plugin.getPrinter().getPersonalFishWinner().isEmpty()) {
              player.sendMessage(plugin.getPrinter().getPersonalFishWinner());
            }
            plugin.getFishRace().shootFireWorkIfEnabled(player);
            plugin
                .getFishRace()
                .getRewardRandomizer()
                .executeRandomCommand(plugin.getFishRace().getCommandRewardsMap(), player);
            plugin.getRaceCreator().getFishRaceTask().cancel();
            plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
            plugin.getFishRace().removeOnlinePlayers();
          }
        }
      }
    }
  }

  @EventHandler
  public void addPlayerFishRace(PlayerJoinEvent event) {
    if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.fish)) {
      if (!plugin.getFishRace().getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
        plugin.getFishRace().getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
      }
    }
  }
}
