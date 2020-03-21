package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatRaceListener implements Listener {

  private ChatBrawl plugin;

  public ChatRaceListener(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void checkWordInChat(AsyncPlayerChatEvent event) {
    // if (!plugin.getRaceCreator().getChatRaceTask().isCancelled()) {
    if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.chat)) {
      String message = event.getMessage();
      Player player = event.getPlayer();
      if (message.equals(plugin.getChatrace().getWordToGuess())) {
        plugin
            .getServer()
            .broadcastMessage(plugin.getPrinter().getAnnounceChatWinner(player));
        if (!plugin.getPrinter().getPersonalChatWinner().isEmpty()) {
          player.sendMessage(plugin.getPrinter().getPersonalChatWinner());
        }
        plugin.getChatrace().shootFireWorkIfEnabledAsync(player);
        plugin
            .getChatrace()
            .getRewardRandomizer()
            .executeRandomCommand(plugin.getChatrace().getCommandRewardsMap(), player);
        plugin.getRaceCreator().getChatRaceTask().cancel();
        plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
      }
    }
  }
}
