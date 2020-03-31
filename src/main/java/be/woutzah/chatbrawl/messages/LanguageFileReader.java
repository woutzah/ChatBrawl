package be.woutzah.chatbrawl.messages;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import com.meowj.langutils.lang.LanguageHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class LanguageFileReader {

    private ChatBrawl plugin;
    private String lang;
    private String chatraceName;
    private List<String> chatraceStart;
    private String chatraceEnd;
    private List<String> chatraceWinner;
    private String chatraceWinnerPersonal;
    private String blockraceName;
    private List<String> blockraceStart;
    private String blockraceEnd;
    private List<String> blockraceWinner;
    private String blockraceWinnerPersonal;
    private String fishraceName;
    private List<String> fishraceStart;
    private String fishraceEnd;
    private List<String> fishraceWinner;
    private String fishraceWinnerPersonal;
    private String huntraceName;
    private List<String> huntraceStart;
    private String huntraceEnd;
    private List<String> huntraceWinner;
    private String huntraceWinnerPersonal;
    private String craftraceName;
    private List<String> craftraceStart;
    private String craftraceEnd;
    private List<String> craftraceWinner;
    private String craftraceWinnerPersonal;
    private String quizraceName;
    private List<String> quizraceStart;
    private String quizraceEnd;
    private List<String> quizraceWinner;
    private String quizraceWinnerPersonal;
    private String foodraceName;
    private List<String> foodraceStart;
    private String foodraceEnd;
    private List<String> foodraceWinner;
    private String foodraceWinnerPersonal;
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
    private FileConfiguration languageConfig;


    public LanguageFileReader(ChatBrawl plugin){
        this.plugin = plugin;
        this.languageConfig = plugin.getLanguageConfig();
        this.lang = languageConfig.getString("lang");
        this.chatraceName = languageConfig.getString("chatrace.chatrace-name");
        this.chatraceStart = languageConfig.getStringList("chatrace.chatrace-start");
        this.chatraceEnd = languageConfig.getString("chatrace.chatrace-ended");
        this.chatraceWinner = languageConfig.getStringList("chatrace.chatrace-winner");
        this.chatraceWinnerPersonal = languageConfig.getString("chatrace.chatrace-winner-personal");
        this.blockraceName = languageConfig.getString("blockrace.blockrace-name");
        this.blockraceStart = languageConfig.getStringList("blockrace.blockrace-start");
        this.blockraceEnd = languageConfig.getString("blockrace.blockrace-ended");
        this.blockraceWinner = languageConfig.getStringList("blockrace.blockrace-winner");
        this.blockraceWinnerPersonal = languageConfig.getString("blockrace.blockrace-winner-personal");
        this.fishraceName = languageConfig.getString("fishrace.fishrace-name");
        this.fishraceStart = languageConfig.getStringList("fishrace.fishrace-start");
        this.fishraceEnd = languageConfig.getString("fishrace.fishrace-ended");
        this.fishraceWinner = languageConfig.getStringList("fishrace.fishrace-winner");
        this.fishraceWinnerPersonal = languageConfig.getString("fishrace.fishrace-winner-personal");
        this.huntraceName = languageConfig.getString("huntrace.huntrace-name");
        this.huntraceStart = languageConfig.getStringList("huntrace.huntrace-start");
        this.huntraceEnd = languageConfig.getString("huntrace.huntrace-ended");
        this.huntraceWinner = languageConfig.getStringList("huntrace.huntrace-winner");
        this.huntraceWinnerPersonal = languageConfig.getString("huntrace.huntrace-winner-personal");
        this.craftraceName = languageConfig.getString("craftrace.craftrace-name");
        this.craftraceStart = languageConfig.getStringList("craftrace.craftrace-start");
        this.craftraceEnd = languageConfig.getString("craftrace.craftrace-ended");
        this.craftraceWinner = languageConfig.getStringList("craftrace.craftrace-winner");
        this.craftraceWinnerPersonal = languageConfig.getString("craftrace.craftrace-winner-personal");
        this.quizraceName = languageConfig.getString("quizrace.quizrace-name");
        this.quizraceStart = languageConfig.getStringList("quizrace.quizrace-start");
        this.quizraceEnd = languageConfig.getString("quizrace.quizrace-ended");
        this.quizraceWinner = languageConfig.getStringList("quizrace.quizrace-winner");
        this.quizraceWinnerPersonal = languageConfig.getString("quizrace.quizrace-winner-personal");
        this.foodraceName = languageConfig.getString("foodrace.foodrace-name");
        this.foodraceStart = languageConfig.getStringList("foodrace.foodrace-start");
        this.foodraceEnd = languageConfig.getString("foodrace.foodrace-ended");
        this.foodraceWinner = languageConfig.getStringList("foodrace.foodrace-winner");
        this.foodraceWinnerPersonal = languageConfig.getString("foodrace.foodrace-winner-personal");
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
                return parseColorCodes(stoppedRace.replace("<race>", chatraceName));
            case block:
                return parseColorCodes(stoppedRace.replace("<race>", blockraceName));
            case fish:
                return parseColorCodes(stoppedRace.replace("<race>", fishraceName));
            case hunt:
                return parseColorCodes(stoppedRace.replace("<race>", huntraceName));
            case craft:
                return parseColorCodes(stoppedRace.replace("<race>", craftraceName));
            case quiz:
                return parseColorCodes(stoppedRace.replace("<race>", quizraceName));
            case food:
                return parseColorCodes(stoppedRace.replace("<race>", foodraceName));
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
        for (String line : chatraceStart) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<word>", word));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceChatEnd() {
        return parseColorCodes(chatraceEnd);
    }

    public String getAnnounceChatWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : chatraceWinner) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalChatWinner() {
        return parseColorCodes(chatraceWinnerPersonal);
    }

    //blockrace messages
    public String getAnnounceBlockStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : blockraceStart) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<block>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceBlockEnd() {
        return parseColorCodes(blockraceEnd);
    }

    public String getAnnounceBlockWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : blockraceWinner) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalBlockWinner() {
        return parseColorCodes(blockraceWinnerPersonal);
    }
    
    //fishrace messages
    public String getAnnounceFishStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : fishraceStart) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<fish>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceFishEnd() {
        return parseColorCodes(fishraceEnd);
    }

    public String getAnnounceFishWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : fishraceWinner) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalFishWinner() {
        return parseColorCodes(fishraceWinnerPersonal);
    }

    //huntrace messages
    public String getAnnounceHuntStart(EntityType entityType, int amount) {
        StringBuilder sb = new StringBuilder();
        for (String line : huntraceStart) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf(amount))
                    .replace("<mob>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getEntityName(entityType,lang):
                            capitalize(entityType.name())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceHuntEnd() {
        return parseColorCodes(huntraceEnd);
    }

    public String getAnnounceHuntWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : huntraceWinner) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalHuntWinner() {
        return parseColorCodes(huntraceWinnerPersonal);
    }

    //craftrace messages
    public String getAnnounceCraftStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : craftraceStart) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<item>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceCraftEnd() {
        return parseColorCodes(craftraceEnd);
    }

    public String getAnnounceCraftWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : craftraceWinner) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalCraftWinner() {
        return parseColorCodes(craftraceWinnerPersonal);
    }

    //quizrace messages
    public String getAnnounceQuizStart(String question){
        StringBuilder sb = new StringBuilder();
        for (String line : quizraceStart) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<question>", question));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceQuizEnd() {
        return parseColorCodes(quizraceEnd);
    }

    public String getAnnounceQuizWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : quizraceWinner) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalQuizWinner() {
        return parseColorCodes(quizraceWinnerPersonal);
    }

    //foodrace messages
    public String getAnnounceFoodStart(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for (String line : foodraceStart) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<amount>", String.valueOf( itemStack.getAmount()))
                    .replace("<food>", ChatBrawl.langUtilsIsEnabled ?
                            LanguageHelper.getItemName(itemStack, lang):
                            formatMaterialName(itemStack.getType())));
        }
        return parseColorCodes(sb.toString());
    }

    public String getAnnounceFoodEnd() {
        return parseColorCodes(foodraceEnd);
    }

    public String getAnnounceFoodWinner(Player player) {
        StringBuilder sb = new StringBuilder();
        for (String line : foodraceWinner) {
            sb.append(line.replace("\\n", "\n")
                    .replace("<player>", player.getDisplayName()));
        }
        return parseColorCodes(sb.toString());
    }

    public String getPersonalFoodWinner() {
        return parseColorCodes(foodraceWinnerPersonal);
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


}
