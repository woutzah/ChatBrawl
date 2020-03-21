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
    private LanguageFileReader languageFileReader;
    private RaceRandomizer raceRandomizer;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.fileManager = new FileManager(this, "language/language.yml");
        fileManager.init();
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
        if (configChecker()) {
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
            if (!this.getConfig().isSet("chatrace")) {
                throw new RaceException("The chatrace section is missing in the config!");
            } else if (!this.getConfig().isSet("chatrace.enabled")) {
                throw new RaceException("The enabled setting of chat race is missing in the config!");
            } else if (!this.getConfig().isSet("chatrace.duration")) {
                throw new RaceException("The duration of chat race is missing in the config!");
            } else if (this.getConfig().getLong("chatrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of chat race is higher than the event delay!");
            } else if (!this.getConfig().isSet("chatrace.chance")) {
                throw new RaceException("The chat race chance is not set!");
            } else if (!this.getConfig().isSet("chatrace.words")) {
                throw new RaceException("The words for the chatrace are missing in the config!");
            } else if (!this.getConfig().isSet("chatrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the chatrace in the config!");
            } else if (!this.getConfig().isSet("chatrace.rewards")) {
                throw new RaceException("The rewards for chatrace are missing in the config!");
            } else if (!this.getConfig().isSet("chatrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for chatrace rewards are missing in the config!");
            }

            // blockrace config checks
            if (!this.getConfig().isSet("blockrace")) {
                throw new RaceException("The blockrace section is missing in the config!");
            } else if (!this.getConfig().isSet("blockrace.enabled")) {
                throw new RaceException("The enabled setting of block race is missing in the config!");
            } else if (!this.getConfig().isSet("blockrace.duration")) {
                throw new RaceException("The duration of block race is missing in the config!");
            } else if (this.getConfig().getLong("blockrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of block race is higher than the event delay!");
            } else if (!this.getConfig().isSet("blockrace.chance")) {
                throw new RaceException("The block race chance is not set!");
            } else if (!this.getConfig().isSet("blockrace.blocks")) {
                throw new RaceException("The blocks for the blockrace are missing in the config!");
            } else if (!this.getConfig().isSet("blockrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the blockrace in the config!");
            } else if (!this.getConfig().isSet("blockrace.rewards")) {
                throw new RaceException("The rewards for blockrace are missing in the config!");
            } else if (!this.getConfig().isSet("blockrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for blockrace rewards are missing in the config!");
            }

            // fishrace config checks
            if (!this.getConfig().isSet("fishrace")) {
                throw new RaceException("The fishrace section is missing in the config!");
            } else if (!this.getConfig().isSet("fishrace.enabled")) {
                throw new RaceException("The enabled setting of fish race is missing in the config!");
            } else if (!this.getConfig().isSet("fishrace.duration")) {
                throw new RaceException("The duration of fish race is missing in the config!");
            } else if (this.getConfig().getLong("fishrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of fish race is higher than the event delay!");
            } else if (!this.getConfig().isSet("fishrace.chance")) {
                throw new RaceException("The fish race chance is not set!");
            } else if (!this.getConfig().isSet("fishrace.fish")) {
                throw new RaceException("The fish for the fishrace are missing in the config!");
            } else if (!this.getConfig().isSet("fishrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the fishrace in the config!");
            } else if (!this.getConfig().isSet("fishrace.rewards")) {
                throw new RaceException("The rewards for fishrace are missing in the config!");
            } else if (!this.getConfig().isSet("fishrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for fishrace rewards are missing in the config!");
            }

            // huntrace config checks
            if (!this.getConfig().isSet("huntrace")) {
                throw new RaceException("The huntrace section is missing in the config!");
            } else if (!this.getConfig().isSet("huntrace.enabled")) {
                throw new RaceException("The enabled setting of hunt race is missing in the config!");
            } else if (!this.getConfig().isSet("huntrace.duration")) {
                throw new RaceException("The duration of hunt race is missing in the config!");
            } else if (this.getConfig().getLong("huntrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of hunt race is higher than the event delay!");
            } else if (!this.getConfig().isSet("huntrace.chance")) {
                throw new RaceException("The hunt race chance is not set!");
            } else if (!this.getConfig().isSet("huntrace.mobs")) {
                throw new RaceException("The mobs for the huntrace are missing in the config!");
            } else if (!this.getConfig().isSet("huntrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the huntrace in the config!");
            } else if (!this.getConfig().isSet("huntrace.rewards")) {
                throw new RaceException("The rewards for huntrace are missing in the config!");
            } else if (!this.getConfig().isSet("huntrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for huntrace rewards are missing in the config!");
            }

            // craftrace config checks
            if (!this.getConfig().isSet("craftrace")) {
                throw new RaceException("The craftrace section is missing in the config!");
            } else if (!this.getConfig().isSet("craftrace.enabled")) {
                throw new RaceException("The enabled setting of craft race is missing in the config!");
            } else if (!this.getConfig().isSet("craftrace.duration")) {
                throw new RaceException("The duration of craft race is missing in the config!");
            } else if (this.getConfig().getLong("craftrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of craft race is higher than the event delay!");
            } else if (!this.getConfig().isSet("craftrace.chance")) {
                throw new RaceException("The craft race chance is not set!");
            } else if (!this.getConfig().isSet("craftrace.items")) {
                throw new RaceException("The items for the craftrace are missing in the config!");
            } else if (!this.getConfig().isSet("craftrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the craftrace in the config!");
            } else if (!this.getConfig().isSet("craftrace.rewards")) {
                throw new RaceException("The rewards for craftrace are missing in the config!");
            } else if (!this.getConfig().isSet("craftrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for craftrace rewards are missing in the config!");
            }
            
            // quizrace config checks
            if (!this.getConfig().isSet("quizrace")) {
                throw new RaceException("The quizrace section is missing in the config!");
            } else if (!this.getConfig().isSet("quizrace.enabled")) {
                throw new RaceException("The enabled setting of quiz race is missing in the config!");
            } else if (!this.getConfig().isSet("quizrace.duration")) {
                throw new RaceException("The duration of quiz race is missing in the config!");
            } else if (this.getConfig().getLong("quizrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of quiz race is higher than the event delay!");
            } else if (!this.getConfig().isSet("quizrace.chance")) {
                throw new RaceException("The quiz race chance is not set!");
            } else if (!this.getConfig().isSet("quizrace.questions")) {
                throw new RaceException("The questions for the quizrace are missing in the config!");
            } else if (!this.getConfig().isSet("quizrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the quizrace in the config!");
            } else if (!this.getConfig().isSet("quizrace.rewards")) {
                throw new RaceException("The rewards for quizrace are missing in the config!");
            } else if (!this.getConfig().isSet("quizrace.rewards.commands")) {
                throw new RaceException(
                        "The commands section for quizrace rewards are missing in the config!");
            }

            // foodrace config checks
            if (!this.getConfig().isSet("foodrace")) {
                throw new RaceException("The foodrace section is missing in the config!");
            } else if (!this.getConfig().isSet("foodrace.enabled")) {
                throw new RaceException("The enabled setting of food race is missing in the config!");
            } else if (!this.getConfig().isSet("foodrace.duration")) {
                throw new RaceException("The duration of food race is missing in the config!");
            } else if (this.getConfig().getLong("foodrace.duration")
                    >= this.getConfig().getLong("event-delay")) {
                throw new RaceException("The duration of food race is higher than the event delay!");
            } else if (!this.getConfig().isSet("foodrace.chance")) {
                throw new RaceException("The food race chance is not set!");
            } else if (!this.getConfig().isSet("foodrace.food")) {
                throw new RaceException("The food for the foodrace is missing in the config!");
            } else if (!this.getConfig().isSet("foodrace.enable-firework")) {
                throw new RaceException(
                        "The enable-firework settings is missing for the foodrace in the config!");
            } else if (!this.getConfig().isSet("foodrace.rewards")) {
                throw new RaceException("The rewards for foodrace are missing in the config!");
            } else if (!this.getConfig().isSet("foodrace.rewards.commands")) {
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


}
