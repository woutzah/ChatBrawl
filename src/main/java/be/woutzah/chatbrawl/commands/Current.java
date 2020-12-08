package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Current implements CommandExecutor {

    private final Printer printer;
    private final ChatBrawl plugin;

    public Current(ChatBrawl plugin) {
        this.printer = plugin.getPrinter();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!plugin.getPrinter().getCurrentRunningRaceInfo().isEmpty()) {
            sender.sendMessage(printer.getCurrentRunningRaceInfo());
            return true;
        }
        return true;
    }
}
