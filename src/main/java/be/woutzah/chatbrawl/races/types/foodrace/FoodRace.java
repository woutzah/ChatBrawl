package be.woutzah.chatbrawl.races.types.foodrace;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.contestants.ContestantsManager;
import be.woutzah.chatbrawl.files.ConfigType;
import be.woutzah.chatbrawl.leaderboard.LeaderboardManager;
import be.woutzah.chatbrawl.leaderboard.LeaderboardStatistic;
import be.woutzah.chatbrawl.races.RaceManager;
import be.woutzah.chatbrawl.races.types.ContestantRace;
import be.woutzah.chatbrawl.races.types.RaceType;
import be.woutzah.chatbrawl.rewards.RewardManager;
import be.woutzah.chatbrawl.settings.GeneralSetting;
import be.woutzah.chatbrawl.settings.LanguageSetting;
import be.woutzah.chatbrawl.settings.SettingManager;
import be.woutzah.chatbrawl.settings.races.FoodRaceSetting;
import be.woutzah.chatbrawl.settings.races.RaceSetting;
import be.woutzah.chatbrawl.time.TimeManager;
import be.woutzah.chatbrawl.util.ErrorHandler;
import be.woutzah.chatbrawl.util.FireWorkUtil;
import be.woutzah.chatbrawl.util.Printer;
import com.meowj.langutils.lang.LanguageHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FoodRace extends ContestantRace {
    private final List<FoodEntry> foodEntryList;
    private FoodEntry foodEntry;

    public FoodRace(RaceManager raceManager, SettingManager settingManager,
                    RewardManager rewardManager, TimeManager timeManager,
                    ContestantsManager contestantsManager, LeaderboardManager leaderboardManager) {
        super(RaceType.FOOD, raceManager, settingManager, rewardManager, timeManager, contestantsManager, leaderboardManager);
        this.foodEntryList = new ArrayList<>();
        initFoodEntryList();
    }

    private void initFoodEntryList() {
        settingManager.getConfigSection(FoodRaceSetting.FOOD).getKeys(false).forEach(entry -> {
            Material material = null;
            try {
                material = Material.valueOf(settingManager.getString(ConfigType.FOODRACE, "food." + entry + ".food").toUpperCase());
            } catch (Exception exception) {
                ErrorHandler.error("foodrace: foodentry " + entry + " has an invalid material!");
            }
            int amount = settingManager.getInt(ConfigType.FOODRACE, "food." + entry + ".amount");
            List<Integer> rewardIds = settingManager.getIntegerList(ConfigType.FOODRACE, "food." + entry + ".rewards");
            foodEntryList.add(new FoodEntry(material, amount, rewardIds));
        });
    }

    public void initRandomFoodEntry() {
        foodEntry = foodEntryList.get(random.nextInt(foodEntryList.size()));
    }

    @Override
    public void announceStart(boolean center) {
        List<String> messageList = settingManager.getStringList(RaceType.FOOD, RaceSetting.LANGUAGE_START)
                .stream()
                .map(this::replacePlaceholders)
                .collect(Collectors.toList());
        if (center) {
            Printer.broadcast(Printer.centerMessage(messageList));
            return;
        }
        Printer.broadcast(messageList);
    }

    @Override
    public void sendStart(Player player) {
        List<String> messageList = settingManager.getStringList(RaceType.FOOD, RaceSetting.LANGUAGE_START)
                .stream()
                .map(this::replacePlaceholders)
                .collect(Collectors.toList());
        if (isCenterMessages()) {
            Printer.sendMessage(Printer.centerMessage(messageList), player);
            return;
        }
        Printer.sendMessage(messageList, player);
    }

    @EventHandler
    public void onFoodConsume(PlayerItemConsumeEvent e) {
        //do checks
        if (!isActive()) return;
        Player player = e.getPlayer();
        if (!raceManager.isCreativeAllowed()) {
            if (player.getGameMode() == GameMode.CREATIVE) return;
        }
        World world = player.getWorld();
        if (!raceManager.isWorldAllowed(world.toString())) return;
        ItemStack consumedItemstack = e.getItem();
        if (consumedItemstack.getType().equals(foodEntry.getMaterial())) {
            UUID uuid = player.getUniqueId();
            contestantsManager.addScore(uuid);
            if (contestantsManager.hasWon(uuid, foodEntry.getAmount())) {
                //when correct
                afterRaceEnd();
                if (isAnnounceEndEnabled()) announceWinner(isCenterMessages(), player);
                if (isFireWorkEnabled()) FireWorkUtil.shootFireWorkSync(player);
                this.raceTask.cancel();
                rewardManager.executeRandomRewardSync(foodEntry.getRewardIds(), player);
                if (settingManager.getBoolean(GeneralSetting.MYSQL_ENABLED)) {
                    leaderboardManager.addWin(new LeaderboardStatistic(player.getUniqueId(), type, timeManager.getTotalSeconds()));
                }
                Printer.sendMessage(getWinnerPersonal(), player);
                contestantsManager.removeOnlinePlayers();
            }
        }
    }

    @Override
    public void announceWinner(boolean center, Player player) {
        List<String> messageList = settingManager.getStringList(RaceType.FOOD, RaceSetting.LANGUAGE_WINNER)
                .stream()
                .map(this::replacePlaceholders)
                .map(s -> s.replace("<displayname>", player.getDisplayName()))
                .map(s -> s.replace("<player>", player.getName()))
                .map(s -> s.replace("<time>", timeManager.getTimeString()))
                .collect(Collectors.toList());
        if (center) {
            Printer.broadcast(Printer.centerMessage(messageList));
            return;
        }
        Printer.broadcast(messageList);
    }

    @Override
    public void showActionbar() {
        String message = replacePlaceholders(settingManager.getString(RaceType.FOOD, RaceSetting.LANGUAGE_ACTIONBAR));
        this.actionBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(p -> p.spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent(Printer.parseColor(message))));
            }
        }.runTaskTimer(ChatBrawl.getInstance(), 0, 20);
    }

    @Override
    public String replacePlaceholders(String message) {
        return message.replace("<food>", ChatBrawl.isLangUtilsIsEnabled() ?
                LanguageHelper.getItemName(new ItemStack(foodEntry.getMaterial()), settingManager.getString(LanguageSetting.LANG))
                : WordUtils.capitalizeFully(foodEntry.getMaterial().toString().replace("_", " ")))
                .replace("<amount>", String.valueOf(foodEntry.getAmount()));
    }

    @Override
    public void beforeRaceStart() {
        super.beforeRaceStart();
        initRandomFoodEntry();
        if (isAnnounceStartEnabled()) announceStart(isCenterMessages());
        if (isActionBarEnabled()) showActionbar();
    }
}
