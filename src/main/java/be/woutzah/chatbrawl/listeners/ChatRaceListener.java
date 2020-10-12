package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.ChatRace;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatRaceListener implements Listener {

    private RaceCreator raceCreator;
    private ChatRace chatRace;
    private Printer printer;
    private RewardRandomizer rewardRandomizer;
    private ChatBrawl plugin;

    public ChatRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.chatRace = plugin.getChatrace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = chatRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler
    public void checkWordInChat(AsyncPlayerChatEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.CHAT)) {
            if (plugin.getDisabledWorldsList().contains(event.getPlayer().getLocation().getWorld().getName())) {
                return;
            }
            if (!plugin.getAllowCreative()) {
                if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
            }
            String message = event.getMessage();
            if (chatRace.getIgnoredCommandsList().stream()
                    .anyMatch(ic -> message.toLowerCase().startsWith(ic.toLowerCase()))) {
                return;
            }
            if (printer.stripColors(message).equals(chatRace.getWordToGuess())) {
                Player player = event.getPlayer();
                if(raceCreator.isEndBroadcastsEnabled()) {
                    Bukkit.broadcast(printer.getAnnounceChatWinner(player), "cb.default");
                }
                if (!printer.getPersonalChatWinner().isEmpty()) {
                    player.sendMessage(printer.getPersonalChatWinner());
                }
                if (plugin.isSoundEnabled()){
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
                }
                chatRace.shootFireWorkIfEnabledAsync(player);
                rewardRandomizer.executeRandomCommand(chatRace.getCommandRewardsMap(), player);
                raceCreator.getChatRaceTask().cancel();
                try {
                    raceCreator.getActionbarTask().cancel();
                }catch (Exception ignored){

                }
                raceCreator.setCurrentRunningRace(RaceType.NONE);
            }
        }
    }
}
