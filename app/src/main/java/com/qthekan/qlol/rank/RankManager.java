package com.qthekan.qlol.rank;

import com.google.gson.Gson;
import com.qthekan.qlol.summoner.SummonerManager;
import com.qthekan.util.qUtil;
import com.qthekan.util.qlog;


public class RankManager extends Thread
{
    String mRegion = "";
    String mTier;
    int mWinRate;


    public RankManager(String region, String tier, int winRate)
    {
        mRegion = region;
        mTier = tier;
        mWinRate = winRate;
    }


    @Override
    public void run()
    {
        String [] divisions = {"I", "II", "III", "IV"};

        for(String division : divisions )
        {
            if("CHALLENGER".equalsIgnoreCase(mTier) || "GRANDMASTER".equalsIgnoreCase(mTier) || "MASTER".equalsIgnoreCase(mTier))
            {
                if("II".equalsIgnoreCase(division) || "III".equalsIgnoreCase(division) || "IV".equalsIgnoreCase(division))
                {
                    continue;
                }
            }

            getRankers(mRegion, mTier, division, mWinRate);
        }
    }


    private void getRankers(String region, String tier, String division, int winRate)
    {
        //String url = "https://kr.api.riotgames.com/lol/league-exp/v4/entries/RANKED_SOLO_5x5/CHALLENGER/I?page=1";
        String url = "https://"+region+".api.riotgames.com/lol/league-exp/v4/entries/RANKED_SOLO_5x5/" + tier + "/" + division;
        qlog.e("url: " + url);
        //String response = qUtil.sendHttpsGetRequestAsync(url);
        String response = qUtil.sendHttpsGetRequest(url);

        try {
            Gson gson = new Gson();
            RankModel[] array = gson.fromJson(response, RankModel[].class);
            SummonerManager summoner = new SummonerManager();

            for(RankModel user : array)
            {
                if(user.getWinRate() > winRate)
                {
                    summoner.getSummonerInfo(region, user.getSummonerName(), user.getWinRate());
                }
            }
        }
        catch (Exception e) {
            qlog.e("nok : " + tier + "-" + division + " " + winRate, e);
        }
    }

}
