package be.woutzah.chatbrawl.messages;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Printer {

    private final String discordLink;
    private ChatBrawl plugin;
    private FileConfiguration config;

    public Printer(ChatBrawl plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        discordLink = "https://discord.gg/xPFPYbV";
    }

    //General messages
    public String getPrefix() {
        return parseColorCodes(config.getString("plugin-prefix"));
    }

    public String getHelpMenu() {
        return plugin.getLanguageFileReader().getHelpMenu();
    }

    public String getStartedCreating() {
        return getPrefix() + plugin.getLanguageFileReader().getStartedCreating();
    }

    public String getReloadMessage() {
        return parseColorCodes(getPrefix() + "&aChatBrawl &eV" + plugin.getDescription().getVersion() +" &ahas been reloaded!" );
    }

    public String getDiscordMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("&e&l>&7&m---------&e&l[ &6&oChatBrawl Discord &e&l]&7&m---------&e&l<\n");
        sb.append("      &fFor &esupport/issues&f or &esuggestions           \n");
        sb.append("           &fjoin our official discord!               \n");
        sb.append("          &d&o&n" + discordLink + "\n");
        sb.append("&e&l>&7&m------------------------------------&e&l<\n");

        return parseColorCodes(sb.toString());
    }

    public void printConsoleMessage() {
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>&7&m------------&e&l[ &6&oChatBrawl &e&l]&7&m-----------&e&l<"));
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>                                    &e&l<"));
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>           &bChatbrawl V"
                        + plugin.getDescription().getVersion()
                        + "           &e&l<"));
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>             &9&oBy woutzah             &e&l<"));
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>                                    &e&l<"));
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>            &eEnabling ...            &e&l<"));
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>                                    &e&l<"));
        Bukkit.getConsoleSender()
                .sendMessage(parseColorCodes("&e&l>&7&m------------------------------------&e&l<"));
    }

    //commands messages
    public String getNoPermission() {
        return getPrefix() + plugin.getLanguageFileReader().getNoPermission();
    }

    public String getSubcommandNotExist() {
        return getPrefix() + plugin.getLanguageFileReader().getSubcommandNotExist();
    }

    public String getStopRaceUsage() {
        return getPrefix() + plugin.getLanguageFileReader().getStopRaceUsage();
    }

    public String getStoppedRace(RaceType raceType) {
        return getPrefix() + plugin.getLanguageFileReader().getStoppedRace(raceType);
    }

    public String getNoRaceRunning() {
        return getPrefix() + plugin.getLanguageFileReader().getNoRaceRunning();
    }

    public String getDisabled() {
        return getPrefix() + plugin.getLanguageFileReader().getDisabled();
    }

    public String getAlreadyDisabled() {
        return getPrefix() + plugin.getLanguageFileReader().getAlreadyDisabled();
    }

    public String getEnabled() {
        return getPrefix() + plugin.getLanguageFileReader().getEnabled();
    }

    public String getAlreadyEnabled() {
        return getPrefix() + plugin.getLanguageFileReader().getAlreadyEnabled();
    }


    public String getCurrentRunningRaceInfo() {
        switch (plugin.getRaceCreator().getCurrentRunningRace()) {
            case chat:
                return getAnnounceChatStart(plugin.getChatrace().getWordToGuess());
            case block:
                return getAnnounceBlockStart(plugin.getBlockRace().getCurrentItemStack());
            case fish:
                return getAnnounceFishStart(plugin.getFishRace().getCurrentItemStack());
            case hunt:
                return getAnnounceHuntStart(plugin.getHuntRace().getCurrentEntityType(), plugin.getHuntRace().getCurrentAmount());
            case craft:
                return getAnnounceCraftStart(plugin.getCraftRace().getCurrentItemStack());
            case quiz:
                return getAnnounceQuizStart(plugin.getQuizRace().getCurrentQuestion());
            case food:
                return getAnnounceFoodStart(plugin.getFoodRace().getCurrentItemStack());
            case none:
                return getNoRaceRunning();
        }
        return null;
    }

    //chatrace messages
    public String getAnnounceChatStart(String word) {
        return plugin.getLanguageFileReader().getAnnounceChatStart(word);
    }

    public String getAnnounceChatEnd() {
        return getPrefix() + plugin.getLanguageFileReader().getAnnounceChatEnd();
    }

    public String getAnnounceChatWinner(Player player) {
        return plugin.getLanguageFileReader().getAnnounceChatWinner(player);
    }

    public String getPersonalChatWinner() {
        return plugin.getLanguageFileReader().getPersonalChatWinner().isEmpty() ? "" :getPrefix() + plugin.getLanguageFileReader().getPersonalChatWinner();
    }

    //blockrace messages
    public String getAnnounceBlockStart(ItemStack itemStack) {
        return plugin.getLanguageFileReader().getAnnounceBlockStart(itemStack);
    }

    public String getAnnounceBlockEnd() {
        return getPrefix() + plugin.getLanguageFileReader().getAnnounceBlockEnd();
    }

    public String getAnnounceBlockWinner(Player player) {
        return plugin.getLanguageFileReader().getAnnounceBlockWinner(player);
    }

    public String getPersonalBlockWinner() {
        return plugin.getLanguageFileReader().getPersonalBlockWinner().isEmpty() ? "" :getPrefix() + plugin.getLanguageFileReader().getPersonalBlockWinner();
    }

    //fishrace messages
    public String getAnnounceFishStart(ItemStack itemStack) {
        return plugin.getLanguageFileReader().getAnnounceFishStart(itemStack);
    }

    public String getAnnounceFishEnd() {
        return getPrefix() + plugin.getLanguageFileReader().getAnnounceFishEnd();
    }

    public String getAnnounceFishWinner(Player player) {
        return plugin.getLanguageFileReader().getAnnounceFishWinner(player);
    }

    public String getPersonalFishWinner() {
        return plugin.getLanguageFileReader().getPersonalFishWinner().isEmpty() ? "" :getPrefix() + plugin.getLanguageFileReader().getPersonalFishWinner();
    }

    //huntrace messages
    public String getAnnounceHuntStart(EntityType entityType, int amount) {
        return plugin.getLanguageFileReader().getAnnounceHuntStart(entityType, amount);
    }

    public String getAnnounceHuntEnd() {
        return getPrefix() + plugin.getLanguageFileReader().getAnnounceHuntEnd();
    }

    public String getAnnounceHuntWinner(Player player) {
        return plugin.getLanguageFileReader().getAnnounceHuntWinner(player);
    }

    public String getPersonalHuntWinner() {
        return plugin.getLanguageFileReader().getPersonalHuntWinner().isEmpty() ? "" :getPrefix() + plugin.getLanguageFileReader().getPersonalHuntWinner();
    }

    //craftrace messages
    public String getAnnounceCraftStart(ItemStack itemStack) {
        return plugin.getLanguageFileReader().getAnnounceCraftStart(itemStack);
    }

    public String getAnnounceCraftEnd() {
        return getPrefix() + plugin.getLanguageFileReader().getAnnounceCraftEnd();
    }

    public String getAnnounceCraftWinner(Player player) {
        return plugin.getLanguageFileReader().getAnnounceCraftWinner(player);
    }

    public String getPersonalCraftWinner() {
        return plugin.getLanguageFileReader().getPersonalCraftWinner().isEmpty() ? "" :getPrefix() + plugin.getLanguageFileReader().getPersonalCraftWinner();
    }
    
    //quizrace messages
    public String getAnnounceQuizStart(String word) {
        return plugin.getLanguageFileReader().getAnnounceQuizStart(word);
    }

    public String getAnnounceQuizEnd() {
        return getPrefix() + plugin.getLanguageFileReader().getAnnounceQuizEnd();
    }

    public String getAnnounceQuizWinner(Player player) {
        return plugin.getLanguageFileReader().getAnnounceQuizWinner(player);
    }

    public String getPersonalQuizWinner() {
        return plugin.getLanguageFileReader().getPersonalQuizWinner().isEmpty() ? "" :getPrefix() + plugin.getLanguageFileReader().getPersonalQuizWinner();
    }

    //foodrace messages
    public String getAnnounceFoodStart(ItemStack itemStack) {
        return plugin.getLanguageFileReader().getAnnounceFoodStart(itemStack);
    }

    public String getAnnounceFoodEnd() {
        return getPrefix() + plugin.getLanguageFileReader().getAnnounceFoodEnd();
    }

    public String getAnnounceFoodWinner(Player player) {
        return plugin.getLanguageFileReader().getAnnounceFoodWinner(player);
    }

    public String getPersonalFoodWinner() {
        return plugin.getLanguageFileReader().getPersonalFoodWinner().isEmpty() ? "" :getPrefix() + plugin.getLanguageFileReader().getPersonalFoodWinner();
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

}
