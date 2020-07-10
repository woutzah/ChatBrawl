package be.woutzah.chatbrawl.rewards;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.utils.Chance;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void executeRandomCommand(HashMap<List<String>, Integer> rewardsMap, Player player) {
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
            }
        }
    }

    public void calculateSumAndFillChanceList(HashMap<List<String>, Integer> rewardsMap) {
        for (List<String> commands : rewardsMap.keySet()) {
            Chance chance = new Chance(sum + rewardsMap.get(commands), sum, commands);
            sum += rewardsMap.get(commands);
            chanceList.add(chance);
        }
    }
}
