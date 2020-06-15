package com.qthekan.qlol.rank;

import com.google.gson.Gson;
import com.qthekan.qlol.MainActivity;
import com.qthekan.qlol.summoner.SummonerManager;
import com.qthekan.util.qlog;
import com.qthekan.util.qUtil;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class RankManager
{
    /**
     * 고승률 유저 정보만 저장
     */
    public static ArrayList<RankModel> mRankerList = new ArrayList<>();


    public static String getRankers(String tier, int winRate)
    {
        mRankerList.clear();
        String [] divisions = {"I", "II", "III", "IV"};

        String ret = "";
        for(String division : divisions )
        {
            if("CHALLENGER".equalsIgnoreCase(tier) || "GRANDMASTER".equalsIgnoreCase(tier) || "MASTER".equalsIgnoreCase(tier))
            {
                if("II".equalsIgnoreCase(division) || "III".equalsIgnoreCase(division) || "IV".equalsIgnoreCase(division))
                {
                    continue;
                }
            }

            String tmp = getRankers(tier, division, winRate);
            if(tmp == null)
            {
                continue;
            }
            ret += "\n\n" +tier+"-"+division+ "\n" + tmp;
        }
        return ret;
    }


    private static String getRankers(String tier, String division, int winRate)
    {
        //String url = "https://kr.api.riotgames.com/lol/league-exp/v4/entries/RANKED_SOLO_5x5/CHALLENGER/I?page=1";
        String url = "https://kr.api.riotgames.com/lol/league-exp/v4/entries/RANKED_SOLO_5x5/" + tier + "/" + division;
        String response = qUtil.sendHttpsGetRequestAsync(url);

        try {
            Gson gson = new Gson();
            RankModel[] array = gson.fromJson(response, RankModel[].class);

            String ret = "";
            for(RankModel user : array)
            {
                if(user.getWinRate() > winRate)
                {
                    ret = ret + user.getSummonerName() + "=" + user.getWinRate() + "\n";
                    mRankerList.add(user);
                    SummonerManager.getSummonerInfo(user.getSummonerName(), user.getWinRate());
                }
            }
            return ret;
        }
        catch (Exception e) {
            qlog.e("nok : " + tier + "-" + division + " " + winRate, e);
            return null;
        }
    }

}
