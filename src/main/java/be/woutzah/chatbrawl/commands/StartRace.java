package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartRace implements CommandExecutor {

    private final Printer printer;
    private final RaceCreator raceCreator;

    public StartRace(ChatBrawl plugin) {
        this.printer = plugin.getPrinter();
        this.raceCreator = plugin.getRaceCreator();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (raceCreator.getCurrentRunningRace() != RaceType.NONE) {
            sender.sendMessage(printer.getRaceStillRunning());
            return true;
        }
        try {
            raceCreator.getRaceCreationTask().cancel();
        } catch (Exception ignored) { }

        switch (args[0].toUpperCase()) {
            case "CHAT":
                Bukkit.broadcast(printer.getStartedRace(RaceType.CHAT), "cb.default");
                raceCreator.chatRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            case "BLOCK":
                Bukkit.broadcast(printer.getStartedRace(RaceType.BLOCK), "cb.default");
                raceCreator.blockRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            case "CRAFT":
                Bukkit.broadcast(printer.getStartedRace(RaceType.CRAFT), "cb.default");
                raceCreator.craftRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            case "FISH":
                Bukkit.broadcast(printer.getStartedRace(RaceType.FISH), "cb.default");
                raceCreator.fishRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            case "FOOD":
                Bukkit.broadcast(printer.getStartedRace(RaceType.FOOD), "cb.default");
                raceCreator.foodRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            case "HUNT":
                Bukkit.broadcast(printer.getStartedRace(RaceType.HUNT), "cb.default");
                raceCreator.huntRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            case "QUIZ":
                Bukkit.broadcast(printer.getStartedRace(RaceType.QUIZ), "cb.default");
                raceCreator.quizRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            case "SCRAMBLE":
                Bukkit.broadcast(printer.getStartedRace(RaceType.SCRAMBLE), "cb.default");
                raceCreator.scrambleRaceStart();
                raceCreator.setRacesEnabled(true);
                return true;
            default:
                sender.sendMessage(printer.getRaceTypeNotExist());
        }
        return true;
    }
}
