package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.ChatRace;
import be.woutzah.chatbrawl.races.types.ScrambleRace;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ScrambleRaceListener implements Listener {

    private RaceCreator raceCreator;
    private ScrambleRace scrambleRace;
    private Printer printer;
    private RewardRandomizer rewardRandomizer;
    private ChatBrawl plugin;

    public ScrambleRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.scrambleRace = plugin.getScrambleRace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = scrambleRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler
    public void checkScrambledWordInChat(AsyncPlayerChatEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.scramble)) {
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
            if (message.equals(scrambleRace.getOriginalWord())) {
                Player player = event.getPlayer();
                Bukkit.broadcast(printer.getAnnounceScrambleWinner(player), "cb.default");
                if (!printer.getPersonalScrambleWinner().isEmpty()) {
                    player.sendMessage(printer.getPersonalScrambleWinner());
                }
                if (plugin.isSoundEnabled()){
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
                }
                scrambleRace.shootFireWorkIfEnabledAsync(player);
                rewardRandomizer.executeRandomCommand(scrambleRace.getCommandRewardsMap(), player);
                raceCreator.getScrambleRaceTask().cancel();
                raceCreator.setCurrentRunningRace(RaceType.none);
            }
        }
    }
}
