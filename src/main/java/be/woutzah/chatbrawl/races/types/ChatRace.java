package be.woutzah.chatbrawl.races.types;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.Race;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;

public class ChatRace extends Race {

  private String wordToGuess;
  private List<String> allWordsList;
  private FileConfiguration chatRaceConfig;
  private String chatraceName;
  private List<String> chatraceStart;
  private String chatraceEnd;
  private List<String> chatraceWinner;
  private String chatraceWinnerPersonal;
  private String chatraceActionBar;

  public ChatRace(ChatBrawl plugin, FileConfiguration config) {
    super(
        plugin,
        config.getLong("chatrace.duration"),
        config.getInt("chatrace.chance"),
        config.getBoolean("chatrace.enable-firework"),
        config.getBoolean("chatrace.enabled"),
        config.getConfigurationSection("chatrace.rewards.commands")
    );
    this.chatRaceConfig = config;
    this.allWordsList = chatRaceConfig.getStringList("chatrace.words");
    generateRandomWord();
    initializeLanguageEntries();
  }

  private void initializeLanguageEntries() {
    this.chatraceName = chatRaceConfig.getString("language.chatrace-name");
    this.chatraceStart = chatRaceConfig.getStringList("language.chatrace-start");
    this.chatraceEnd = chatRaceConfig.getString("language.chatrace-ended");
    this.chatraceWinner = chatRaceConfig.getStringList("language.chatrace-winner");
    this.chatraceWinnerPersonal = chatRaceConfig.getString("language.chatrace-winner-personal");
    this.chatraceActionBar = chatRaceConfig.getString("language.chatrace-actionbar-start");
  }

  public void generateRandomWord() {
    Random random = new Random();
    wordToGuess = allWordsList.get(random.nextInt(allWordsList.size()));
  }

  public String getWordToGuess() {
    return wordToGuess;
  }

  public String getChatraceName() {
    return chatraceName;
  }

  public List<String> getChatraceStart() {
    return chatraceStart;
  }

  public String getChatraceEnd() {
    return chatraceEnd;
  }

  public List<String> getChatraceWinner() {
    return chatraceWinner;
  }

  public String getChatraceWinnerPersonal() {
    return chatraceWinnerPersonal;
  }

  public String getChatraceActionBar() {
    return chatraceActionBar;
  }
}
