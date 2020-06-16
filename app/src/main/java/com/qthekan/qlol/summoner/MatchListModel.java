package com.qthekan.qlol.summoner;

import com.qthekan.qlol.MainActivity;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchListModel {
    public int startIndex;
    public int totalGames;
    public int endIndex;
    public ArrayList<MatchReference> matches;

    @Getter
    @Setter
    public static class MatchReference {
        public long gameId;
        public String role;
        public int season;
        public String platformId;
        public int champion;
        public int queue;
        public String lane;
        public long timestamp;


        public String getChampName()
        {
            return MainActivity.mIns.mChampionInfo.get(champion);
        }


        public String getLine()
        {
            int max = 8;
            String ret = lane;
            for(int i = 0 ; i < max - lane.length() ; i++)
            {
                ret += " ";
            }

            return ret;
        }
    }



}
