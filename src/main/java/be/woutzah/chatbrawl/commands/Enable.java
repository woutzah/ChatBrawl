package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Enable implements CommandExecutor {

  private ChatBrawl plugin;

  public Enable(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!plugin.getRaceCreator().getRacesEnabled()) {
      plugin.getRaceCreator().setRacesEnabled(true);
      plugin.getRaceCreator().createRaces();
      sender.sendMessage(
          plugin.getPrinter().getEnabled());
    } else {
      sender.sendMessage(
          plugin.getPrinter().getAlreadyEnabled());
    }
    return true;
  }
}
