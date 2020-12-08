package be.woutzah.chatbrawl.placeholders;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {

    private final ChatBrawl plugin;

    public Placeholders(ChatBrawl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "chatbrawl";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("currentrace")) {
            if (plugin.getRaceCreator().getCurrentRunningRace() == RaceType.NONE) {
                return "No Race";
            }

            return plugin
                    .getPrinter()
                    .capitalize(plugin.getRaceCreator().getCurrentRunningRace().toString()) + " Race";
        }

        return null;
    }
}
