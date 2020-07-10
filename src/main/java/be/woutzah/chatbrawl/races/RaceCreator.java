package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.types.*;
import org.bukkit.Bukkit;
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
    private ScrambleRace scrambleRace;
    private BukkitTask raceCreationTask;
    private BukkitTask chatRaceTask;
    private BukkitTask blockRaceTask;
    private BukkitTask fishRaceTask;
    private BukkitTask huntRaceTask;
    private BukkitTask craftRaceTask;
    private BukkitTask quizRaceTask;
    private BukkitTask foodRaceTask;
    private BukkitTask scrambleRaceTask;
    private RaceType currentRunningRace;
    private Printer printer;

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
        this.scrambleRace = plugin.getScrambleRace();
        this.currentRunningRace = RaceType.none;
        this.printer = plugin.getPrinter();
        if (plugin.isConfigCorrect) createRaces();
    }

    public void createRaces() {
        Bukkit.broadcast(printer.getStartedCreating(), "cb.default");
        raceCreationTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!racesEnabled || RaceException.isBadConfig()) {
                    cancel();
                } else {
                    if ((plugin.getServer().getOnlinePlayers().size() >= minimumAmountOfPlayers)) {
                        boolean isEnabled = false;
                        RaceType raceType;
                        while (!isEnabled) {
                            raceType = plugin.getRaceRandomizer().getRandomRace();
                            //raceType = RaceType.chat;
                            if (plugin.isSoundEnabled()){
                                Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getBeginSound(),1.0F, 8.0F) );
                            }
                            switch (raceType) {
                                case chat:
                                    if (chatRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.chat);
                                        chatRaceStart();
                                    }
                                    break;
                                case block:
                                    if (blockRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.block);
                                        blockRaceStart();
                                    }
                                    break;
                                case fish:
                                    if (fishRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.fish);
                                        fishRaceStart();
                                    }
                                    break;
                                case hunt:
                                    if (huntRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.hunt);
                                        huntRaceStart();
                                    }
                                    break;
                                case craft:
                                    if (craftRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.craft);
                                        craftRaceStart();
                                    }
                                    break;
                                case quiz:
                                    if (quizRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.quiz);
                                        quizRaceStart();
                                    }
                                    break;
                                case food:
                                    if (foodRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.food);
                                        foodRaceStart();
                                    }
                                    break;
                                case scramble:
                                    if (scrambleRace.isEnabled()) {
                                        isEnabled = true;
                                        setCurrentRunningRace(RaceType.scramble);
                                        scrambleRaceStart();
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 200, eventDelay);
    }

    public void chatRaceStart() {
        chatRace.generateRandomWord();
        Bukkit.broadcast(printer.getAnnounceChatStart(chatRace.getWordToGuess()), "cb.default");
        chatRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceChatEnd(), "cb.default");
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, chatRace.getDuration());
    }

    public void blockRaceStart() {
        blockRace.generateNewMaterialPair();
        blockRace.fillOnlinePlayers();
        Bukkit.broadcast(printer.getAnnounceBlockStart(blockRace.getCurrentItemStack()), "cb.default");
        blockRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceBlockEnd(), "cb.default");
                        blockRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, blockRace.getDuration());
    }

    private void fishRaceStart() {
        fishRace.generateNewMaterialPair();
        fishRace.fillOnlinePlayers();
        Bukkit.broadcast(printer.getAnnounceFishStart(fishRace.getCurrentItemStack()), "cb.default");
        fishRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceFishEnd(), "cb.default");
                        fishRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, fishRace.getDuration());
    }

    private void huntRaceStart() {
        huntRace.generateNewMobPair();
        huntRace.fillOnlinePlayers();
        Bukkit.broadcast(printer.getAnnounceHuntStart(huntRace.getCurrentEntityType(),
                huntRace.getCurrentAmount()), "cb.default");
        huntRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceHuntEnd(), "cb.default");
                        huntRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, huntRace.getDuration());
    }

    public void craftRaceStart() {
        craftRace.generateNewMaterialPair();
        craftRace.fillOnlinePlayers();
        Bukkit.broadcast(printer.getAnnounceCraftStart(craftRace.getCurrentItemStack()), "cb.default");
        craftRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceCraftEnd(), "cb.default");
                        craftRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, craftRace.getDuration());
    }

    public void quizRaceStart() {
        quizRace.generateRandomQuestionWithAnswer();
        Bukkit.broadcast(printer.getAnnounceQuizStart(quizRace.getCurrentQuestion()), "cb.default");
        quizRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceQuizEnd(), "cb.default");
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, quizRace.getDuration());
    }

    public void foodRaceStart() {
        foodRace.generateNewFoodPair();
        foodRace.fillOnlinePlayers();
        Bukkit.broadcast(printer.getAnnounceFoodStart(foodRace.getCurrentItemStack()), "cb.default");
        foodRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceFoodEnd(),"cb.default");
                        foodRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, foodRace.getDuration());
    }

    public void scrambleRaceStart() {
        scrambleRace.generateRandomScrambledWord();
        Bukkit.broadcast(printer.getAnnounceScrambleStart(scrambleRace.getWordToUnscramble()), "cb.default");
        scrambleRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceScrambleEnd(), "cb.default");
                        setCurrentRunningRace(RaceType.none);
                        playEndSound();
                    }
                }.runTaskLater(plugin, scrambleRace.getDuration());
    }

    public void playEndSound(){
        if (plugin.isSoundEnabled()){
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
        }
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

    public BukkitTask getScrambleRaceTask() {
        return scrambleRaceTask;
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
