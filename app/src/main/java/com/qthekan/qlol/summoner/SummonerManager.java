package com.qthekan.qlol.summoner;

import android.app.Activity;
import android.text.util.Linkify;

import com.google.gson.Gson;
import com.qthekan.qlol.MainActivity;
import com.qthekan.util.qlog;
import com.qthekan.util.qUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SummonerManager 
{
    public String mRegion = "";

    String mSummonerInfo = "";


    /**
     * 전적 검색을 하기위한 소환사 계정 정보 조회
     */
    public void getSummonerInfo(String region, final String summonerName, int winRate)
    {
        String url = "https://"+region+".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName;
        String response = qUtil.sendHttpsGetRequestAsync(url);
        
        SummonerModel summoner = new SummonerModel();
        Gson gson = new Gson();
        summoner = gson.fromJson(response, SummonerModel.class);

        // 삭제되거나 닉변을 해서 검색이 안됨
        if(summoner.getName() == null)
        {
            qlog.e("user not exist: " + summonerName);
            return;
        }

        mSummonerInfo = "\n" + MainActivity.mSEARCH_KEY_WORD + "\n" + summonerName + " = " + winRate + " %\n";
        MainActivity.mIns.printTextView(mSummonerInfo);

        switch (region)
        {
            case "euw1":
                mRegion = "euw";
                break;
            case "na1":
                mRegion = "na";
                break;
            default:
                mRegion = "www";
                break;
        }

        MainActivity.mIns.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Pattern pattern = Pattern.compile(summonerName);

                Linkify.TransformFilter transform = new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "https://"+mRegion+".op.gg/summoner/userName=" + url;
                    }
                };

                // scheme 에 이동할 url 을 넣으면 전부 소문자로 치환되는 문제발생
                // 그래서 transformUrl() 안에서 이동할 url 을 만들고 반환하도록 구현.
                Linkify.addLinks(MainActivity.mTvSearch, pattern, "", null, transform);
            }
        });

        summonerMatchList(region, summoner.accountId, winRate);
    }
    

    //String mMatchInfo;
    private void summonerMatchList(String region, String accountId, int winRate)
    {
        String url = "https://"+region+".api.riotgames.com/lol/match/v4/matchlists/by-account/" + accountId;
        String response = qUtil.sendHttpsGetRequestAsync(url);

        Gson gson = new Gson();
        MatchListModel matchList = gson.fromJson(response, MatchListModel.class);

        if(matchList.matches == null)
        {
            qlog.e("match list not exist: " + accountId);
            return;
        }

        int i = 0;
        for(MatchListModel.MatchReference m : matchList.matches)
        {
            if(i++ > 9)
            {
                break;
            }

            String matchInfo = "season:"+m.season + ", champion:"+m.champion + ", lane:"+m.lane + ", dtime:" + qUtil.timestampToDtime(m.timestamp) + "\n";
            MainActivity.mIns.printTextView(matchInfo);
        }
    }
}
