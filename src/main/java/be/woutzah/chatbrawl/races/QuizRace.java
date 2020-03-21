package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class QuizRace extends Race {

    private HashMap<String, String> questionsMap;
    private String currentQuestion;
    private String currentAnswer;

    public QuizRace(ChatBrawl plugin) {
        super(
                plugin,
                plugin.getConfig().getLong("quizrace.duration"),
                plugin.getConfig().getInt("quizrace.chance"),
                plugin.getConfig().getBoolean("quizrace.enable-firework"),
                plugin.getConfig().getBoolean("quizrace.enabled"),
                plugin.getConfig().getConfigurationSection("quizrace.rewards.commands"));
        this.questionsMap = new HashMap<>();
        getQuestionsFromConfig();
    }

    private void getQuestionsFromConfig() {
        try {
            ConfigurationSection configSection =
                    getPlugin().getConfig().getConfigurationSection("quizrace.questions");
            for (String questionAnswerEntry :
                    Objects.requireNonNull(configSection).getKeys(false)) {
                String question = configSection.getString( questionAnswerEntry + ".question");
                String answer = configSection.getString(questionAnswerEntry + ".answer");
                if (question == null) {
                    throw new RaceException("Empty question in quizrace for questionsection number " + questionAnswerEntry);
                }
                if (answer == null) {
                    throw new RaceException("Empty answer in quizrace for questionsection number " + questionAnswerEntry);
                }
                questionsMap.put(question, answer);
            }

        } catch (RaceException e) {
            RaceException.handleConfigException(getPlugin(), e);
        }
    }

    public void generateRandomQuestionWithAnswer() {
        Object[] questions = questionsMap.keySet().toArray();
        currentQuestion = (String) questions[new Random().nextInt(questions.length)];
        currentAnswer = questionsMap.get(currentQuestion);
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
    }
}
