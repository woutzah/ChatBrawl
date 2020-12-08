package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.HuntRace;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class HuntRaceListener implements Listener {

    private final RaceCreator raceCreator;
    private final HuntRace huntRace;
    private final Printer printer;
    private final RewardRandomizer rewardRandomizer;
    private final ChatBrawl plugin;

    public HuntRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.huntRace = plugin.getHuntRace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = huntRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler
    public void checkMobsKilled(EntityDeathEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.HUNT)) {
            if (event.getEntity().getKiller() == null) {
                return;
            }
            if (plugin.getDisabledWorldsList().contains(event.getEntity().getKiller().getLocation().getWorld().getName())) {
                return;
            }
            if (!plugin.getAllowCreative()) {
                if (event.getEntity().getKiller().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
            }
            Player player = event.getEntity().getKiller();
            if (player != null) {
                Entity mob = event.getEntity();
                if (mob.getType().equals(huntRace.getCurrentEntityType())) {
                    UUID uuid = player.getUniqueId();
                    int currentAmount = huntRace.getPlayerScores().get(uuid);
                    currentAmount++;
                    huntRace.getPlayerScores().put(uuid, currentAmount);
                    if (huntRace.getPlayerScores().get(uuid)
                            == huntRace.getCurrentAmount()) {
                        if (raceCreator.isEndBroadcastsEnabled()) {
                            Bukkit.broadcast(printer.getAnnounceHuntWinner(player), "cb.default");
                        }
                        if (!printer.getPersonalHuntWinner().isEmpty()) {
                            player.sendMessage(printer.getPersonalHuntWinner());
                        }
                        if (plugin.isSoundEnabled()) {
                            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), plugin.getEndSound(), 1.0F, 8.0F));
                        }
                        huntRace.shootFireWorkIfEnabled(player);
                        rewardRandomizer.executeRandomCommand(huntRace.getCommandRewardsMap(), player);
                        raceCreator.getHuntRaceTask().cancel();
                        try {
                            raceCreator.getActionbarTask().cancel();
                        } catch (Exception ignored) {

                        }
                        raceCreator.setCurrentRunningRace(RaceType.NONE);
                        huntRace.removeOnlinePlayers();
                    }
                }
            }
        }
    }

    @EventHandler
    public void addPlayerFishRace(PlayerJoinEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.HUNT)) {
            if (!huntRace.getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
                huntRace.getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
            }
        }
    }
}
