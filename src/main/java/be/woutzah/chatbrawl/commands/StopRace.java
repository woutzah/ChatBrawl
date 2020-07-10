package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class StopRace implements CommandExecutor {

    private Printer printer;
    private RaceCreator raceCreator;

    public StopRace(ChatBrawl plugin) {
        this.printer = plugin.getPrinter();
        this.raceCreator = plugin.getRaceCreator();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!raceCreator.getCurrentRunningRace().equals(RaceType.none)) {
            switch (raceCreator.getCurrentRunningRace()) {
                case chat:
                    raceCreator.getChatRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.chat), "cb.default");
                    return true;
                case block:
                    raceCreator.getBlockRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.block), "cb.default");
                    return true;
                case fish:
                    raceCreator.getFishRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.fish), "cb.default");
                    return true;
                case hunt:
                    raceCreator.getHuntRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.hunt), "cb.default");
                    return true;
                case craft:
                    raceCreator.getCraftRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.craft), "cb.default");
                    return true;
                case quiz:
                    raceCreator.getQuizRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.quiz), "cb.default");
                    return true;
                case food:
                    raceCreator.getFoodRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.food), "cb.default");
                    return true;
                case scramble:
                    raceCreator.getScrambleRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.none);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.scramble), "cb.default");
                    return true;
            }
        } else {
            if (!printer.getNoRaceRunning().isEmpty()) {
                sender.sendMessage(printer.getNoRaceRunning());
            }
        }
        return true;
    }
}
