package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Disable implements CommandExecutor {

  private RaceCreator raceCreator;
  private Printer printer;

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
      }catch (Exception ignored){

      }
      switch (raceCreator.getCurrentRunningRace()) {
        case chat:
          raceCreator.getChatRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
        case block:
          raceCreator.getBlockRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
        case fish:
          raceCreator.getFishRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
        case hunt:
          raceCreator.getHuntRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
        case craft:
          raceCreator.getCraftRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
        case quiz:
          raceCreator.getQuizRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
        case food:
          raceCreator.getFoodRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
        case scramble:
          raceCreator.getScrambleRaceTask().cancel();
          raceCreator.setCurrentRunningRace(RaceType.none);
          break;
      }
      sender.sendMessage(printer.getDisabled());
    } else {
      sender.sendMessage(printer.getAlreadyDisabled());
    }
    return true;
  }
}
