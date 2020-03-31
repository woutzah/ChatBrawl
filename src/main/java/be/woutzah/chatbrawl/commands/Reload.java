package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class Reload implements CommandExecutor {

    private ChatBrawl plugin;

    public Reload(ChatBrawl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.getRaceCreator().getRaceCreationTask().cancel();
        switch (plugin.getRaceCreator().getCurrentRunningRace()){
            case chat:
                plugin.getRaceCreator().getChatRaceTask().cancel();
                break;
            case block:
                plugin.getRaceCreator().getBlockRaceTask().cancel();
                break;
            case fish:
                plugin.getRaceCreator().getFishRaceTask().cancel();
                break;
            case hunt:
                plugin.getRaceCreator().getHuntRaceTask().cancel();
                break;
            case craft:
                plugin.getRaceCreator().getCraftRaceTask().cancel();
                break;
            case quiz:
                plugin.getRaceCreator().getQuizRaceTask().cancel();
            case food:
                plugin.getRaceCreator().getFoodRaceTask().cancel();
            case none:
                break;
        }
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        plugin.setupFiles();
        plugin.init();
        RaceException.resetIsBadConfig();
        plugin.configChecker();
        sender.sendMessage(plugin.getPrinter().getReloadMessage());
        plugin.getRaceCreator().createRaces();
        return true;
    }
}
