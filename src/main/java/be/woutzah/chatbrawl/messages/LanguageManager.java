package be.woutzah.chatbrawl.messages;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.races.types.*;
import com.meowj.langutils.lang.LanguageHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LanguageManager {

    private ChatBrawl plugin;
    private String lang;
    private ChatRace chatRace;
    private BlockRace blockRace;
    private FishRace fishRace;
    private HuntRace huntRace;
    private CraftRace craftRace;
    private QuizRace quizRace;
    private FoodRace foodRace;
    private ScrambleRace scrambleRace;
    private List<String> helpMenuList;
    private String startedCreating;
    private String noPermission;
    private String subcommandNotExist;
    private String stopRaceUsage;
    private String stoppedRace;
    private String noRaceRunning;
    private String disabled;
    private String alreadyDisabled;
    private String enabled;
    private String alreadyEnabled;
    private boolean centerMessageEnabled;


    public LanguageManager(ChatBrawl plugin){
        this.plugin = plugin;
        this.centerMessageEnabled = plugin.getConfig().getBoolean("center-racestart");
        FileConfiguration languageConfig = plugin.getLanguageConfig();
        this.lang = languageConfig.getString("lang");
        this.chatRace = plugin.getChatrace();
        this.blockRace = plugin.getBlockRace();
        this.fishRace = plugin.getFishRace();
        this.huntRace =plugin.getHuntRace();
        this.craftRace = plugin.getCraftRace();
        this.quizRace = plugin.getQuizRace();
        this.foodRace = plugin.getFoodRace();
        this.scrambleRace = plugin.getScrambleRace();
        this.helpMenuList = languageConfig.getStringList("general.helpmenu");
        this.startedCreating = languageConfig.getString("general.started-creating");
        this.noPermission = languageConfig.getString("commands.no-permission");
        this.subcommandNotExist = languageConfig.getString("commands.subcommand-not-exist");
        this.stopRaceUsage = languageConfig.getString("commands.stop-race-usage");
        this.stoppedRace = languageConfig.getString("commands.stopped-race");
        this.noRaceRunning = languageConfig.getString("commands.no-race-running");
        this.disabled = languageConfig.getString("commands.disabled");
        this.alreadyDisabled = languageConfig.getString("commands.already-disabled");
        this.enabled = languageConfig.getString("commands.enabled");
        this.alreadyEnabled = languageConfig.getString("commands.already-enabled");
    }

    //General messages
    public String getHelpMenu() {
        StringBuilder sb = new StringBuilder();
        for (String line : helpMenuList) {
            sb.append(line.replace("\\n", "\n"));
        }
        return parseColorCodes(sb.toString());
    }

    public String getStartedCreating() {
        return parseColorCodes(startedCreating);
    }

    public String getNoPermission() {
        return parseColorCodes(noPermission);
    }

    public String getSubcommandNotExist() {
        return parseColorCodes(subcommandNotExist);
    }

    public String getStopRaceUsage() {
        return parseColorCodes(stopRaceUsage);
    }

    public String getStoppedRace(RaceType raceType) {
        switch (raceType) {
            case chat:
                return parseColorCodes(stoppedRace.replace("<race>", chatRace.getChatraceName()));
            case block:
                return parseColorCodes(stoppedRace.replace("<race>", blockRace.getBlockraceName()));
            case fish:
                return parseColorCodes(stoppedRace.replace("<race>", fishRace.getFishraceName()));
            case hunt:
                return parseColorCodes(stoppedRace.replace("<race>", huntRace.getHuntraceName()));
            case craft:
                return parseColorCodes(stoppedRace.replace("<race>", craftRace.getCraftraceName()));
            case quiz:
                return parseColorCodes(stoppedRace.replace("<race>", quizRace.getQuizraceName()));
            case food:
                return parseColorCodes(stoppedRace.replace("<race>", foodRace.getFoodraceName()));
            case scramble:
                return parseColorCodes(stoppedRace.replace("<race>", scrambleRace.getScrambleraceName()));
        }
        return "wrong format";
    }

    public String getNoRaceRunning() {
        return parseColorCodes(noRaceRunning);
    }

    public String getDisabled() {
        return parseColorCodes(disabled);
    }

    public String getAlreadyDisabled() {
        return parseColorCodes(alreadyDisabled);
    }

    public String getEnabled() {
        return parseColorCodes(enabled);
    }

    public String getAlreadyEnabled() {
        return parseColorCodes(alreadyEnabled);
    }

    //chatrace messages
    public String getAnnounceChatStart(String word){
        StringBuilder sb = new StringBuilder();
        for (String line : chatRace.getChatraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<word>", word)));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceChatEnd() {
        return parseColorCodes(chatRace.getChatraceEnd());
    }

    public String getAnnounceChatWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : chatRace.getChatraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName())
                    .replace("<rawname>",player.getName())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalChatWinner() {
        return parseColorCodes(chatRace.getChatraceWinnerPersonal());
    }

    //blockrace messages
    public String getAnnounceBlockStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : blockRace.getBlockraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<block>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType()))));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceBlockEnd() {
        return parseColorCodes(blockRace.getBlockraceEnd());
    }

    public String getAnnounceBlockWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : blockRace.getBlockraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()))
                    .replace("<rawname>",player.getName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalBlockWinner() {
        return parseColorCodes(blockRace.getBlockraceWinnerPersonal());
    }
    
    //fishrace messages
    public String getAnnounceFishStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : fishRace.getFishraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<fish>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType()))));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceFishEnd() {
        return parseColorCodes(fishRace.getFishraceEnd());
    }

    public String getAnnounceFishWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : fishRace.getFishraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName())
                    .replace("<rawname>",player.getName())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalFishWinner() {
        return parseColorCodes(fishRace.getFishraceWinnerPersonal());
    }

    //huntrace messages
    public String getAnnounceHuntStart(EntityType entityType, int amount) {
        StringBuilder sb = new StringBuilder();
        for (String line : huntRace.getHuntraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf(amount))
                    .replace("<mob>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getEntityName(entityType,lang):
                            capitalize(entityType.name()))));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceHuntEnd() {
        return parseColorCodes(huntRace.getHuntraceEnd());
    }

    public String getAnnounceHuntWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : huntRace.getHuntraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName())
                    .replace("<rawname>",player.getName())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalHuntWinner() {
        return parseColorCodes(huntRace.getHuntraceWinnerPersonal());
    }

    //craftrace messages
    public String getAnnounceCraftStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : craftRace.getCraftraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<item>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType()))));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceCraftEnd() {
        return parseColorCodes(craftRace.getCraftraceEnd());
    }

    public String getAnnounceCraftWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : craftRace.getCraftraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName())
                    .replace("<rawname>",player.getName())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalCraftWinner() {
        return parseColorCodes(craftRace.getCraftraceWinnerPersonal());
    }

    //quizrace messages
    public String getAnnounceQuizStart(String question){
        StringBuilder sb = new StringBuilder();
        for (String line : quizRace.getQuizraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<question>", question)));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceQuizEnd() {
        return parseColorCodes(quizRace.getQuizraceEnd());
    }

    public String getAnnounceQuizWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : quizRace.getQuizraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName())
                    .replace("<rawname>",player.getName())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalQuizWinner() {
        return parseColorCodes(quizRace.getQuizraceWinnerPersonal());
    }

    //foodrace messages
    public String getAnnounceFoodStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : foodRace.getFoodraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<food>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType()))));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceFoodEnd() {
        return parseColorCodes(foodRace.getFoodraceEnd());
    }

    public String getAnnounceFoodWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : foodRace.getFoodraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName())
                    .replace("<rawname>",player.getName())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalFoodWinner() {
        return parseColorCodes(foodRace.getFoodraceWinnerPersonal());
    }

    //scramblerace messages
    public String getAnnounceScrambleStart(String word){
        StringBuilder sb = new StringBuilder();
        for (String line : scrambleRace.getScrambleraceStart()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<word>", word)));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceScrambleEnd() {
        return parseColorCodes(scrambleRace.getScrambleraceEnd());
    }

    public String getAnnounceScrambleWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : scrambleRace.getScrambleraceWinner()) {
            if(centerMessageEnabled){
                line = line.trim();
            }
            sb.append(centerMessage(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName())
                    .replace("<rawname>",player.getName())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalScrambleWinner() {
        return parseColorCodes(scrambleRace.getScrambleraceWinnerPersonal());
    }

    //helper methods
    private String parseColorCodes(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private String formatMaterialName(Material material) {
        return capitalize(material.toString().replace("_", " ").toLowerCase());
    }

    public String capitalize(String str) {
        if ((str == null) || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public String centerMessage(String message) {
        if (!centerMessageEnabled){
            return message;
        }
        final int CENTER_PX = 154;
        message = parseColorCodes(message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }


}
