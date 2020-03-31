package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RaceCreator {

    private ChatBrawl plugin;
    private long eventDelay;
    private int minimumAmountOfPlayers;
    private Boolean racesEnabled;
    private ChatRace chatRace;
    private BlockRace blockRace;
    private FishRace fishRace;
    private HuntRace huntRace;
    private CraftRace craftRace;
    private QuizRace quizRace;
    private FoodRace foodRace;
    private BukkitTask raceCreationTask;
    private BukkitTask chatRaceTask;
    private BukkitTask blockRaceTask;
    private BukkitTask fishRaceTask;
    private BukkitTask huntRaceTask;
    private BukkitTask craftRaceTask;
    private BukkitTask quizRaceTask;
    private BukkitTask foodRaceTask;
    private RaceType currentRunningRace;

    public RaceCreator(ChatBrawl plugin) {
        this.plugin = plugin;
        this.eventDelay = plugin.getConfig().getLong("event-delay") * 20;
        this.racesEnabled = plugin.getConfig().getBoolean("events-enabled");
        this.minimumAmountOfPlayers = plugin.getConfig().getInt("minimum-players");
        this.chatRace = plugin.getChatrace();
        this.blockRace = plugin.getBlockRace();
        this.fishRace = plugin.getFishRace();
        this.huntRace = plugin.getHuntRace();
        this.craftRace = plugin.getCraftRace();
        this.quizRace = plugin.getQuizRace();
        this.foodRace = plugin.getFoodRace();
        this.currentRunningRace = RaceType.none;
    }

    public void createRaces() {
        plugin
                .getServer()
                .broadcastMessage(plugin.getPrinter().getStartedCreating());
        raceCreationTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!racesEnabled || RaceException.isBadConfig()) {
                    cancel();
                } else {
                    if (!(plugin.getServer().getOnlinePlayers().size() < minimumAmountOfPlayers)) {
                        switch (plugin.getRaceRandomizer().getRandomRace()) {
                        //switch (RaceType.quiz) {
                            case chat:
                                chatRaceStart();
                                setCurrentRunningRace(RaceType.chat);
                                break;
                            case block:
                                blockRaceStart();
                                setCurrentRunningRace(RaceType.block);
                                break;
                            case fish:
                                fishRaceStart();
                                setCurrentRunningRace(RaceType.fish);
                                break;
                            case hunt:
                                huntRaceStart();
                                setCurrentRunningRace(RaceType.hunt);
                                break;
                            case craft:
                                craftRaceStart();
                                setCurrentRunningRace(RaceType.craft);
                                break;
                            case quiz:
                                quizRaceStart();
                                setCurrentRunningRace(RaceType.quiz);
                                break;
                            case food:
                                foodRaceStart();
                                setCurrentRunningRace(RaceType.food);
                                break;
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 200, eventDelay);
    }

    public void chatRaceStart() {
        chatRace.getRandomWordFromConfig();
        plugin
                .getServer()
                .broadcastMessage(
                        plugin.getPrinter().getAnnounceChatStart(chatRace.getWordToGuess()));
        chatRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin
                                .getServer()
                                .broadcastMessage(plugin.getPrinter().getAnnounceChatEnd());
                        setCurrentRunningRace(RaceType.none);
                    }
                }.runTaskLater(plugin, chatRace.getDuration());
    }

    public void blockRaceStart() {
        blockRace.generateNewMaterialPair();
        blockRace.fillOnlinePlayers();
        plugin
                .getServer()
                .broadcastMessage(
                        plugin
                                .getPrinter()
                                .getAnnounceBlockStart(blockRace.getCurrentItemStack()));
        blockRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin
                                .getServer()
                                .broadcastMessage(plugin.getPrinter().getAnnounceBlockEnd());
                        blockRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                    }
                }.runTaskLater(plugin, blockRace.getDuration());
    }

    private void fishRaceStart() {
        fishRace.generateNewMaterialPair();
        fishRace.fillOnlinePlayers();
        plugin
                .getServer()
                .broadcastMessage(
                        plugin
                                .getPrinter()
                                .getAnnounceFishStart(fishRace.getCurrentItemStack()));
        fishRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin
                                .getServer()
                                .broadcastMessage(plugin.getPrinter().getAnnounceFishEnd());
                        fishRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                    }
                }.runTaskLater(plugin, fishRace.getDuration());
    }

    private void huntRaceStart() {
        huntRace.generateNewMobPair();
        huntRace.fillOnlinePlayers();
        plugin
                .getServer()
                .broadcastMessage(
                        plugin
                                .getPrinter()
                                .getAnnounceHuntStart(huntRace.getCurrentEntityType(), huntRace.getCurrentAmount()));
        huntRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin
                                .getServer()
                                .broadcastMessage(plugin.getPrinter().getAnnounceHuntEnd());
                        huntRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                    }
                }.runTaskLater(plugin, huntRace.getDuration());
    }

    public void craftRaceStart() {
        craftRace.generateNewMaterialPair();
        craftRace.fillOnlinePlayers();
        plugin
                .getServer()
                .broadcastMessage(
                        plugin
                                .getPrinter()
                                .getAnnounceCraftStart(craftRace.getCurrentItemStack()));
        craftRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin
                                .getServer()
                                .broadcastMessage(plugin.getPrinter().getAnnounceCraftEnd());
                        craftRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                    }
                }.runTaskLater(plugin, craftRace.getDuration());
    }

    public void quizRaceStart() {
        quizRace.generateRandomQuestionWithAnswer();
        plugin
                .getServer()
                .broadcastMessage(
                        plugin
                                .getPrinter()
                                .getAnnounceQuizStart(quizRace.getCurrentQuestion()));
        quizRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin
                                .getServer()
                                .broadcastMessage(plugin.getPrinter().getAnnounceQuizEnd());
                        setCurrentRunningRace(RaceType.none);
                    }
                }.runTaskLater(plugin, quizRace.getDuration());
    }

    public void foodRaceStart() {
        foodRace.generateNewFoodPair();
        foodRace.fillOnlinePlayers();
        plugin
                .getServer()
                .broadcastMessage(
                        plugin
                                .getPrinter()
                                .getAnnounceFoodStart(foodRace.getCurrentItemStack()));
        foodRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin
                                .getServer()
                                .broadcastMessage(plugin.getPrinter().getAnnounceFoodEnd());
                        foodRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                    }
                }.runTaskLater(plugin, foodRace.getDuration());
    }

    public BukkitTask getRaceCreationTask() {
        return raceCreationTask;
    }

    public BukkitTask getChatRaceTask() {
        return chatRaceTask;
    }

    public BukkitTask getBlockRaceTask() {
        return blockRaceTask;
    }

    public BukkitTask getFishRaceTask() {
        return fishRaceTask;
    }

    public BukkitTask getHuntRaceTask() {
        return huntRaceTask;
    }

    public BukkitTask getCraftRaceTask() {
        return craftRaceTask;
    }

    public BukkitTask getQuizRaceTask() {
        return quizRaceTask;
    }

    public BukkitTask getFoodRaceTask() {
        return foodRaceTask;
    }

    public RaceType getCurrentRunningRace() {
        return currentRunningRace;
    }

    public void setCurrentRunningRace(RaceType currentRunningRace) {
        this.currentRunningRace = currentRunningRace;
    }

    public Boolean getRacesEnabled() {
        return racesEnabled;
    }

    public void setRacesEnabled(Boolean racesEnabled) {
        this.racesEnabled = racesEnabled;
    }
}
