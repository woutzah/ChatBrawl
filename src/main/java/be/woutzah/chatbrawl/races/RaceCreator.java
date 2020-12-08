package be.woutzah.chatbrawl.races;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.types.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RaceCreator {

    private final ChatBrawl plugin;
    private long eventDelay;
    private int minimumAmountOfPlayers;
    private Boolean racesEnabled;
    private boolean startBroadcastsEnabled;
    private boolean endBroadcastsEnabled;
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
    private BukkitTask actionbarTask;

    public RaceCreator(ChatBrawl plugin) {
        this.plugin = plugin;
        this.eventDelay = plugin.getConfig().getLong("event-delay") * 20;
        this.racesEnabled = plugin.getConfig().getBoolean("events-enabled");
        this.startBroadcastsEnabled = plugin.getConfig().getBoolean("enable-start-race-broadcasts");
        this.endBroadcastsEnabled = plugin.getConfig().getBoolean("enable-end-race-broadcasts");
        this.minimumAmountOfPlayers = plugin.getConfig().getInt("minimum-players");
        this.chatRace = plugin.getChatrace();
        this.blockRace = plugin.getBlockRace();
        this.fishRace = plugin.getFishRace();
        this.huntRace = plugin.getHuntRace();
        this.craftRace = plugin.getCraftRace();
        this.quizRace = plugin.getQuizRace();
        this.foodRace = plugin.getFoodRace();
        this.scrambleRace = plugin.getScrambleRace();
        this.currentRunningRace = RaceType.NONE;
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
                        while (!isEnabled) {
                            RaceType raceType;
                            raceType = plugin.getRaceRandomizer().getRandomRace();
                            //raceType = RaceType.chat;
                            switch (raceType) {
                                case CHAT:
                                    if (chatRace.isEnabled()) {
                                        isEnabled = true;
                                        chatRaceStart();
                                    }
                                    break;
                                case BLOCK:
                                    if (blockRace.isEnabled()) {
                                        isEnabled = true;
                                        blockRaceStart();
                                    }
                                    break;
                                case FISH:
                                    if (fishRace.isEnabled()) {
                                        isEnabled = true;
                                        fishRaceStart();
                                    }
                                    break;
                                case HUNT:
                                    if (huntRace.isEnabled()) {
                                        isEnabled = true;
                                        huntRaceStart();
                                    }
                                    break;
                                case CRAFT:
                                    if (craftRace.isEnabled()) {
                                        isEnabled = true;
                                        craftRaceStart();
                                    }
                                    break;
                                case QUIZ:
                                    if (quizRace.isEnabled()) {
                                        isEnabled = true;
                                        quizRaceStart();
                                    }
                                    break;
                                case FOOD:
                                    if (foodRace.isEnabled()) {
                                        isEnabled = true;
                                        foodRaceStart();
                                    }
                                    break;
                                case SCRAMBLE:
                                    if (scrambleRace.isEnabled()) {
                                        isEnabled = true;
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

    public void showActionBar(String message) {
        if (plugin.isEnabledActionbar()) {
            actionbarTask = new BukkitRunnable() {
                @Override
                public void run() {
                        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message)));
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    public void stopActionBar(){
        if (plugin.isEnabledActionbar()) {
            actionbarTask.cancel();
        }
    }

    public void announceStart(String message) {
        if (startBroadcastsEnabled) {
            Bukkit.broadcast(message, "cb.default");
        }
    }

    public void chatRaceStart() {
        setCurrentRunningRace(RaceType.CHAT);
        chatRace.generateRandomWord();
        announceStart(printer.getAnnounceChatStart(chatRace.getWordToGuess()));
        showActionBar(printer.getActionBarChatStart(chatRace.getWordToGuess()));
        playBeginSound();
        chatRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceChatEnd(), "cb.default");
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, chatRace.getDuration());
    }

    public void blockRaceStart() {
        setCurrentRunningRace(RaceType.BLOCK);
        blockRace.generateNewMaterialPair();
        blockRace.fillOnlinePlayers();
        announceStart(printer.getAnnounceBlockStart(blockRace.getCurrentItemStack()));
        showActionBar(printer.getActionBarBlockStart(blockRace.getCurrentItemStack()));
        playBeginSound();
        blockRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceBlockEnd(), "cb.default");
                        blockRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, blockRace.getDuration());
    }

    public void fishRaceStart() {
        setCurrentRunningRace(RaceType.FISH);
        fishRace.generateNewMaterialPair();
        fishRace.fillOnlinePlayers();
        announceStart(printer.getAnnounceFishStart(fishRace.getCurrentItemStack()));
        showActionBar(printer.getActionBarFishStart(fishRace.getCurrentItemStack()));
        playBeginSound();
        fishRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceFishEnd(), "cb.default");
                        fishRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, fishRace.getDuration());
    }

    public void huntRaceStart() {
        setCurrentRunningRace(RaceType.HUNT);
        huntRace.generateNewMobPair();
        huntRace.fillOnlinePlayers();
        announceStart(printer.getAnnounceHuntStart(huntRace.getCurrentEntityType(), huntRace.getCurrentAmount()));
        showActionBar(printer.getActionBarHuntStart(huntRace.getCurrentEntityType(),
                huntRace.getCurrentAmount()));
        playBeginSound();
        huntRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceHuntEnd(), "cb.default");
                        huntRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, huntRace.getDuration());
    }

    public void craftRaceStart() {
        setCurrentRunningRace(RaceType.CRAFT);
        craftRace.generateNewMaterialPair();
        craftRace.fillOnlinePlayers();
        announceStart(printer.getAnnounceCraftStart(craftRace.getCurrentItemStack()));
        showActionBar(printer.getActionBarCraftStart(craftRace.getCurrentItemStack()));
        playBeginSound();
        craftRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceCraftEnd(), "cb.default");
                        craftRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, craftRace.getDuration());
    }

    public void quizRaceStart() {
        setCurrentRunningRace(RaceType.QUIZ);
        quizRace.generateRandomQuestionWithAnswer();
        announceStart(printer.getAnnounceQuizStart(quizRace.getCurrentQuestion()));
        showActionBar(printer.getActionBarQuizStart(quizRace.getCurrentQuestion()));
        playBeginSound();
        quizRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceQuizEnd(), "cb.default");
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, quizRace.getDuration());
    }

    public void foodRaceStart() {
        setCurrentRunningRace(RaceType.FOOD);
        foodRace.generateNewFoodPair();
        foodRace.fillOnlinePlayers();
        announceStart(printer.getAnnounceFoodStart(foodRace.getCurrentItemStack()));
        showActionBar(printer.getActionBarFoodStart(foodRace.getCurrentItemStack()));
        playBeginSound();
        foodRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceFoodEnd(), "cb.default");
                        foodRace.removeOnlinePlayers();
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, foodRace.getDuration());
    }

    public void scrambleRaceStart() {
        setCurrentRunningRace(RaceType.SCRAMBLE);
        scrambleRace.generateRandomScrambledWord();
        announceStart(printer.getAnnounceScrambleStart(scrambleRace.getWordToUnscramble()));
        showActionBar(printer.getActionBarScrambleStart(scrambleRace.getWordToUnscramble()));
        playBeginSound();
        scrambleRaceTask =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(printer.getAnnounceScrambleEnd(scrambleRace.getOriginalWord()), "cb.default");
                        setCurrentRunningRace(RaceType.NONE);
                        playEndSound();
                        stopActionBar();
                    }
                }.runTaskLater(plugin, scrambleRace.getDuration());
    }

    private void playEndSound() {
        if (plugin.isSoundEnabled()) {
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), plugin.getEndSound(), 1.0F, 8.0F));
        }
    }

    private void playBeginSound(){
        if (plugin.isSoundEnabled()) {
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), plugin.getBeginSound(), 1.0F, 8.0F));
        }
    }


    public BukkitTask getActionbarTask() {
        return actionbarTask;
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

    public boolean isEndBroadcastsEnabled() {
        return endBroadcastsEnabled;
    }
}
