package be.woutzah.chatbrawl;

import be.woutzah.chatbrawl.commands.ChatBrawlCommand;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.files.FileManager;
import be.woutzah.chatbrawl.listeners.*;
import be.woutzah.chatbrawl.messages.LanguageFileReader;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.placeholders.Placeholders;
import be.woutzah.chatbrawl.races.*;
import be.woutzah.chatbrawl.utils.RaceRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ChatBrawl extends JavaPlugin {

    public static boolean langUtilsIsEnabled = false;
    private ChatRace chatrace;
    private BlockRace blockRace;
    private FishRace fishRace;
    private HuntRace huntRace;
    private CraftRace craftRace;
    private QuizRace quizRace;
    private FoodRace foodRace;
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
    private LanguageFileReader languageFileReader;
    private RaceRandomizer raceRandomizer;

    @Override
    public void onEnable() {
        setupFiles();
        boolean isConfigCorrect = configChecker();
        init();
        printer.printConsoleMessage();
        this.getServer().getPluginManager().registerEvents(new GeneralListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatRaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockRaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new FishRaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new HuntRaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CraftRaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new QuizRaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new FoodRaceListener(this), this);
        Objects.requireNonNull(this.getCommand("cb")).setExecutor(new ChatBrawlCommand(this));
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }
        if (Bukkit.getPluginManager().getPlugin("LangUtils") != null) {
            langUtilsIsEnabled = true;
        }
        if (isConfigCorrect) {
            raceCreator.createRaces();
        }
    }


    @Override
    public void onDisable() {
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
            }

        } catch (RaceException e) {
            RaceException.handleConfigException(this, e);
            return false;
        }
        return true;
    }

    public void init() {
        this.languageFileReader = new LanguageFileReader(this);
        this.printer = new Printer(this);
        this.chatrace = new ChatRace(this);
        this.blockRace = new BlockRace(this);
        this.fishRace = new FishRace(this);
        this.huntRace = new HuntRace(this);
        this.craftRace = new CraftRace(this);
        this.quizRace = new QuizRace(this);
        this.foodRace = new FoodRace(this);
        this.raceCreator = new RaceCreator(this);
        this.raceRandomizer = new RaceRandomizer(this);
    }

    public void setupFiles(){
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

    public LanguageFileReader getLanguageFileReader() {
        return languageFileReader;
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
}
