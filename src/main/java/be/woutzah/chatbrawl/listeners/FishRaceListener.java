package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.types.FishRace;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class FishRaceListener implements Listener {

  private RaceCreator raceCreator;
  private FishRace fishRace;
  private Printer printer;
  private RewardRandomizer rewardRandomizer;
  private ChatBrawl plugin;

  public FishRaceListener(ChatBrawl plugin) {
    this.raceCreator = plugin.getRaceCreator();
    this.fishRace = plugin.getFishRace();
    this.printer = plugin.getPrinter();
    this.rewardRandomizer = fishRace.getRewardRandomizer();
    this.plugin = plugin;
  }

  @EventHandler
  public void checkFishedObjects(PlayerFishEvent event) {
    if (raceCreator.getCurrentRunningRace().equals(RaceType.FISH)) {
      if (plugin.getDisabledWorldsList().contains(event.getPlayer().getLocation().getWorld().getName())){
        return;
      }
      if (!plugin.getAllowCreative()) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
          return;
        }
      }
      if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
        Item fish = (Item) event.getCaught();
        ItemStack fishStack = fish.getItemStack();
        if (fishStack.getType().equals(fishRace.getCurrentItemStack().getType())) {
          UUID uuid = event.getPlayer().getUniqueId();
          int currentAmount = fishRace.getPlayerScores().get(uuid);
          currentAmount++;
          fishRace.getPlayerScores().put(uuid, currentAmount);
          if (fishRace.getPlayerScores().get(uuid)
              == fishRace.getCurrentItemStack().getAmount()) {
            Player player = event.getPlayer();
            if(raceCreator.isEndBroadcastsEnabled()) {
              Bukkit.broadcast(printer.getAnnounceFishWinner(player), "cb.default");
            }
            if (!printer.getPersonalFishWinner().isEmpty()) {
              player.sendMessage(printer.getPersonalFishWinner());
            }
            if (plugin.isSoundEnabled()){
              Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
            }
            fishRace.shootFireWorkIfEnabled(player);
            rewardRandomizer.executeRandomCommand(fishRace.getCommandRewardsMap(), player);
            raceCreator.getFishRaceTask().cancel();
            try {
              raceCreator.getActionbarTask().cancel();
            }catch (Exception ignored){

            }
            raceCreator.setCurrentRunningRace(RaceType.NONE);
            fishRace.removeOnlinePlayers();
          }
        }
      }
    }
  }

  @EventHandler
  public void addPlayerFishRace(PlayerJoinEvent event) {
    if (raceCreator.getCurrentRunningRace().equals(RaceType.FISH)) {
      if (!fishRace.getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
        fishRace.getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
      }
    }
  }
}
