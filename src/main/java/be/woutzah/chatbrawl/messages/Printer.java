package be.woutzah.chatbrawl.messages;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Stream;

public class Printer {

    private final String discordLink;
    private final ChatBrawl plugin;
    private FileConfiguration config;
    private LanguageManager languageManager;
    private ChatRace chatRace;
    private BlockRace blockRace;
    private FishRace fishRace;
    private HuntRace huntRace;
    private CraftRace craftRace;
    private QuizRace quizRace;
    private FoodRace foodRace;
    private ScrambleRace scrambleRace;

    public Printer(ChatBrawl plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        discordLink = "https://discord.gg/TvTUWvG";
        this.languageManager = plugin.getLanguageFileReader();
        this.chatRace = plugin.getChatrace();
        this.blockRace = plugin.getBlockRace();
        this.fishRace = plugin.getFishRace();
        this.huntRace = plugin.getHuntRace();
        this.craftRace = plugin.getCraftRace();
        this.quizRace = plugin.getQuizRace();
        this.foodRace = plugin.getFoodRace();
        this.scrambleRace = plugin.getScrambleRace();
    }

    //General messages
    public String getPrefix() {
        return parseColorCodes(config.getString("plugin-prefix"));
    }

    public String getHelpMenu() {
        return languageManager.getHelpMenu();
    }

    public String getStartedCreating() {
        return getPrefix() + languageManager.getStartedCreating();
    }

    public String getReloadMessage() {
        return parseColorCodes(getPrefix() + "&aChatBrawl &eV" + plugin.getDescription().getVersion() + " &ahas been reloaded!");
    }

    public String getDiscordMessage() {
        return parseColorCodes(
                "&e&l>&7&m---------&e&l[ &6&oChatBrawl Discord &e&l]&7&m---------&e&l<\n" +
                        "      &fFor &esupport/issues&f or &esuggestions           \n" +
                        "           &fjoin our official discord!               \n" +
                        "          &d&o&n" + discordLink + "\n" +
                        "&e&l>&7&m------------------------------------&e&l<\n"
        );
    }

    public void printConsoleMessage() {
        Stream.of(
                "&e&l>&7&m------------&e&l[ &6&oChatBrawl &e&l]&7&m-----------&e&l<",
                "&e&l>                                    &e&l<",
                "&e&l>           &bChatbrawl V" + plugin.getDescription().getVersion() + "         &e&l<",
                "&e&l>             &9&oBy woutzah             &e&l<",
                "&e&l>                                    &e&l<",
                "&e&l>            &eEnabling ...            &e&l<",
                "&e&l>                                    &e&l<",
                "&e&l>&7&m------------------------------------&e&l<"
        ).forEach(line -> Bukkit.getConsoleSender().sendMessage(parseColorCodes(line)));
    }

    //commands messages
    public String getNoPermission() {
        return getPrefix() + languageManager.getNoPermission();
    }

    public String getSubcommandNotExist() {
        return getPrefix() + languageManager.getSubcommandNotExist();
    }

    public String getStopRaceUsage() {
        return getPrefix() + languageManager.getStopRaceUsage();
    }

    public String getStoppedRace(RaceType raceType) {
        return getPrefix() + languageManager.getStoppedRace(raceType);
    }

    public String getStartRaceUsage(){
        return getPrefix() + languageManager.getStartRaceUsage();
    }

    public String getStartedRace(RaceType raceType) {
        return getPrefix() + languageManager.getStartedRace(raceType);
    }

    public String getRaceTypeNotExist(){
        return getPrefix() + languageManager.getRacetypeNotExist();
    }

    public String getRaceStillRunning(){
        return getPrefix() + languageManager.getRaceStillRunning();
    }

    public String getNoRaceRunning() {
        return languageManager.getNoRaceRunning().isEmpty() ? "" : getPrefix() + languageManager.getNoRaceRunning();
    }

    public String getDisabled() {
        return getPrefix() + languageManager.getDisabled();
    }

    public String getAlreadyDisabled() {
        return getPrefix() + languageManager.getAlreadyDisabled();
    }

    public String getEnabled() {
        return getPrefix() + languageManager.getEnabled();
    }

    public String getAlreadyEnabled() {
        return getPrefix() + languageManager.getAlreadyEnabled();
    }


    public String getCurrentRunningRaceInfo() {
        switch (plugin.getRaceCreator().getCurrentRunningRace()) {
            case CHAT:
                return getAnnounceChatStart(chatRace.getWordToGuess());
            case BLOCK:
                return getAnnounceBlockStart(blockRace.getCurrentItemStack());
            case FISH:
                return getAnnounceFishStart(fishRace.getCurrentItemStack());
            case HUNT:
                return getAnnounceHuntStart(huntRace.getCurrentEntityType(), huntRace.getCurrentAmount());
            case CRAFT:
                return getAnnounceCraftStart(craftRace.getCurrentItemStack());
            case QUIZ:
                return getAnnounceQuizStart(quizRace.getCurrentQuestion());
            case FOOD:
                return getAnnounceFoodStart(foodRace.getCurrentItemStack());
            case SCRAMBLE:
                return getAnnounceScrambleStart(scrambleRace.getWordToUnscramble());
            case NONE:
                return getNoRaceRunning();
        }
        return null;
    }

