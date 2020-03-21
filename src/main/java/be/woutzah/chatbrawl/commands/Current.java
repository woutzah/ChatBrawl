package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Current implements CommandExecutor {

  private ChatBrawl plugin;

  public Current(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    sender.sendMessage(plugin.getPrinter().getCurrentRunningRaceInfo());
    return true;
  }
}
