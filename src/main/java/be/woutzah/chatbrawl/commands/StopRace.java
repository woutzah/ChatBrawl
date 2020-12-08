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

    private final Printer printer;
    private final RaceCreator raceCreator;

    public StopRace(ChatBrawl plugin) {
        this.printer = plugin.getPrinter();
        this.raceCreator = plugin.getRaceCreator();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!raceCreator.getCurrentRunningRace().equals(RaceType.NONE)) {
            try {
                raceCreator.getActionbarTask().cancel();
            } catch (Exception ignored) { }

            switch (raceCreator.getCurrentRunningRace()) {
                case CHAT:
                    raceCreator.getChatRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.CHAT), "cb.default");
                    return true;
                case BLOCK:
                    raceCreator.getBlockRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.BLOCK), "cb.default");
                    return true;
                case FISH:
                    raceCreator.getFishRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.FISH), "cb.default");
                    return true;
                case HUNT:
                    raceCreator.getHuntRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.HUNT), "cb.default");
                    return true;
                case CRAFT:
                    raceCreator.getCraftRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.CRAFT), "cb.default");
                    return true;
                case QUIZ:
                    raceCreator.getQuizRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.QUIZ), "cb.default");
                    return true;
                case FOOD:
                    raceCreator.getFoodRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.FOOD), "cb.default");
                    return true;
                case SCRAMBLE:
                    raceCreator.getScrambleRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    raceCreator.setRacesEnabled(false);
                    Bukkit.broadcast(printer.getStoppedRace(RaceType.SCRAMBLE), "cb.default");
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
