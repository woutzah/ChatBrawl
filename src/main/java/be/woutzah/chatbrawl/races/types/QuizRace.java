package be.woutzah.chatbrawl.races.types;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.races.Race;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class QuizRace extends Race {

    private ChatBrawl plugin;
    private HashMap<String, List<String>> questionsMap;
    private String currentQuestion;
    private List<String> currentAnswers;
    private FileConfiguration quizRaceConfig;
    private String quizraceName;
    private List<String> quizraceStart;
    private String quizraceEnd;
    private List<String> quizraceWinner;
    private String quizraceWinnerPersonal;
    private String quizraceActionBar;

    public QuizRace(ChatBrawl plugin, FileConfiguration config) {
        super(
                plugin,
                config.getLong("quizrace.duration"),
                config.getInt("quizrace.chance"),
                config.getBoolean("quizrace.enable-firework"),
                config.getBoolean("quizrace.enabled"),
                config.getConfigurationSection("quizrace.rewards.commands"));
        this.plugin = plugin;
        this.quizRaceConfig = config;
        this.questionsMap = new HashMap<>();
        getQuestionsFromConfig();
        initializeLanguageEntries();
    }

    private void initializeLanguageEntries() {
        this.quizraceName = quizRaceConfig.getString("language.quizrace-name");
        this.quizraceStart = quizRaceConfig.getStringList("language.quizrace-start");
        this.quizraceEnd = quizRaceConfig.getString("language.quizrace-ended");
        this.quizraceWinner = quizRaceConfig.getStringList("language.quizrace-winner");
        this.quizraceWinnerPersonal = quizRaceConfig.getString("language.quizrace-winner-personal");
        this.quizraceActionBar = quizRaceConfig.getString("language.quizrace-actionbar-start");
    }

    private void getQuestionsFromConfig() {
        try {
            ConfigurationSection configSection =
                    quizRaceConfig.getConfigurationSection("quizrace.questions");
            for (String questionAnswerEntry :
                    Objects.requireNonNull(configSection).getKeys(false)) {
                String question = configSection.getString( questionAnswerEntry + ".question");
                if (question == null) {
                    throw new RaceException("Empty question in quizrace for questionsection number " + questionAnswerEntry);
                }
                List<String> answers = configSection.getStringList(questionAnswerEntry + ".answer")
                        .stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                if (answers.isEmpty()) {
                    throw new RaceException("Empty answer in quizrace for questionsection number " + questionAnswerEntry);
                }
                questionsMap.put(question, answers);
            }

        } catch (RaceException e) {
            RaceException.handleConfigException(plugin, e);
        }
    }

    public void generateRandomQuestionWithAnswer() {
        Object[] questions = questionsMap.keySet().toArray();
        currentQuestion = (String) questions[new Random().nextInt(questions.length)];
        currentAnswers = questionsMap.get(currentQuestion);
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public List<String> getCurrentAnswers() {
        return currentAnswers;
    }

    public String getQuizraceName() {
        return quizraceName;
    }

    public List<String> getQuizraceStart() {
        return quizraceStart;
    }

    public String getQuizraceEnd() {
        return quizraceEnd;
    }

    public List<String> getQuizraceWinner() {
        return quizraceWinner;
    }

    public String getQuizraceWinnerPersonal() {
        return quizraceWinnerPersonal;
    }

    public String getQuizraceActionBar() {
        return quizraceActionBar;
    }
}
