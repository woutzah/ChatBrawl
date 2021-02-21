package be.woutzah.chatbrawl.commands.subcommands;

import be.woutzah.chatbrawl.util.Printer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class DiscordCommand extends SubCommand {
    public DiscordCommand() {
        super("discord", "none", new ArrayList<>(), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Printer.sendDiscordMessage(sender);
    }
}
