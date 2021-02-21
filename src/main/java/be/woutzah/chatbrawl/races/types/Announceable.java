package be.woutzah.chatbrawl.races.types;

import org.bukkit.entity.Player;

public interface Announceable {
    void announceStart(boolean center);
    void sendStart(Player player);
    void announceEnd();
    void announceWinner(boolean center, Player player);
    void showActionbar();
    void stopActionBar();
    String replacePlaceholders(String message);
}
