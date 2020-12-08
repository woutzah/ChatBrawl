package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.utils.Permission;
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
        if (raceCreator.getCurrentRunningRace() == RaceType.NONE) {
            if (!printer.getNoRaceRunning().isEmpty()) {
                sender.sendMessage(printer.getNoRaceRunning());
            }

            return true;
        }

        try {
            raceCreator.getActionbarTask().cancel();
        } catch (Exception ignored) { }

        final RaceType currentRunningRace = raceCreator.getCurrentRunningRace();

        switch (currentRunningRace) {
            case BLOCK: {
                raceCreator.getBlockRaceTask().cancel();
                break;
            }

            case CHAT: {
                raceCreator.getChatRaceTask().cancel();
                break;
            }

            case CRAFT: {
                raceCreator.getCraftRaceTask().cancel();
                break;
            }

            case FISH: {
                raceCreator.getFishRaceTask().cancel();
                break;
            }

            case FOOD: {
                raceCreator.getFoodRaceTask().cancel();
                break;
            }

            case HUNT: {
                raceCreator.getHuntRaceTask().cancel();
                break;
            }

            case QUIZ: {
                raceCreator.getQuizRaceTask().cancel();
                break;
            }

            case SCRAMBLE: {
                raceCreator.getScrambleRaceTask().cancel();
                break;
            }
        }

        raceCreator.setCurrentRunningRace(RaceType.NONE);
        raceCreator.getChatRaceTask().cancel();
        Bukkit.broadcast(printer.getStoppedRace(currentRunningRace), Permission.DEFAULT);
        return true;
    }
}
