package be.woutzah.chatbrawl.commands;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.RaceCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Enable implements CommandExecutor {

    private final Printer printer;
    private final RaceCreator raceCreator;

    public Enable(ChatBrawl plugin) {
        this.printer = plugin.getPrinter();
        this.raceCreator = plugin.getRaceCreator();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!raceCreator.getRacesEnabled()) {
            raceCreator.setRacesEnabled(true);
            raceCreator.createRaces();
            sender.sendMessage(printer.getEnabled());
        } else {
            sender.sendMessage(printer.getAlreadyEnabled());
        }
        return true;
    }
}
