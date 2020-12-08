package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.BlockRace;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import be.woutzah.chatbrawl.utils.Permission;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BlockRaceListener implements Listener {

    private final RaceCreator raceCreator;
    private final BlockRace blockRace;
    private final Printer printer;
    private final RewardRandomizer rewardRandomizer;
    private final ChatBrawl plugin;

    public BlockRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.blockRace = plugin.getBlockRace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = blockRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler
    public void checkBlocksMined(final BlockBreakEvent event) {
        if (raceCreator.getCurrentRunningRace() != RaceType.BLOCK) {
            return;
        }

        final Player player = event.getPlayer();

        if (plugin.isDisabledInWorld(player.getWorld())) {
            return;
        }

        if (!plugin.getAllowCreative() && player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (event.getBlock().getType() != blockRace.getCurrentItemStack().getType()) {
            return;
        }

        final Integer score = blockRace.getPlayerScores().get(player.getUniqueId());

        if (score == null) {
            return;
        }

        if (score + 1 >= blockRace.getCurrentItemStack().getAmount()) {
            if (raceCreator.isEndBroadcastsEnabled()) {
                Bukkit.broadcast(printer.getAnnounceBlockWinner(player), Permission.DEFAULT);
            }

            if (!printer.getPersonalBlockWinner().isEmpty()) {
                player.sendMessage(printer.getPersonalBlockWinner());
            }

            blockRace.shootFireWorkIfEnabled(player);
            rewardRandomizer.executeRandomCommand(blockRace.getCommandRewardsMap(), player);
            raceCreator.getBlockRaceTask().cancel();

            try {
                raceCreator.getActionbarTask().cancel();
            } catch (Exception ignored) { }

            raceCreator.setCurrentRunningRace(RaceType.NONE);
            blockRace.removeOnlinePlayers();
            return;
        }

        blockRace.getPlayerScores().put(player.getUniqueId(), score + 1);
    }

    @EventHandler
    public void addPlayerBlockRace(PlayerJoinEvent event) {
        if (raceCreator.getCurrentRunningRace() != RaceType.BLOCK) {
            return;
        }

        blockRace.getPlayerScores().putIfAbsent(event.getPlayer().getUniqueId(), 0);
    }
}
