package be.woutzah.chatbrawl.utils;

import be.woutzah.chatbrawl.races.RaceType;

import java.util.List;

public class Chance {

  private int upperLimit;
  private int lowerLimit;
  private List<String> commands;
  private String broadcastString;
  private String titleString;
  private String subtitleString;
  private RaceType raceType;

  public Chance(int upperlimit, int lowerLimit, List<String> commands, String broadcastString, String titleString,String subtitleString) {
    this.upperLimit = upperlimit;
    this.lowerLimit = lowerLimit;
    this.commands = commands;
    this.broadcastString = broadcastString;
    this.titleString = titleString;
    this.subtitleString = subtitleString;
  }

  public Chance(int upperLimit, int lowerLimit, RaceType raceType) {
    this.upperLimit = upperLimit;
    this.lowerLimit = lowerLimit;
    this.raceType = raceType;
  }

  public int getUpperLimit() {
    return upperLimit;
  }

  public int getLowerLimit() {
    return lowerLimit;
  }

  public RaceType getRaceType() {
    return raceType;
  }

  public List<String> getCommands() {
    return commands;
  }

  public String getBroadcastString() {
    return broadcastString;
  }

  public String getTitleString() {
    return titleString;
  }

  public String getSubtitleString() {
    return subtitleString;
  }
}