    //chatrace messages
    public String getAnnounceChatStart(String word) {
        return languageManager.getAnnounceChatStart(word);
    }
    public String getActionBarChatStart(String word){
        return languageManager.getActionBarChatStart(word);
    }

    public String getAnnounceChatEnd() {
        return getPrefix() + languageManager.getAnnounceChatEnd();
    }

    public String getAnnounceChatWinner(Player player) {
        return languageManager.getAnnounceChatWinner(player);
    }

    public String getPersonalChatWinner() {
        return languageManager.getPersonalChatWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalChatWinner();
    }

    //blockrace messages
    public String getAnnounceBlockStart(ItemStack itemStack) {
        return languageManager.getAnnounceBlockStart(itemStack);
    }
    public String getActionBarBlockStart(ItemStack itemStack){
        return languageManager.getActionBarBlockStart(itemStack);
    }

    public String getAnnounceBlockEnd() {
        return getPrefix() + languageManager.getAnnounceBlockEnd();
    }

    public String getAnnounceBlockWinner(Player player) {
        return languageManager.getAnnounceBlockWinner(player);
    }

    public String getPersonalBlockWinner() {
        return languageManager.getPersonalBlockWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalBlockWinner();
    }

    //fishrace messages
    public String getAnnounceFishStart(ItemStack itemStack) {
        return languageManager.getAnnounceFishStart(itemStack);
    }
    public String getActionBarFishStart(ItemStack itemStack){
        return languageManager.getActionBarFishStart(itemStack);
    }

    public String getAnnounceFishEnd() {
        return getPrefix() + languageManager.getAnnounceFishEnd();
    }

    public String getAnnounceFishWinner(Player player) {
        return languageManager.getAnnounceFishWinner(player);
    }

    public String getPersonalFishWinner() {
        return languageManager.getPersonalFishWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalFishWinner();
    }

    //huntrace messages
    public String getAnnounceHuntStart(EntityType entityType, int amount) {
        return languageManager.getAnnounceHuntStart(entityType, amount);
    }
    public String getActionBarHuntStart(EntityType entityType, int amount) {
        return languageManager.getActionBarHuntStart(entityType, amount);
    }

    public String getAnnounceHuntEnd() {
        return getPrefix() + languageManager.getAnnounceHuntEnd();
    }

    public String getAnnounceHuntWinner(Player player) {
        return languageManager.getAnnounceHuntWinner(player);
    }

    public String getPersonalHuntWinner() {
        return languageManager.getPersonalHuntWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalHuntWinner();
    }

    //craftrace messages
    public String getAnnounceCraftStart(ItemStack itemStack) {
        return languageManager.getAnnounceCraftStart(itemStack);
    }
    public String getActionBarCraftStart(ItemStack itemStack) {
        return languageManager.getActionBarCraftStart(itemStack);
    }

    public String getAnnounceCraftEnd() {
        return getPrefix() + languageManager.getAnnounceCraftEnd();
    }

    public String getAnnounceCraftWinner(Player player) {
        return languageManager.getAnnounceCraftWinner(player);
    }

    public String getPersonalCraftWinner() {
        return languageManager.getPersonalCraftWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalCraftWinner();
    }

    //quizrace messages
    public String getAnnounceQuizStart(String word) {
        return languageManager.getAnnounceQuizStart(word);
    }
    public String getActionBarQuizStart(String question) {
        return languageManager.getActionBarQuizStart(question);
    }

    public String getAnnounceQuizEnd() {
        return getPrefix() + languageManager.getAnnounceQuizEnd();
    }

    public String getAnnounceQuizWinner(Player player) {
        return languageManager.getAnnounceQuizWinner(player);
    }

    public String getPersonalQuizWinner() {
        return languageManager.getPersonalQuizWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalQuizWinner();
    }

    //foodrace messages
    public String getAnnounceFoodStart(ItemStack itemStack) {
        return languageManager.getAnnounceFoodStart(itemStack);
    }
    public String getActionBarFoodStart(ItemStack itemStack) {
        return languageManager.getActionBarFoodStart(itemStack);
    }

    public String getAnnounceFoodEnd() {
        return getPrefix() + languageManager.getAnnounceFoodEnd();
    }

    public String getAnnounceFoodWinner(Player player) {
        return languageManager.getAnnounceFoodWinner(player);
    }

    public String getPersonalFoodWinner() {
        return languageManager.getPersonalFoodWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalFoodWinner();
    }

    //scramblerace messages
    public String getAnnounceScrambleStart(String word) {
        return languageManager.getAnnounceScrambleStart(word);
    }
    public String getActionBarScrambleStart(String word) {
        return languageManager.getActionBarScrambleStart(word);
    }

    public String getAnnounceScrambleEnd(String answer) {
        return getPrefix() + languageManager.getAnnounceScrambleEnd(answer);
    }

    public String getAnnounceScrambleWinner(Player player, String answer) {
        return languageManager.getAnnounceScrambleWinner(player, answer);
    }

    public String getPersonalScrambleWinner() {
        return languageManager.getPersonalScrambleWinner().isEmpty() ? "" : getPrefix() + languageManager.getPersonalScrambleWinner();
    }

    //helper methods
    public String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private String parseColorCodes(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String stripColors(String message){
        return ChatColor.stripColor(message);
    }

}
