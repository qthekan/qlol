package com.qthekan.qlol.summoner;

import android.text.util.Linkify;

import com.google.gson.Gson;
import com.qthekan.qlol.MainActivity;
import com.qthekan.util.qlog;
import com.qthekan.util.qUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SummonerManager 
{
    /**
     * 전적 검색을 하기위한 소환사 계정 정보 조회
     */
    public static void getSummonerInfo(String summonerName, int winRate)
    {
        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName;
        String response = qUtil.sendHttpsGetRequestAsync(url);
        
        SummonerModel summoner = new SummonerModel();
        Gson gson = new Gson();
        summoner = gson.fromJson(response, SummonerModel.class);
        qlog.i(summoner.toString());

        // 삭제되거나 닉변을 해서 검색이 안됨
       if(summoner.getName() == null)
        {
            qlog.e("user not exist: " + summonerName);
            return;
        }

        MainActivity.printTextView(MainActivity.mSEARCH_KEY_WORD + "\n" + summonerName + " = " + winRate + " %\n");
        Linkify.TransformFilter transform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "https://www.op.gg/summoner/userName=" + url;
            }
        };

        Pattern pattern = Pattern.compile(summonerName);
        Linkify.addLinks(MainActivity.mTvSearch, pattern, "", null, transform);

        summonerMatchList(summoner.accountId, winRate);
    }
    
    
    private static void summonerMatchList(String accountId, int winRate)
    {
        String url = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/" + accountId;
        String response = qUtil.sendHttpsGetRequestAsync(url);

        Gson gson = new Gson();
        MatchListModel matchList = gson.fromJson(response, MatchListModel.class);

        String ret = "";
        int i = 0;
        for(MatchListModel.MatchReference m : matchList.matches)
        {
            if(i++ > 9)
            {
                break;
            }
            String matchInfo = "season:"+m.season + ", champion:"+m.champion + ", lane:"+m.lane + ", dtime:" + qUtil.timestampToDtime(m.timestamp) + "\n";
            MainActivity.printTextView(matchInfo);
        }
    }
}
