package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.BlockRace;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

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
    public void checkBlocksMined(BlockBreakEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.BLOCK)) {
            if (plugin.getDisabledWorldsList().contains(event.getPlayer().getLocation().getWorld().getName())){
                return;
            }
            if (!plugin.getAllowCreative()) {
                if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
            }
            Block minedBlock = event.getBlock();
            if (minedBlock.getType().equals(blockRace.getCurrentItemStack().getType())) {
                UUID uuid = event.getPlayer().getUniqueId();
                if (blockRace.getPlayerScores().containsKey(uuid)) {
                    int currentAmount = blockRace.getPlayerScores().get(uuid);
                    currentAmount++;
                    blockRace.getPlayerScores().put(uuid, currentAmount);
                    if (blockRace.getPlayerScores().get(uuid)
                            == blockRace.getCurrentItemStack().getAmount()) {
                        Player player = event.getPlayer();
                        if(raceCreator.isEndBroadcastsEnabled()) {
                            Bukkit.broadcast(printer.getAnnounceBlockWinner(player), "cb.default");
                        }
                        if (!printer.getPersonalBlockWinner().isEmpty()) {
                            player.sendMessage(printer.getPersonalBlockWinner());
                        }
                        if (plugin.isSoundEnabled()){
                            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
                        }
                        blockRace.shootFireWorkIfEnabled(player);
                        rewardRandomizer.executeRandomCommand(blockRace.getCommandRewardsMap(), player);
                        raceCreator.getBlockRaceTask().cancel();
                        try {
                            raceCreator.getActionbarTask().cancel();
                        }catch (Exception ignored){

                        }
                        raceCreator.setCurrentRunningRace(RaceType.NONE);
                        blockRace.removeOnlinePlayers();
                    }
                }
            }
        }
    }

    @EventHandler
    public void addPlayerBlockRace(PlayerJoinEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.BLOCK)) {
            if (!blockRace.getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
                blockRace.getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
            }
        }
    }
}
