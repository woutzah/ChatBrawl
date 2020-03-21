package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class CraftRaceListener implements Listener {

    private ChatBrawl plugin;

    public CraftRaceListener(ChatBrawl plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void checkCraftedItems(CraftItemEvent event) {
        if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.craft)) {
            if (!(event.getWhoClicked().getInventory().firstEmpty() == -1)) {
                if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                    ItemStack craftedItemStack;
                    if (event.getClick().isShiftClick()) {
                        int amount = Arrays.stream(event.getInventory().getMatrix())
                                .filter(Objects::nonNull)
                                .mapToInt(ItemStack::getAmount)
                                .min()
                                .orElse(0);
                        craftedItemStack = new ItemStack(Objects.requireNonNull(event.getCurrentItem()).getType(), amount * event.getCurrentItem().getAmount());
                    } else {
                        craftedItemStack = event.getCurrentItem();
                    }
                    UUID uuid = event.getWhoClicked().getUniqueId();
                    Player player = (Player) event.getWhoClicked();
                    assert craftedItemStack != null;
                    if (craftedItemStack.getType().equals(plugin.getCraftRace().getCurrentItemStack().getType())) {
                        if (plugin.getCraftRace().getPlayerScores().containsKey(uuid)) {
                            int currentAmount = plugin.getCraftRace().getPlayerScores().get(uuid);
                            currentAmount += craftedItemStack.getAmount();
                            plugin.getCraftRace().getPlayerScores().put(uuid, currentAmount);
                            if (plugin.getCraftRace().getPlayerScores().get(uuid)
                                    >= plugin.getCraftRace().getCurrentItemStack().getAmount()) {
                                plugin
                                        .getServer()
                                        .broadcastMessage(
                                                plugin.getPrinter().getAnnounceCraftWinner(player));
                                if (!plugin.getPrinter().getPersonalCraftWinner().isEmpty()) {
                                    player.sendMessage(plugin.getPrinter().getPersonalCraftWinner());
                                }
                                plugin.getChatrace().shootFireWorkIfEnabled(player);
                                plugin
                                        .getCraftRace()
                                        .getRewardRandomizer()
                                        .executeRandomCommand(plugin.getCraftRace().getCommandRewardsMap(), player);
                                plugin.getRaceCreator().getCraftRaceTask().cancel();
                                plugin.getRaceCreator().setCurrentRunningRace(RaceType.none);
                                plugin.getCraftRace().removeOnlinePlayers();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void addPlayerCraftRace(PlayerJoinEvent event) {
        if (plugin.getRaceCreator().getCurrentRunningRace().equals(RaceType.craft)) {
            if (!plugin.getCraftRace().getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
                plugin.getCraftRace().getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
            }
        }
    }
}
