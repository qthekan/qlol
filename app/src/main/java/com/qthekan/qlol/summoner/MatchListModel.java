package com.qthekan.qlol.summoner;

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
    }
}
