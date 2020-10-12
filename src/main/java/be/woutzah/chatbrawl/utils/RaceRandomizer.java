package be.woutzah.chatbrawl.utils;

import be.woutzah.chatbrawl.ChatBrawl;
import be.woutzah.chatbrawl.races.RaceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaceRandomizer {

  private ChatBrawl plugin;
  private List<Chance> chanceList;
  private int sum;
  private Random random;

  public RaceRandomizer(ChatBrawl plugin) {
    this.plugin = plugin;
    this.chanceList = new ArrayList<>();
    this.sum = 0;
    this.random = new Random();
    putChancesInList();
  }

  public void putChancesInList() {
    if (plugin.getChatrace().isEnabled()) {
      Chance chatChance = new Chance(sum + plugin.getChatrace().getChance(), sum, RaceType.CHAT);
      sum += plugin.getChatrace().getChance();
      chanceList.add(chatChance);
    }
    if (plugin.getBlockRace().isEnabled()) {
      Chance blockChance = new Chance(sum + plugin.getBlockRace().getChance(), sum, RaceType.BLOCK);
      sum += plugin.getBlockRace().getChance();
      chanceList.add(blockChance);
    }
    if (plugin.getFishRace().isEnabled()) {
      Chance fishChance = new Chance(sum + plugin.getFishRace().getChance(), sum, RaceType.FISH);
      sum += plugin.getFishRace().getChance();
      chanceList.add(fishChance);
    }
    if (plugin.getHuntRace().isEnabled()) {
      Chance huntChance = new Chance(sum + plugin.getHuntRace().getChance(), sum, RaceType.HUNT);
      sum += plugin.getHuntRace().getChance();
      chanceList.add(huntChance);
    }
    if (plugin.getCraftRace().isEnabled()) {
      Chance craftChance = new Chance(sum + plugin.getCraftRace().getChance(), sum, RaceType.CRAFT);
      sum += plugin.getCraftRace().getChance();
      chanceList.add(craftChance);
    }
    if (plugin.getQuizRace().isEnabled()) {
      Chance quizChance = new Chance(sum + plugin.getQuizRace().getChance(), sum, RaceType.QUIZ);
      sum += plugin.getQuizRace().getChance();
      chanceList.add(quizChance);
    }
    if (plugin.getFoodRace().isEnabled()) {
      Chance foodChance = new Chance(sum + plugin.getFoodRace().getChance(), sum, RaceType.FOOD);
      sum += plugin.getFoodRace().getChance();
      chanceList.add(foodChance);
    }
    if (plugin.getScrambleRace().isEnabled()) {
      Chance scrambleChance = new Chance(sum + plugin.getScrambleRace().getChance(), sum, RaceType.SCRAMBLE);
      sum += plugin.getScrambleRace().getChance();
      chanceList.add(scrambleChance);
    }
  }

  public RaceType getRandomRace() {
    int index = this.random.nextInt(this.sum);
    for (Chance chance : this.chanceList) {
      if (chance.getLowerLimit() <= index && chance.getUpperLimit() > index) {
        return chance.getRaceType();
      }
    }
    return RaceType.NONE;
  }
}
