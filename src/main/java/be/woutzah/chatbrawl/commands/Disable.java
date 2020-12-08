package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Disable implements CommandExecutor {

    private final RaceCreator raceCreator;
    private final Printer printer;

    public Disable(ChatBrawl plugin) {
        this.printer = plugin.getPrinter();
        this.raceCreator = plugin.getRaceCreator();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (raceCreator.getRacesEnabled()) {
            raceCreator.setRacesEnabled(false);
            try {
                raceCreator.getRaceCreationTask().cancel();
                raceCreator.getActionbarTask().cancel();
            }catch (Exception ignored){

            }
            switch (raceCreator.getCurrentRunningRace()) {
                case CHAT:
                    raceCreator.getChatRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
                case BLOCK:
                    raceCreator.getBlockRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
                case FISH:
                    raceCreator.getFishRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
                case HUNT:
                    raceCreator.getHuntRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
                case CRAFT:
                    raceCreator.getCraftRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
                case QUIZ:
                    raceCreator.getQuizRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
                case FOOD:
                    raceCreator.getFoodRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
                case SCRAMBLE:
                    raceCreator.getScrambleRaceTask().cancel();
                    raceCreator.setCurrentRunningRace(RaceType.NONE);
                    break;
            }
            sender.sendMessage(printer.getDisabled());
        } else {
            sender.sendMessage(printer.getAlreadyDisabled());
        }
        return true;
    }
}
