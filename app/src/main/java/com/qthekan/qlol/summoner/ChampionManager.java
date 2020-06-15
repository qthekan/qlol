package com.qthekan.qlol.summoner;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qthekan.qlol.MainActivity;
import com.qthekan.util.qlog;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class ChampionManager
{
    private static HashMap<Integer, String> mChampList = new HashMap<>();

    public static String getName(int id)
    {
        if(mChampList.size() == 0)
        {
            init();
        }

        return "";
    }


    private static void init()
    {
        AssetManager am = MainActivity.mAsset;

        try {
            InputStream is = am.open("Champions.json");
            Reader r = new InputStreamReader(is);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(is.toString());
        }
        catch (Exception e) {
            qlog.e("", e);
        }
    }
}
