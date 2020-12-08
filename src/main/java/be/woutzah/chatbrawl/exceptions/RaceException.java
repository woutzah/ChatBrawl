package be.woutzah.chatbrawl.exceptions;

import be.woutzah.chatbrawl.ChatBrawl;
import org.bukkit.ChatColor;

public class RaceException extends Exception {

    private static boolean isBadConfig;

    public RaceException(String message) {
        super(message);
        isBadConfig = true;
    }

    public static void handleConfigException(ChatBrawl plugin, RaceException e) {
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ChatBrawl Config Error] " + e.getMessage());
    }

    public static boolean isBadConfig() {
        return isBadConfig;
    }

    public static void resetIsBadConfig(){
        isBadConfig = false;
    }
}
