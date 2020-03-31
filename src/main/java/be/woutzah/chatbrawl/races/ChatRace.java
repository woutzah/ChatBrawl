package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;

import java.util.List;
import java.util.Random;

public class ChatRace extends Race {

  private String wordToGuess;

  public ChatRace(ChatBrawl plugin) {
    super(
        plugin,
        plugin.getChatraceConfig().getLong("chatrace.duration"),
        plugin.getChatraceConfig().getInt("chatrace.chance"),
        plugin.getChatraceConfig().getBoolean("chatrace.enable-firework"),
        plugin.getChatraceConfig().getBoolean("chatrace.enabled"),
        plugin.getChatraceConfig().getConfigurationSection("chatrace.rewards.commands"));
    getRandomWordFromConfig();
  }

  public void getRandomWordFromConfig() {
    Random random = new Random();
    List<String> allWords = getPlugin().getChatraceConfig().getStringList("chatrace.words");
    wordToGuess = allWords.get(random.nextInt(allWords.size()));
  }

  public String getWordToGuess() {
    return wordToGuess;
  }
}
