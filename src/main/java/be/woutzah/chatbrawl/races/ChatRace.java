package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;

import java.util.List;
import java.util.Random;

public class ChatRace extends Race {

  private String wordToGuess;

  public ChatRace(ChatBrawl plugin) {
    super(
        plugin,
        plugin.getConfig().getLong("chatrace.duration"),
        plugin.getConfig().getInt("chatrace.chance"),
        plugin.getConfig().getBoolean("chatrace.enable-firework"),
        plugin.getConfig().getBoolean("chatrace.enabled"),
        plugin.getConfig().getConfigurationSection("chatrace.rewards.commands"));
    getRandomWordFromConfig();
  }

  public void getRandomWordFromConfig() {
    Random random = new Random();
    List<String> allWords = getPlugin().getConfig().getStringList("chatrace.words");
    wordToGuess = allWords.get(random.nextInt(allWords.size()));
  }

  public String getWordToGuess() {
    return wordToGuess;
  }
}
