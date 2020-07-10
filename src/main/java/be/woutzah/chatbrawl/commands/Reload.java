package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.listeners.ChatRaceListener;
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
        try {
            plugin.getRaceCreator().getRaceCreationTask().cancel();
        }catch (Exception ignored){

        }
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
                break;
            case food:
                plugin.getRaceCreator().getFoodRaceTask().cancel();
                break;
            case scramble:
                plugin.getRaceCreator().getScrambleRaceTask().cancel();
                break;
            case none:
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
