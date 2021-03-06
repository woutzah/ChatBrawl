package be.woutzah.chatbrawl.races.types;

import be.woutzah.chatbrawl.contestants.ContestantsManager;
import be.woutzah.chatbrawl.leaderboard.LeaderboardManager;
import be.woutzah.chatbrawl.races.RaceManager;
import be.woutzah.chatbrawl.rewards.RewardManager;
import be.woutzah.chatbrawl.settings.SettingManager;
import be.woutzah.chatbrawl.time.TimeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public abstract class ContestantRace extends Race {

    protected ContestantsManager contestantsManager;

    public ContestantRace(RaceType type, RaceManager raceManager, SettingManager settingManager,
                          RewardManager rewardManager, TimeManager timeManager,
                          ContestantsManager contestantsManager, LeaderboardManager leaderboardManager) {
        super(type, raceManager, settingManager, rewardManager, timeManager,leaderboardManager);
        this.contestantsManager = contestantsManager;
    }

    @Override
    public void beforeRaceStart() {
        contestantsManager.fillOnlinePlayers();
    }

    @Override
    public void afterRaceEnd() {
        super.afterRaceEnd();
        contestantsManager.removeOnlinePlayers();
    }

    @EventHandler
    public void addContestant(PlayerJoinEvent e) {
        if (!isActive()) return;
        contestantsManager.addContestant(e.getPlayer().getUniqueId());
    }
}
