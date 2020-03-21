package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Disable implements CommandExecutor {

  private ChatBrawl plugin;

  public Disable(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (plugin.getRaceCreator().getRacesEnabled()) {
      plugin.getRaceCreator().setRacesEnabled(false);
      plugin.getRaceCreator().getRaceCreationTask().cancel();
      switch (plugin.getRaceCreator().getCurrentRunningRace()) {
        case chat:
          plugin.getRaceCreator().getChatRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          break;
        case block:
          plugin.getRaceCreator().getBlockRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          break;
        case fish:
          plugin.getRaceCreator().getFishRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          break;
        case hunt:
          plugin.getRaceCreator().getHuntRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          break;
        case craft:
          plugin.getRaceCreator().getCraftRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          break;
        case quiz:
          plugin.getRaceCreator().getQuizRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          break;
        case food:
          plugin.getRaceCreator().getFoodRaceTask().cancel();
          plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
          break;
        default:
          break;
      }
      sender.sendMessage(
          plugin.getPrinter().getDisabled());
    } else {
      sender.sendMessage(
          plugin.getPrinter().getAlreadyDisabled());
    }
    return true;
  }
}
