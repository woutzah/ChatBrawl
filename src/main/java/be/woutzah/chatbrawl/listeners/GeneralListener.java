package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class GeneralListener implements Listener {

    private ChatBrawl plugin;
    private boolean notifyOnLogin;

    public GeneralListener(ChatBrawl plugin) {
        this.plugin = plugin;
        this.notifyOnLogin = plugin.getConfig().getBoolean("notify-event-login");
    }

    @EventHandler
    public void notifyOnLogin(PlayerLoginEvent event) {
        if (notifyOnLogin) {
            if (!plugin.getPrinter().getCurrentRunningRaceInfo().isEmpty()) {
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () ->
                                event.getPlayer().sendMessage(plugin.getPrinter().getCurrentRunningRaceInfo()),
                        100);
            }
        }
    }
}
