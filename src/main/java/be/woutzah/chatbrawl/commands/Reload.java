package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class Reload implements CommandExecutor {

    private final ChatBrawl plugin;

    public Reload(ChatBrawl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            plugin.getRaceCreator().getRaceCreationTask().cancel();
            plugin.getRaceCreator().getActionbarTask().cancel();
        } catch (Exception ignored) { }

        switch (plugin.getRaceCreator().getCurrentRunningRace()) {
            case CHAT:
                plugin.getRaceCreator().getChatRaceTask().cancel();
                break;
            case BLOCK:
                plugin.getRaceCreator().getBlockRaceTask().cancel();
                break;
            case FISH:
                plugin.getRaceCreator().getFishRaceTask().cancel();
                break;
            case HUNT:
                plugin.getRaceCreator().getHuntRaceTask().cancel();
                break;
            case CRAFT:
                plugin.getRaceCreator().getCraftRaceTask().cancel();
                break;
            case QUIZ:
                plugin.getRaceCreator().getQuizRaceTask().cancel();
                break;
            case FOOD:
                plugin.getRaceCreator().getFoodRaceTask().cancel();
                break;
            case SCRAMBLE:
                plugin.getRaceCreator().getScrambleRaceTask().cancel();
                break;
            case NONE:
                break;
        }
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        plugin.setupFiles();
        HandlerList.unregisterAll(plugin);
        plugin.init();
        RaceException.resetIsBadConfig();
        plugin.configChecker();
        sender.sendMessage(plugin.getPrinter().getReloadMessage());
        return true;
    }
}
