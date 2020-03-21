package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class ChatBrawlCommand implements CommandExecutor {

  private ChatBrawl plugin;
  private Disable disable;
  private Enable enable;
  private StopRace stopRace;
  private Help help;
  private Discord discord;
  private Current current;
  private Reload reload;

  public ChatBrawlCommand(ChatBrawl plugin) {
    this.plugin = plugin;
    this.disable = new Disable(plugin);
    this.enable = new Enable(plugin);
    this.stopRace = new StopRace(plugin);
    this.help = new Help(plugin);
    this.discord = new Discord(plugin);
    this.current = new Current(plugin);
    this.reload = new Reload(plugin);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (RaceException.isBadConfig() && !args[0].toLowerCase().equals("reload")) {
      sender.sendMessage(
          plugin.getPrinter().getPrefix()
              + ChatColor.RED
              + "Error in the config. Check startup log for details!");
      return true;
    }
    if (args.length >= 1) {
      switch (args[0].toLowerCase()) {
        case "disable":
          if (sender.hasPermission("cb.disable") || sender.hasPermission("cb.admin")) {
            disable.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
          } else {
            sender.sendMessage(plugin.getPrinter().getNoPermission());
          }
          break;
        case "enable":
          if (sender.hasPermission("cb.enable") || sender.hasPermission("cb.admin")) {
            enable.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
          } else {
            sender.sendMessage(plugin.getPrinter().getNoPermission());
          }
          break;
        case "stop":
          if (args.length == 1) {
            sender.sendMessage(plugin.getPrinter().getStopRaceUsage());
            break;
          }
          if (args[1].equalsIgnoreCase("race")) {
            if (sender.hasPermission("cb.stop") || sender.hasPermission("cb.admin")) {
              stopRace.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
              break;
            } else {
              sender.sendMessage(plugin.getPrinter().getNoPermission());
            }
          } else {
            sender.sendMessage(plugin.getPrinter().getStopRaceUsage());
          }
          break;
        case "help":
          if (sender.hasPermission("cb.default") || sender.hasPermission("cb.admin")) {
            help.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
            break;
          }
        case "discord":
          if (sender.hasPermission("cb.default") || sender.hasPermission("cb.admin")) {
            discord.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
            break;
          }
        case "current":
          if (sender.hasPermission("cb.current") || sender.hasPermission("cb.admin")) {
            current.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
            break;
          }
        case "reload":
          if (sender.hasPermission("cb.reload") || sender.hasPermission("cb.admin")) {
            reload.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
            break;
          }
        default:
          sender.sendMessage(plugin.getPrinter().getSubcommandNotExist());
          break;
      }
    } else {
      sender.sendMessage(
          plugin.getPrinter().getPrefix()
              + ChatColor.WHITE
              + "Running ChatBrawl "
              + ChatColor.YELLOW
              + plugin.getDescription().getVersion()
              + ChatColor.WHITE
              + " created by Woutzah.");
    }
    return true;
  }
}
