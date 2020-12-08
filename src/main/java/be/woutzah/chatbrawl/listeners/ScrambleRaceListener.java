package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.ScrambleRace;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ScrambleRaceListener implements Listener {

    private final RaceCreator raceCreator;
    private final ScrambleRace scrambleRace;
    private final Printer printer;
    private final RewardRandomizer rewardRandomizer;
    private final ChatBrawl plugin;

    public ScrambleRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.scrambleRace = plugin.getScrambleRace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = scrambleRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler
    public void checkScrambledWordInChat(AsyncPlayerChatEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.SCRAMBLE)) {
            if (plugin.getDisabledWorldsList().contains(event.getPlayer().getLocation().getWorld().getName())){
                return;
            }
            if (!plugin.getAllowCreative()) {
                if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
            }
            String message = event.getMessage();
            if (scrambleRace.getIgnoredCommandsList().stream()
                    .anyMatch(ic -> message.toLowerCase().startsWith(ic.toLowerCase()))){
                return;
            }
            if (printer.stripColors(message).equals(scrambleRace.getOriginalWord())) {
                Player player = event.getPlayer();
                if(raceCreator.isEndBroadcastsEnabled()) {
                    Bukkit.broadcast(printer.getAnnounceScrambleWinner(player,scrambleRace.getOriginalWord()), "cb.default");
                }
                if (!printer.getPersonalScrambleWinner().isEmpty()) {
                    player.sendMessage(printer.getPersonalScrambleWinner());
                }
                if (plugin.isSoundEnabled()){
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
                }
                scrambleRace.shootFireWorkIfEnabledAsync(player);
                rewardRandomizer.executeRandomCommand(scrambleRace.getCommandRewardsMap(), player);
                raceCreator.getScrambleRaceTask().cancel();
                try {
                    raceCreator.getActionbarTask().cancel();
                }catch (Exception ignored){

                }
                raceCreator.setCurrentRunningRace(RaceType.NONE);
            }
        }
    }
}
