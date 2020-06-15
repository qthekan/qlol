package com.qthekan.qlol.rank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RankModel 
{
    public String leagueId;
    public String queueType;
    public String tier;
    public String rank;
    public String summonerId;
    public String summonerName;
    public int leaguePoints;
    public int wins;
    public int losses;
    public boolean veteran;
    public boolean inactive;
    public boolean freshBlood;
    public boolean hotStreak;


    public RankModel()
    {

    }
    
    public int getWinRate()
    {
        return wins * 100 / (wins + losses);
    }
}
