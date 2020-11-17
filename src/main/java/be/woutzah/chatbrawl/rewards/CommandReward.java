package be.woutzah.chatbrawl.rewards;

import java.util.List;

public class CommandReward {

    private String broadcastString;
    private String titleString;
    private String subtitleString;
    private List<String> commands;
    private int chance;

    public CommandReward(String broadcastString, String titleString, String subtitleString, List<String> commands, int chance) {
        this.broadcastString = broadcastString;
        this.titleString = titleString;
        this.subtitleString = subtitleString;
        this.commands = commands;
        this.chance = chance;
    }

    public String getBroadcastString() {
        return broadcastString;
    }

    public String getTitleString() {
        return titleString;
    }

    public String getSubtitleString() {
        return subtitleString;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getChance() {
        return chance;
    }
}
