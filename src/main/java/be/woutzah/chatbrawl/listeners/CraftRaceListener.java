package be.woutzah.chatbrawl.listeners;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.messages.Printer;
import be.woutzah.chatbrawl.races.types.CraftRace;
import be.woutzah.chatbrawl.races.RaceCreator;
import be.woutzah.chatbrawl.races.RaceType;
import be.woutzah.chatbrawl.rewards.RewardRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

    private RaceCreator raceCreator;
    private CraftRace craftRace;
    private Printer printer;
    private RewardRandomizer rewardRandomizer;
    private ChatBrawl plugin;

    public CraftRaceListener(ChatBrawl plugin) {
        this.raceCreator = plugin.getRaceCreator();
        this.craftRace = plugin.getCraftRace();
        this.printer = plugin.getPrinter();
        this.rewardRandomizer = craftRace.getRewardRandomizer();
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void checkCraftedItems(CraftItemEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.craft)) {
            if (plugin.getDisabledWorldsList().contains(event.getWhoClicked().getLocation().getWorld().getName())){
                return;
            }
            if (!plugin.getAllowCreative()) {
                if (event.getWhoClicked().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
            }
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
                    assert craftedItemStack != null;
                    if (craftedItemStack.getType().equals(craftRace.getCurrentItemStack().getType())) {
                        UUID uuid = event.getWhoClicked().getUniqueId();
                        if (craftRace.getPlayerScores().containsKey(uuid)) {
                            int currentAmount = craftRace.getPlayerScores().get(uuid);
                            currentAmount += craftedItemStack.getAmount();
                            craftRace.getPlayerScores().put(uuid, currentAmount);
                            if (craftRace.getPlayerScores().get(uuid)
                                    >= craftRace.getCurrentItemStack().getAmount()) {
                                Player player = (Player) event.getWhoClicked();
                                Bukkit.broadcast(printer.getAnnounceCraftWinner(player), "cb.default");
                                if (!printer.getPersonalCraftWinner().isEmpty()) {
                                    player.sendMessage(printer.getPersonalCraftWinner());
                                }
                                if (plugin.isSoundEnabled()){
                                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),plugin.getEndSound(),1.0F, 8.0F) );
                                }
                                craftRace.shootFireWorkIfEnabled(player);
                                rewardRandomizer.executeRandomCommand(craftRace.getCommandRewardsMap(), player);
                                raceCreator.getCraftRaceTask().cancel();
                                raceCreator.setCurrentRunningRace(RaceType.none);
                                craftRace.removeOnlinePlayers();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void addPlayerCraftRace(PlayerJoinEvent event) {
        if (raceCreator.getCurrentRunningRace().equals(RaceType.craft)) {
            if (!craftRace.getPlayerScores().containsKey(event.getPlayer().getUniqueId())) {
                craftRace.getPlayerScores().put(event.getPlayer().getUniqueId(), 0);
            }
        }
    }
}
