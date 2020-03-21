package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class FoodRaceListener implements Listener {
    private ChatBrawl plugin;

    public FoodRaceListener(ChatBrawl plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodConsume(PlayerItemConsumeEvent event) {
        if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.food)) {
            UUID uuid = event.getPlayer().getUniqueId();
            Player player = event.getPlayer();
            if (event.getItem().getType().equals(plugin.getFoodRace().getCurrentItemStack().getType())) {
                if (plugin.getFoodRace().getPlayerScores().containsKey(uuid)) {
                    int currentAmount = plugin.getFoodRace().getPlayerScores().get(uuid);
                    currentAmount++;
                    plugin.getFoodRace().getPlayerScores().put(uuid, currentAmount);
                    if (plugin.getFoodRace().getPlayerScores().get(uuid)
                            == plugin.getFoodRace().getCurrentItemStack().getAmount()) {
                        plugin
                                .getServer()
                                .broadcastMessage(
                                        plugin.getPrinter().getAnnounceFoodWinner(player));
                        if (!plugin.getPrinter().getPersonalFoodWinner().isEmpty()) {
                            player.sendMessage(plugin.getPrinter().getPersonalFoodWinner());
                        }
                        plugin.getFoodRace().shootFireWorkIfEnabled(player);
                        plugin
                                .getFoodRace()
                                .getRewardRandomizer()
                                .executeRandomCommand(plugin.getFoodRace().getCommandRewardsMap(), player);
                        plugin.getRaceCreator().getFoodRaceTask().cancel();
                        plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
                        plugin.getFoodRace().removeOnlinePlayers();
                    }
                }
            }
        }
    }

    @EventHandler
    public void addPlayerFoodRace(PlayerJoinEvent event) {
        if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.food)) {
            if (!plugin.getFoodRace().getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
                plugin.getFoodRace().getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
            }
        }
    }

}
