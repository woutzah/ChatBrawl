package be.woutzah.chatbrawl.rewards;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.utils.Chance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RewardRandomizer {

    private ChatBrawl plugin;
    private List<Chance> chanceList;
    private int sum;
    private Random random;

    public RewardRandomizer(ChatBrawl plugin) {
        this.plugin = plugin;
        this.chanceList = new ArrayList<>();
        this.sum = 0;
        this.random = new Random();
    }

    public void executeRandomCommand(List<CommandReward> rewardsMap, Player player) {
        calculateSumAndFillChanceList(rewardsMap);
        int index = random.nextInt(sum);
        for (Chance chance : chanceList) {
            if (chance.getLowerLimit() <= index && chance.getUpperLimit() > index) {
                chance.getCommands().forEach(c -> Bukkit.getServer()
                        .getScheduler().runTaskLater(
                                plugin,
                                () -> Bukkit.dispatchCommand(
                                        Bukkit.getServer().getConsoleSender(),
                                        c.replace("{player}", player.getName())),
                                0));
                if (!chance.getBroadcastString().isEmpty()) {
                    Bukkit.broadcast(parseColorCodes(plugin.getConfig().getString("plugin-prefix") + chance.getBroadcastString().replace("{player}", player.getDisplayName())), "cb.default");
                }
                if(!chance.getTitleString().isEmpty()) {
                    player.sendTitle(parseColorCodes(chance.getTitleString()),parseColorCodes(chance.getSubtitleString()), 10, 70, 20);
                }
            }
        }
    }

    public void calculateSumAndFillChanceList(List<CommandReward> rewardsMap) {
        for (CommandReward commandReward : rewardsMap) {
            Chance chance = new Chance(sum + commandReward.getChance(), sum,
                    commandReward.getCommands(), commandReward.getBroadcastString(),commandReward.getTitleString(),
                    commandReward.getSubtitleString());
            sum += commandReward.getChance();
            chanceList.add(chance);
        }
    }

    private String parseColorCodes(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
