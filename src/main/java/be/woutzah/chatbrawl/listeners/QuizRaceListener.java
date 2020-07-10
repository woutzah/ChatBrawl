package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.types.QuizRace;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class QuizRaceListener implements Listener {

    private RaceCreator raceCreator;
    private QuizRace quizRace;
    private Printer printer;
    private RewardRandomizer rewardRandomizer;
    private ChatBrawl plugin;

    public QuizRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.quizRace = plugin.getQuizRace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = quizRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler
    public void checkAnswerInChat(AsyncPlayerChatEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.quiz)) {
            if (plugin.getDisabledWorldsList().contains(event.getPlayer().getLocation().getWorld().getName())){
                return;
            }
            if (!plugin.getAllowCreative()) {
                if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
            }
            String message = event.getMessage();
            if (quizRace.getIgnoredCommandsList().stream().anyMatch(ic -> message.toLowerCase().startsWith(ic.toLowerCase()))){
                return;
            }
            if (quizRace.getCurrentAnswers().stream().anyMatch(message::equalsIgnoreCase)){
                Player player = event.getPlayer();
                Bukkit.broadcast(printer.getAnnounceQuizWinner(player),"cb.default");
                if (!printer.getPersonalQuizWinner().isEmpty()) {
                    player.sendMessage(printer.getPersonalQuizWinner());
                }
                if (plugin.isSoundEnabled()){
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
                }
                quizRace.shootFireWorkIfEnabledAsync(player);
                rewardRandomizer.executeRandomCommand(quizRace.getCommandRewardsMap(), player);
                raceCreator.getQuizRaceTask().cancel();
                raceCreator.setCurrentRunningRace(RaceType.none);
            }
        }
    }

}
