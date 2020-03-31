package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class QuizRace extends Race {

    private HashMap<String, List<String>> questionsMap;
    private String currentQuestion;
    private List<String> currentAnswers;

    public QuizRace(ChatBrawl plugin) {
        super(
                plugin,
                plugin.getQuizraceConfig().getLong("quizrace.duration"),
                plugin.getQuizraceConfig().getInt("quizrace.chance"),
                plugin.getQuizraceConfig().getBoolean("quizrace.enable-firework"),
                plugin.getQuizraceConfig().getBoolean("quizrace.enabled"),
                plugin.getQuizraceConfig().getConfigurationSection("quizrace.rewards.commands"));
        this.questionsMap = new HashMap<>();
        getQuestionsFromConfig();
    }

    private void getQuestionsFromConfig() {
        try {
            ConfigurationSection configSection =
                    getPlugin().getQuizraceConfig().getConfigurationSection("quizrace.questions");
            for (String questionAnswerEntry :
                    Objects.requireNonNull(configSection).getKeys(false)) {
                String question = configSection.getString( questionAnswerEntry + ".question");
                List<String> answers = configSection.getStringList(questionAnswerEntry + ".answer")
                        .stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                if (question == null) {
                    throw new RaceException("Empty question in quizrace for questionsection number " + questionAnswerEntry);
                }
                if (answers.isEmpty()) {
                    throw new RaceException("Empty answer in quizrace for questionsection number " + questionAnswerEntry);
                }
                questionsMap.put(question, answers);
            }

        } catch (RaceException e) {
            RaceException.handleConfigException(getPlugin(), e);
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
}
