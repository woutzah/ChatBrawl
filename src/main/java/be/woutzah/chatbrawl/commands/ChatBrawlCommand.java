package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.exceptions.RaceException;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatBrawlCommand implements CommandExecutor, TabCompleter {

    private ChatBrawl plugin;
    private Printer printer;
    private Disable disable;
    private Enable enable;
    private StopRace stopRace;
    private StartRace startRace;
    private Help help;
    private Discord discord;
    private Current current;
    private Reload reload;

    public ChatBrawlCommand(ChatBrawl plugin) {
        this.plugin = plugin;
        this.printer = plugin.getPrinter();
        this.disable = new Disable(plugin);
        this.enable = new Enable(plugin);
        this.stopRace = new StopRace(plugin);
        this.startRace = new StartRace(plugin);
        this.help = new Help(plugin);
        this.discord = new Discord(plugin);
        this.current = new Current(plugin);
        this.reload = new Reload(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(printer.getPrefix()
                    + ChatColor.WHITE
                    + "Running ChatBrawl V" + ChatColor.AQUA +
                    plugin.getDescription().getVersion() + ChatColor.WHITE
                    + " created by Woutzah.");
            return true;
        }
        if (RaceException.isBadConfig() && !args[0].toLowerCase().equals("reload")) {
            sender.sendMessage(printer.getPrefix()
                    + ChatColor.RED
                    + "Error in the config. Check startup log for details!");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "disable":
                if (sender.hasPermission("cb.disable") || sender.hasPermission("cb.admin")) {
                    disable.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    sender.sendMessage(printer.getNoPermission());
                }
                break;
            case "enable":
                if (sender.hasPermission("cb.enable") || sender.hasPermission("cb.admin")) {
                    enable.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    sender.sendMessage(printer.getNoPermission());
                }
                break;
            case "stop":
                if (args.length == 1) {
                    sender.sendMessage(printer.getStopRaceUsage());
                    break;
                }
                if (args[1].equalsIgnoreCase("race")) {
                    if (sender.hasPermission("cb.stop") || sender.hasPermission("cb.admin")) {
                        stopRace.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
                        break;
                    } else {
                        sender.sendMessage(printer.getNoPermission());
                    }
                } else {
                    sender.sendMessage(printer.getStopRaceUsage());
                }
                break;
            case "start":
                if (args.length == 1) {
                    sender.sendMessage(printer.getStartRaceUsage());
                    break;
                }
                if (sender.hasPermission("cb.start") || sender.hasPermission("cb.admin")) {
                    startRace.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
                    break;
                } else {
                    sender.sendMessage(printer.getNoPermission());
                }
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
                sender.sendMessage(printer.getSubcommandNotExist());
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("cb")) {
            if (sender.hasPermission("cb.default")) {
                List<String> suggestionList = new ArrayList<>();
                List<String> tempList = new ArrayList<>();
                switch (args.length) {
                    case 1:
                        tempList.add("help");
                        tempList.add("discord");
                        if (sender.hasPermission("cb.current") ||
                                sender.hasPermission("cb.admin")) {
                            tempList.add("current");
                        }
                        if (sender.hasPermission("cb.disable") ||
                                sender.hasPermission("cb.admin")) {
                            tempList.add("disable");
                        }
                        if (sender.hasPermission("cb.enable") ||
                                sender.hasPermission("cb.admin")) {
                            tempList.add("enable");
                        }
                        if (sender.hasPermission("cb.stop") ||
                                sender.hasPermission("cb.admin")) {
                            tempList.add("stop");
                        }
                        if (sender.hasPermission("cb.start") ||
                                sender.hasPermission("cb.admin")) {
                            tempList.add("start");
                        }
                        if (sender.hasPermission("cb.reload") ||
                                sender.hasPermission("cb.admin")) {
                            tempList.add("reload");
                        }
                        tempList.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                                .forEach(suggestionList::add);
                        Collections.sort(suggestionList);
                        return suggestionList;
                    case 2:
                        switch (args[0]) {
                            case "stop":
                                if (sender.hasPermission("cb.stop") ||
                                        sender.hasPermission("cb.admin")) {
                                    tempList.add("race");
                                }
                                tempList.stream().filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                                        .forEach(suggestionList::add);
                                Collections.sort(suggestionList);
                                return suggestionList;
                            case "start":
                                if (sender.hasPermission("cb.start") ||
                                        sender.hasPermission("cb.admin")) {
                                    Arrays.stream(RaceType.values()).filter(r -> r != RaceType.NONE)
                                            .forEach(r -> tempList.add(r.toString().toLowerCase()));
                                }
                                tempList.stream().filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                                        .forEach(suggestionList::add);
                                Collections.sort(suggestionList);
                                return suggestionList;
                        }
                }
            }
        }
        return null;
    }
}
