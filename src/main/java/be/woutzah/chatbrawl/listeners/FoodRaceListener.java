package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.types.FoodRace;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class FoodRaceListener implements Listener {

    private RaceCreator raceCreator;
    private FoodRace foodRace;
    private Printer printer;
    private RewardRandomizer rewardRandomizer;
    private ChatBrawl plugin;

    public FoodRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.foodRace = plugin.getFoodRace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = foodRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodConsume(PlayerItemConsumeEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.FOOD)) {
            if (plugin.getDisabledWorldsList().contains(event.getPlayer().getLocation().getWorld().getName())){
                return;
            }
            if (!plugin.getAllowCreative()) {
                if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
            }
            if (event.getItem().getType().equals(foodRace.getCurrentItemStack().getType())) {
                UUID uuid = event.getPlayer().getUniqueId();
                if (foodRace.getPlayerScores().containsKey(uuid)) {
                    int currentAmount = foodRace.getPlayerScores().get(uuid);
                    currentAmount++;
                    foodRace.getPlayerScores().put(uuid, currentAmount);
                    if (foodRace.getPlayerScores().get(uuid)
                            == foodRace.getCurrentItemStack().getAmount()) {
                        Player player = event.getPlayer();
                        if(raceCreator.isEndBroadcastsEnabled()) {
                            Bukkit.broadcast(printer.getAnnounceFoodWinner(player), "cb.default");
                        }
                        if (!printer.getPersonalFoodWinner().isEmpty()) {
                            player.sendMessage(printer.getPersonalFoodWinner());
                        }
                        if (plugin.isSoundEnabled()){
                            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
                        }
                        foodRace.shootFireWorkIfEnabled(player);
                        rewardRandomizer.executeRandomCommand(foodRace.getCommandRewardsMap(), player);
                        raceCreator.getFoodRaceTask().cancel();
                        try {
                            raceCreator.getActionbarTask().cancel();
                        }catch (Exception ignored){

                        }
                        raceCreator.setCurrentRunningRace(RaceType.NONE);
                        foodRace.removeOnlinePlayers();
                    }
                }
            }
        }
    }

    @EventHandler
    public void addPlayerFoodRace(PlayerJoinEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.FOOD)) {
            if (!foodRace.getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
                foodRace.getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
            }
        }
    }

}
