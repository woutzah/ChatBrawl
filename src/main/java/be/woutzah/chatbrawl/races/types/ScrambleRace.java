package be.woutzah.chatbrawl.races.types;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.Race;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ScrambleRace extends Race {

    private String wordToUnscramble;
    private String originalWord;
    private List<String> allWordsList;
    private FileConfiguration scrambleRaceConfig;
    private int difficulty;
    private Random random;
    private String scrambleraceName;
    private List<String> scrambleraceStart;
    private String scrambleraceEnd;
    private List<String> scrambleraceWinner;
    private String scrambleraceWinnerPersonal;
    private String scrambleraceActionBar;

    public ScrambleRace(ChatBrawl plugin, FileConfiguration config) {
        super(
                plugin,
                config.getLong("scramblerace.duration"),
                config.getInt("scramblerace.chance"),
                config.getBoolean("scramblerace.enable-firework"),
                config.getBoolean("scramblerace.enabled"),
                config.getConfigurationSection("scramblerace.rewards.commands"));
        this.scrambleRaceConfig = config;
        this.allWordsList = scrambleRaceConfig.getStringList("scramblerace.words");
        this.difficulty = scrambleRaceConfig.getInt("scramblerace.difficulty");
        this.random = new Random();
        this.originalWord = "";
        this.wordToUnscramble = "";
        generateRandomScrambledWord();
        initializeLanguageEntries();
    }

    private void initializeLanguageEntries() {
        this.scrambleraceName = scrambleRaceConfig.getString("language.scramblerace-name");
        this.scrambleraceStart = scrambleRaceConfig.getStringList("language.scramblerace-start");
        this.scrambleraceEnd = scrambleRaceConfig.getString("language.scramblerace-ended");
        this.scrambleraceWinner = scrambleRaceConfig.getStringList("language.scramblerace-winner");
        this.scrambleraceWinnerPersonal = scrambleRaceConfig.getString("language.scramblerace-winner-personal");
        this.scrambleraceActionBar = scrambleRaceConfig.getString("language.scramblerace-actionbar-start");
    }

    public void generateRandomScrambledWord() {
        originalWord = allWordsList.get(random.nextInt(allWordsList.size()));
        wordToUnscramble = scramble(originalWord);
    }

    public String scramble(String word) {
        List<Character> chars = word.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toList());
        Collections.shuffle(chars);
        String shuffledWord = chars.toString()
                .substring(1, 3 * chars.size() - 1)
                .replaceAll(", ", "");
        switch (difficulty) {
            case 1:
                return shuffledWord;
            case 2:
                return randomCase(shuffledWord);
            case 3:
                return swapInNumbers(randomCase(shuffledWord));
        }
        return null;
    }

    private String randomCase(String word) {
        StringBuilder newWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            int number = random.nextInt(2);
            if (number == 0) {
                newWord.append(word.substring(i, i + 1).toLowerCase());
            } else {
                newWord.append(word.substring(i, i + 1).toUpperCase());
            }
        }
        return newWord.toString();
    }

    private String swapInNumbers(String word) {
        return word.replace("A", "5").replace("a", "5")
                .replace("E", "3").replace("a", "3")
                .replace("O", "0").replace("o", "0")
                .replace("I", "1").replace("i", "1");
    }

    public String getWordToUnscramble() {
        return wordToUnscramble;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public String getScrambleraceName() {
        return scrambleraceName;
    }

    public List<String> getScrambleraceStart() {
        return scrambleraceStart;
    }

    public String getScrambleraceEnd() {
        return scrambleraceEnd;
    }

    public List<String> getScrambleraceWinner() {
        return scrambleraceWinner;
    }

    public String getScrambleraceWinnerPersonal() {
        return scrambleraceWinnerPersonal;
    }

    public String getScrambleraceActionBar() {
        return scrambleraceActionBar;
    }
}
