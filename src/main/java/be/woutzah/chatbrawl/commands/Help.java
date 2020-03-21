package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Help implements CommandExecutor {

  private ChatBrawl plugin;

  public Help(ChatBrawl plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    sender.sendMessage(plugin.getPrinter().getHelpMenu());
    return true;
  }
}
