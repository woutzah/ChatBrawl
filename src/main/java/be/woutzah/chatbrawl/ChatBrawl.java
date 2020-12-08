package be.woutzah.chatbrawl;

import be.woutzah.chatbrawl.commands.ChatBrawlCommand;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.files.FileManager;
import be.woutzah.chatbrawl.listeners.*;
import be.woutzah.chatbrawl.messages.LanguageManager;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.placeholders.Placeholders;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.types.*;
import be.woutzah.chatbrawl.utils.RaceRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ChatBrawl extends JavaPlugin {

    public static boolean langUtilsIsEnabled = false;
    public boolean isConfigCorrect;
    private ChatRace chatrace;
    private BlockRace blockRace;
    private FishRace fishRace;
    private HuntRace huntRace;
    private CraftRace craftRace;
    private QuizRace quizRace;
    private FoodRace foodRace;
    private ScrambleRace scrambleRace;
    private RaceCreator raceCreator;
    private Printer printer;
    private FileManager fileManager;
    private FileConfiguration languageConfig;
    private FileConfiguration chatraceConfig;
    private FileConfiguration blockraceConfig;
    private FileConfiguration fishraceConfig;
    private FileConfiguration huntraceConfig;
    private FileConfiguration craftraceConfig;
    private FileConfiguration quizraceConfig;
    private FileConfiguration foodraceConfig;
    private FileConfiguration scrambleraceConfig;
    private LanguageManager languageManager;
    private RaceRandomizer raceRandomizer;
    private List<String> disabledWorldsList;
    private boolean allowCreative;
    private boolean soundEnabled;
    private Sound beginSound;
    private Sound endSound;

    private boolean enableActionbar;

    @Override
    public void onEnable() {
        setupFiles();
        isConfigCorrect = configChecker();
        this.disabledWorldsList = new ArrayList<>();
        disabledWorldsList.addAll(this.getConfig().getStringList("disabled-worlds"));
        this.allowCreative = this.getConfig().getBoolean("allow-creative");
        this.soundEnabled = this.getConfig().getBoolean("enable-sound");
        this.enableActionbar = this.getConfig().getBoolean("enable-race-actionbar");

        try {
            beginSound = Sound.valueOf(this.getConfig().getString("sound-begin-races"));
            endSound = Sound.valueOf(this.getConfig().getString("sound-end-races"));
        } catch (Exception ex) {
            RaceException.handleConfigException(this, new RaceException("wrong sound in general config!"));
        }

        init();
        printer.printConsoleMessage();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }

        if (Bukkit.getPluginManager().getPlugin("LangUtils") != null) {
            langUtilsIsEnabled = true;
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getLogger().info("Chatbrawl has been disabled!");
    }

    public boolean configChecker() {
        try {
            // general settings
            if (!this.getConfig().isSet("events-enabled")) {
                throw new RaceException("The events-enabled setting is missing in general settings!");
            } else if (!this.getConfig().isSet("event-delay")) {
                throw new RaceException(
                        "The event-delay setting is missing in general settings in the config!");
            } else if (!this.getConfig().isSet("notify-event-login")) {
                throw new RaceException(
                        "The notify-event-login setting is missing in general settings in the config!");
            } else if (!this.getConfig().isSet("plugin-prefix")) {
                throw new RaceException(
                        "The plugin-prefix setting is missing in general settings in the config!");
            }
            // chatrace config checks
            if (!this.getChatraceConfig().isSet("chatrace")) {
                throw new RaceException("The chatrace section is missing in the config!");
            } else if (!this.getChatraceConfig().isSet("chatrace.enabled")) {
                throw new RaceException("The enabled setting of chat race is missing in the config!");
            } else if (!this.getChatraceConfig().isSet("chatrace.duration")) {
                throw new RaceException("The duration of chat race is missing in the config!");
            } else if (this.getChatraceConfig().getLong("chatrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of chat race is higher than the event delay!");
            } else if (!this.getChatraceConfig().isSet("chatrace.chance")) {
                throw new RaceException("The chat race chance is not set!");
            } else if (!this.getChatraceConfig().isSet("chatrace.words")) {
                throw new RaceException("The words for the chatrace are missing in the config!");
            } else if (!this.getChatraceConfig().isSet("chatrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the chatrace in the config!");
            } else if (!this.getChatraceConfig().isSet("chatrace.rewards")) {
                throw new RaceException("The rewards for chatrace are missing in the config!");
            } else if (!this.getChatraceConfig().isSet("chatrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for chatrace rewards are missing in the config!");
            } else if (!this.getChatraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the chatrace is missing in the config!");
            }

            // blockrace config checks
            if (!this.getBlockraceConfig().isSet("blockrace")) {
                throw new RaceException("The blockrace section is missing in the config!");
            } else if (!this.getBlockraceConfig().isSet("blockrace.enabled")) {
                throw new RaceException("The enabled setting of block race is missing in the config!");
            } else if (!this.getBlockraceConfig().isSet("blockrace.duration")) {
                throw new RaceException("The duration of block race is missing in the config!");
            } else if (this.getBlockraceConfig().getLong("blockrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of block race is higher than the event delay!");
            } else if (!this.getBlockraceConfig().isSet("blockrace.chance")) {
                throw new RaceException("The block race chance is not set!");
            } else if (!this.getBlockraceConfig().isSet("blockrace.blocks")) {
                throw new RaceException("The blocks for the blockrace are missing in the config!");
            } else if (!this.getBlockraceConfig().isSet("blockrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the blockrace in the config!");
            } else if (!this.getBlockraceConfig().isSet("blockrace.rewards")) {
                throw new RaceException("The rewards for blockrace are missing in the config!");
            } else if (!this.getBlockraceConfig().isSet("blockrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for blockrace rewards are missing in the config!");
            } else if (!this.getBlockraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the blockrace is missing in the config!");
            }

            // fishrace config checks
            if (!this.getFishraceConfig().isSet("fishrace")) {
                throw new RaceException("The fishrace section is missing in the config!");
            } else if (!this.getFishraceConfig().isSet("fishrace.enabled")) {
                throw new RaceException("The enabled setting of fish race is missing in the config!");
            } else if (!this.getFishraceConfig().isSet("fishrace.duration")) {
                throw new RaceException("The duration of fish race is missing in the config!");
            } else if (this.getFishraceConfig().getLong("fishrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of fish race is higher than the event delay!");
            } else if (!this.getFishraceConfig().isSet("fishrace.chance")) {
                throw new RaceException("The fish race chance is not set!");
            } else if (!this.getFishraceConfig().isSet("fishrace.fish")) {
                throw new RaceException("The fish for the fishrace are missing in the config!");
            } else if (!this.getFishraceConfig().isSet("fishrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the fishrace in the config!");
            } else if (!this.getFishraceConfig().isSet("fishrace.rewards")) {
                throw new RaceException("The rewards for fishrace are missing in the config!");
            } else if (!this.getFishraceConfig().isSet("fishrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for fishrace rewards are missing in the config!");
            } else if (!this.getFishraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the fishrace is missing in the config!");
            }

            // huntrace config checks
            if (!this.getHuntraceConfig().isSet("huntrace")) {
                throw new RaceException("The huntrace section is missing in the config!");
            } else if (!this.getHuntraceConfig().isSet("huntrace.enabled")) {
                throw new RaceException("The enabled setting of hunt race is missing in the config!");
            } else if (!this.getHuntraceConfig().isSet("huntrace.duration")) {
                throw new RaceException("The duration of hunt race is missing in the config!");
            } else if (this.getHuntraceConfig().getLong("huntrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of hunt race is higher than the event delay!");
            } else if (!this.getHuntraceConfig().isSet("huntrace.chance")) {
                throw new RaceException("The hunt race chance is not set!");
            } else if (!this.getHuntraceConfig().isSet("huntrace.mobs")) {
                throw new RaceException("The mobs for the huntrace are missing in the config!");
            } else if (!this.getHuntraceConfig().isSet("huntrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the huntrace in the config!");
            } else if (!this.getHuntraceConfig().isSet("huntrace.rewards")) {
                throw new RaceException("The rewards for huntrace are missing in the config!");
            } else if (!this.getHuntraceConfig().isSet("huntrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for huntrace rewards are missing in the config!");
            } else if (!this.getHuntraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the huntrace is missing in the config!");
            }

            // craftrace config checks
            if (!this.getCraftraceConfig().isSet("craftrace")) {
                throw new RaceException("The craftrace section is missing in the config!");
            } else if (!this.getCraftraceConfig().isSet("craftrace.enabled")) {
                throw new RaceException("The enabled setting of craft race is missing in the config!");
            } else if (!this.getCraftraceConfig().isSet("craftrace.duration")) {
                throw new RaceException("The duration of craft race is missing in the config!");
            } else if (this.getCraftraceConfig().getLong("craftrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of craft race is higher than the event delay!");
            } else if (!this.getCraftraceConfig().isSet("craftrace.chance")) {
                throw new RaceException("The craft race chance is not set!");
            } else if (!this.getCraftraceConfig().isSet("craftrace.items")) {
                throw new RaceException("The items for the craftrace are missing in the config!");
            } else if (!this.getCraftraceConfig().isSet("craftrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the craftrace in the config!");
            } else if (!this.getCraftraceConfig().isSet("craftrace.rewards")) {
                throw new RaceException("The rewards for craftrace are missing in the config!");
            } else if (!this.getCraftraceConfig().isSet("craftrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for craftrace rewards are missing in the config!");
            } else if (!this.getCraftraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the craftrace is missing in the config!");
            }

            // quizrace config checks
            if (!this.getQuizraceConfig().isSet("quizrace")) {
                throw new RaceException("The quizrace section is missing in the config!");
            } else if (!this.getQuizraceConfig().isSet("quizrace.enabled")) {
                throw new RaceException("The enabled setting of quiz race is missing in the config!");
            } else if (!this.getQuizraceConfig().isSet("quizrace.duration")) {
                throw new RaceException("The duration of quiz race is missing in the config!");
            } else if (this.getQuizraceConfig().getLong("quizrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of quiz race is higher than the event delay!");
            } else if (!this.getQuizraceConfig().isSet("quizrace.chance")) {
                throw new RaceException("The quiz race chance is not set!");
            } else if (!this.getQuizraceConfig().isSet("quizrace.questions")) {
                throw new RaceException("The questions for the quizrace are missing in the config!");
            } else if (!this.getQuizraceConfig().isSet("quizrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the quizrace in the config!");
            } else if (!this.getQuizraceConfig().isSet("quizrace.rewards")) {
                throw new RaceException("The rewards for quizrace are missing in the config!");
            } else if (!this.getQuizraceConfig().isSet("quizrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for quizrace rewards are missing in the config!");
            } else if (!this.getQuizraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the quizrace is missing in the config!");
            }

            // foodrace config checks
            if (!this.getFoodraceConfig().isSet("foodrace")) {
                throw new RaceException("The foodrace section is missing in the config!");
            } else if (!this.getFoodraceConfig().isSet("foodrace.enabled")) {
                throw new RaceException("The enabled setting of food race is missing in the config!");
            } else if (!this.getFoodraceConfig().isSet("foodrace.duration")) {
                throw new RaceException("The duration of food race is missing in the config!");
            } else if (this.getFoodraceConfig().getLong("foodrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of food race is higher than the event delay!");
            } else if (!this.getFoodraceConfig().isSet("foodrace.chance")) {
                throw new RaceException("The food race chance is not set!");
            } else if (!this.getFoodraceConfig().isSet("foodrace.food")) {
                throw new RaceException("The food for the foodrace is missing in the config!");
            } else if (!this.getFoodraceConfig().isSet("foodrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the foodrace in the config!");
            } else if (!this.getFoodraceConfig().isSet("foodrace.rewards")) {
                throw new RaceException("The rewards for foodrace are missing in the config!");
            } else if (!this.getFoodraceConfig().isSet("foodrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for foodrace rewards are missing in the config!");
            } else if (!this.getFoodraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the foodrace is missing in the config!");
            }

            // scramblerace config checks
            if (!this.getScrambleraceConfig().isSet("scramblerace")) {
                throw new RaceException("The scramblerace section is missing in the config!");
            } else if (!this.getScrambleraceConfig().isSet("scramblerace.enabled")) {
                throw new RaceException("The enabled setting of scramble race is missing in the config!");
            } else if (!this.getScrambleraceConfig().isSet("scramblerace.duration")) {
                throw new RaceException("The duration of scramble race is missing in the config!");
            } else if (this.getScrambleraceConfig().getLong("scramblerace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of scramble race is higher than the event delay!");
            } else if (!this.getScrambleraceConfig().isSet("scramblerace.chance")) {
                throw new RaceException("The scramble race chance is not set!");
            } else if (!this.getScrambleraceConfig().isSet("scramblerace.words")) {
                throw new RaceException("The words for the scramblerace are missing in the config!");
            } else if (!this.getScrambleraceConfig().isSet("scramblerace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the scramblerace in the config!");
            } else if (!this.getScrambleraceConfig().isSet("scramblerace.rewards")) {
                throw new RaceException("The rewards for scramblerace are missing in the config!");
            } else if (!this.getScrambleraceConfig().isSet("scramblerace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for scramblerace rewards are missing in the config!");
            } else if (!this.getScrambleraceConfig().isSet("language")) {
                throw new RaceException(
                        "The language section for the scramblerace is missing in the config!");
            }
        } catch (RaceException e) {
            RaceException.handleConfigException(this, e);
            return false;
        }
        return true;
    }

    public void init() {
        this.chatrace = new ChatRace(this, chatraceConfig);
        this.blockRace = new BlockRace(this, blockraceConfig);
        this.fishRace = new FishRace(this, fishraceConfig);
        this.huntRace = new HuntRace(this, huntraceConfig);
        this.craftRace = new CraftRace(this, craftraceConfig);
        this.quizRace = new QuizRace(this, quizraceConfig);
        this.foodRace = new FoodRace(this, foodraceConfig);
        this.scrambleRace = new ScrambleRace(this, scrambleraceConfig);
        this.languageManager = new LanguageManager(this);
        this.printer = new Printer(this);
        this.raceRandomizer = new RaceRandomizer(this);
        this.raceCreator = new RaceCreator(this);
        setupListeners();
        setupCommands();
    }

    private void setupCommands() {
        ChatBrawlCommand chatBrawlCommand = new ChatBrawlCommand(this);
        Objects.requireNonNull(this.getCommand("cb")).setExecutor(chatBrawlCommand);
        Objects.requireNonNull(this.getCommand("cb")).setTabCompleter(chatBrawlCommand);
    }

    private void setupListeners() {
        Stream.of(
                new GeneralListener(this),
                new ChatRaceListener(this),
                new BlockRaceListener(this),
                new FishRaceListener(this),
                new HuntRaceListener(this),
                new CraftRaceListener(this),
                new QuizRaceListener(this),
                new FoodRaceListener(this),
                new ScrambleRaceListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public void setupFiles() {
        saveDefaultConfig();
        this.fileManager = new FileManager(this);
        languageConfig = fileManager.loadFile("language/language.yml", true);
        chatraceConfig = fileManager.loadFile("races/chatrace.yml", true);
        blockraceConfig = fileManager.loadFile("races/blockrace.yml", true);
        fishraceConfig = fileManager.loadFile("races/fishrace.yml", true);
        huntraceConfig = fileManager.loadFile("races/huntrace.yml", true);
        craftraceConfig = fileManager.loadFile("races/craftrace.yml", true);
        quizraceConfig = fileManager.loadFile("races/quizrace.yml", true);
        foodraceConfig = fileManager.loadFile("races/foodrace.yml", true);
        scrambleraceConfig = fileManager.loadFile("races/scramblerace.yml", true);
    }

    public List<String> getDisabledWorldsList() {
        return disabledWorldsList;
    }

    public boolean getAllowCreative() {
        return allowCreative;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public Sound getBeginSound() {
        return beginSound;
    }

    public Sound getEndSound() {
        return endSound;
    }

    public boolean isEnabledActionbar() {
        return enableActionbar;
    }

    public ChatRace getChatrace() {
        return chatrace;
    }

    public BlockRace getBlockRace() {
        return blockRace;
    }

    public FishRace getFishRace() {
        return fishRace;
    }

    public HuntRace getHuntRace() {
        return huntRace;
    }

    public CraftRace getCraftRace() {
        return craftRace;
    }

    public QuizRace getQuizRace() {
        return quizRace;
    }

    public FoodRace getFoodRace() {
        return foodRace;
    }

    public ScrambleRace getScrambleRace() {
        return scrambleRace;
    }

    public Printer getPrinter() {
        return printer;
    }

    public RaceCreator getRaceCreator() {
        return raceCreator;
    }

    public RaceRandomizer getRaceRandomizer() {
        return raceRandomizer;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public LanguageManager getLanguageFileReader() {
        return languageManager;
    }

    public FileConfiguration getLanguageConfig() {
        return languageConfig;
    }

    public FileConfiguration getChatraceConfig() {
        return chatraceConfig;
    }

    public FileConfiguration getBlockraceConfig() {
        return blockraceConfig;
    }

    public FileConfiguration getFishraceConfig() {
        return fishraceConfig;
    }

    public FileConfiguration getHuntraceConfig() {
        return huntraceConfig;
    }

    public FileConfiguration getCraftraceConfig() {
        return craftraceConfig;
    }

    public FileConfiguration getQuizraceConfig() {
        return quizraceConfig;
    }

    public FileConfiguration getFoodraceConfig() {
        return foodraceConfig;
    }

    public FileConfiguration getScrambleraceConfig() {
        return scrambleraceConfig;
    }
}
