package com.qthekan.qlol.summoner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummonerModel 
{
    public String id;
    public String accountId;
    public String puuid;
    public String name;
    public int profileIconId;
    public long revisionDate;
    public long summonerLevel;
    
    
    public String toString()
    {
        return "name=" + name + ", summonerId=" + id + ", accountId=" + accountId;
    }
}
