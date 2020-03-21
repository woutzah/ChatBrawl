package be.woutzah.chatbrawl.utils;

import be.woutzah.chatbrawl.races.RaceType;

public class Chance {

  private int upperLimit;
  private int lowerLimit;
  private String commandString;
  private RaceType raceType;

  public Chance(int upperlimit, int lowerLimit, String commandString) {
    this.upperLimit = upperlimit;
    this.lowerLimit = lowerLimit;
    this.commandString = commandString;
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

  public String getCommandString() {
    return commandString;
  }
}
