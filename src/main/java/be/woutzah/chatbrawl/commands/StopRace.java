package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopRace implements CommandExecutor {

  private ChatBrawl plugin;

  public StopRace(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.none)) {
      switch (plugin.getRaceCreator().getCurrentRunningRace()) {
        case chat:
          plugin.getRaceCreator().getChatRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          plugin
              .getServer()
              .broadcastMessage(
                  plugin.getPrinter().getStoppedRace(RaceType.chat));
          return true;
        case block:
          plugin.getRaceCreator().getBlockRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          plugin
              .getServer()
              .broadcastMessage(
                      plugin.getPrinter().getStoppedRace(RaceType.block));
          return true;
        case fish:
          plugin.getRaceCreator().getFishRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          plugin
              .getServer()
              .broadcastMessage(
                      plugin.getPrinter().getStoppedRace(RaceType.fish));
          return true;
        case hunt:
          plugin.getRaceCreator().getHuntRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          plugin
              .getServer()
              .broadcastMessage(
                      plugin.getPrinter().getStoppedRace(RaceType.hunt));
          return true;
        case craft:
          plugin.getRaceCreator().getCraftRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          plugin
                  .getServer()
                  .broadcastMessage(
                          plugin.getPrinter().getStoppedRace(RaceType.craft));
          return true;
        case quiz:
          plugin.getRaceCreator().getQuizRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          plugin
                  .getServer()
                  .broadcastMessage(
                          plugin.getPrinter().getStoppedRace(RaceType.quiz));
          return true;
        case food:
          plugin.getRaceCreator().getFoodRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          plugin
                  .getServer()
                  .broadcastMessage(
                          plugin.getPrinter().getStoppedRace(RaceType.food));
          return true;
      }
    } else {
      sender.sendMessage(
          plugin.getPrinter().getNoRaceRunning());
    }
    return true;
  }
}
