package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class QuizRaceListener implements Listener {

    private ChatBrawl plugin;

    public QuizRaceListener(ChatBrawl plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void checkAnswerInChat(AsyncPlayerChatEvent event) {
        if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.quiz)) {
            String message = event.getMessage();
            Player player = event.getPlayer();
            if (plugin.getQuizRace().getCurrentAnswers().stream().anyMatch(message::equalsIgnoreCase)){
                plugin
                        .getServer()
                        .broadcastMessage(plugin.getPrinter().getAnnounceQuizWinner(player));
                if (!plugin.getPrinter().getPersonalQuizWinner().isEmpty()) {
                    player.sendMessage(plugin.getPrinter().getPersonalQuizWinner());
                }
                plugin.getQuizRace().shootFireWorkIfEnabledAsync(player);
                player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(),20);
                plugin
                        .getQuizRace()
                        .getRewardRandomizer()
                        .executeRandomCommand(plugin.getQuizRace().getCommandRewardsMap(), player);
                plugin.getRaceCreator().getQuizRaceTask().cancel();
                plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
            }
        }
    }

}
