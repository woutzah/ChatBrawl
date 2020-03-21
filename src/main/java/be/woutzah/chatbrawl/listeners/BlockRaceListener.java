package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class BlockRaceListener implements Listener {

  private ChatBrawl plugin;

  public BlockRaceListener(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void checkBlocksMined(BlockBreakEvent event) {
    if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.block)) {
      Block minedBlock = event.getBlock();
      UUID uuid = event.getPlayer().getUniqueId();
      Player player = event.getPlayer();
      if (minedBlock.getType().equals(plugin.getBlockRace().getCurrentItemStack().getType())) {
        if (plugin.getBlockRace().getPlayerScores().containsKey(uuid)) {
          int currentAmount = plugin.getBlockRace().getPlayerScores().get(uuid);
          currentAmount++;
          plugin.getBlockRace().getPlayerScores().put(uuid, currentAmount);
          if (plugin.getBlockRace().getPlayerScores().get(uuid)
              == plugin.getBlockRace().getCurrentItemStack().getAmount()) {
            plugin
                .getServer()
                .broadcastMessage(
                    plugin.getPrinter().getAnnounceBlockWinner(player));
            if (!plugin.getPrinter().getPersonalBlockWinner().isEmpty()) {
              player.sendMessage(plugin.getPrinter().getPersonalBlockWinner());
            }
            plugin.getBlockRace().shootFireWorkIfEnabled(player);
            plugin
                .getBlockRace()
                .getRewardRandomizer()
                .executeRandomCommand(plugin.getBlockRace().getCommandRewardsMap(), player);
            plugin.getRaceCreator().getBlockRaceTask().cancel();
            plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
            plugin.getBlockRace().removeOnlinePlayers();
          }
        }
      }
    }
  }

  @EventHandler
  public void addPlayerBlockRace(PlayerJoinEvent event) {
    if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.block)) {
      if (!plugin.getBlockRace().getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
        plugin.getBlockRace().getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
      }
    }
  }
}
