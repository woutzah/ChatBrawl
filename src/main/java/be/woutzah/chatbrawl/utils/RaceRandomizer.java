package be.woutzah.chatbrawl.utils;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaceRandomizer {

    private final ChatBrawl plugin;
    private final List<Chance> chanceList;
    private final Random random;
    private int sum;

    public RaceRandomizer(ChatBrawl plugin) {
        this.plugin = plugin;
        this.chanceList = new ArrayList<>();
        this.sum = 0;
        this.random = new Random();
        putChancesInList();
    }

    public void putChancesInList() {
        if (plugin.getChatrace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getChatrace().getChance(), sum, RaceType.CHAT));
            sum += plugin.getChatrace().getChance();
        }

        if (plugin.getBlockRace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getBlockRace().getChance(), sum, RaceType.BLOCK));
            sum += plugin.getBlockRace().getChance();
        }

        if (plugin.getFishRace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getFishRace().getChance(), sum, RaceType.FISH));
            sum += plugin.getFishRace().getChance();
        }

        if (plugin.getHuntRace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getHuntRace().getChance(), sum, RaceType.HUNT));
            sum += plugin.getHuntRace().getChance();
        }

        if (plugin.getCraftRace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getCraftRace().getChance(), sum, RaceType.CRAFT));
            sum += plugin.getCraftRace().getChance();
        }

        if (plugin.getQuizRace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getQuizRace().getChance(), sum, RaceType.QUIZ));
            sum += plugin.getQuizRace().getChance();
        }

        if (plugin.getFoodRace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getFoodRace().getChance(), sum, RaceType.FOOD));
            sum += plugin.getFoodRace().getChance();
        }

        if (plugin.getScrambleRace().isEnabled()) {
            chanceList.add(new Chance(sum + plugin.getScrambleRace().getChance(), sum, RaceType.SCRAMBLE));
            sum += plugin.getScrambleRace().getChance();
        }
    }

    public RaceType getRandomRace() {
        final int index = this.random.nextInt(this.sum);

        for (Chance chance : this.chanceList) {
            if (chance.getLowerLimit() <= index && chance.getUpperLimit() > index) {
                return chance.getRaceType();
            }
        }

        return RaceType.NONE;
    }

}
