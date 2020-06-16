package com.qthekan.qlol;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qthekan.qlol.rank.RankManager;
import com.qthekan.util.qUtil;
import com.qthekan.util.qlog;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    private Button mBtnSearch;
    private Button mBtnClear;
    private Button mBtnNext;
    private Button mBtnPrev;
    public static TextView mTvSearch;
    private EditText mEtWinRate;
    private Spinner mSpiTier;
    private Spinner mSpiRegion;

    public static final String mSEARCH_KEY_WORD = "==============================";

    public static AssetManager mAsset;

    public static MainActivity mIns;


    public void printTextView(final String log)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                qlog.e("log : " + log);
                mTvSearch.append(log);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIns = this;

        //=====================================================================
        // Runtime Exception 에 의해 thread 가 종료될 경우
        // 로그를 출력하도록 설정한다.
        //=====================================================================
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                Log.e("main", "uncaughtException() thread dead: " + thread.getName() + "\n" + e.toString(), (Exception) e);
                // 20180307 main thread 가 죽은 경우 프로세스 재기동이 되지 않아서 추가함.
                System.exit(0);
            }
        });

        mBtnSearch = findViewById(R.id.btnSearch);
        mBtnClear = findViewById(R.id.btnClear);
        mBtnNext = findViewById(R.id.btnNext);
        mBtnPrev = findViewById(R.id.btnPrev);
        mTvSearch = findViewById(R.id.tvSearch);
        mEtWinRate = findViewById(R.id.etWinRate);
        mSpiTier = findViewById(R.id.spiTier);
        mSpiRegion = findViewById(R.id.spiRegion);

        mAsset = getAssets();

        checkPermission();

        getChampInfo();
    }


    public HashMap<Integer, String> mChampionInfo = new HashMap<>();
    private void getChampInfo()
    {
        try {
            mChampionInfo.clear();
            AssetManager am = getAssets();

            InputStream is = am.open("champion.json");
            Reader reader = new InputStreamReader(is);

            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(reader);

            JsonObject data = (JsonObject) object.get("data");
            for(String tmp : data.keySet())
            {
                //qlog.e("tmp: " + tmp);
                JsonObject champion = (JsonObject) data.get(tmp);

                int key = champion.get("key").getAsInt();
                String name = champion.get("name").getAsString();
                qlog.e("key: " + key + ", name: " + name);

                mChampionInfo.put(key, name);
            }
        } catch (Exception e) {
            qlog.e("", e);
        }

    }


    //=========================================================================
    // 버튼 클릭 이벤트 처리
    //=========================================================================
    public void onSearch(View v)
    {
        mTvSearch.setText("");
        mLastIndex = 0;

        String region = mSpiRegion.getSelectedItem().toString();
        String tier = mSpiTier.getSelectedItem().toString();
        int winRate = qUtil.parseInt(mEtWinRate.getText().toString().replace(" ", ""), 70);

        RankManager rank = new RankManager(region, tier, winRate);
        rank.start();
    }


    public void onClear(View v)
    {
        mTvSearch.setText("");
    }


    private int mLastIndex = 0;
    public void onNext(View v)
    {
        String totalString = mTvSearch.getText().toString();
        if(mLastIndex < 0)
        {
            mLastIndex = 0;
            return;
        }

        qlog.e("mLastIndex1: " + mLastIndex);
        mTvSearch.bringPointIntoView(mLastIndex);
        int index = totalString.substring(mLastIndex++).indexOf(mSEARCH_KEY_WORD);
        mLastIndex += index;
        qlog.e("mLastIndex2: " + mLastIndex);

        if(mLastIndex + 200 > totalString.length() - 1)
        {
            mTvSearch.bringPointIntoView(totalString.length()-1);
        }
        else
        {
            mTvSearch.bringPointIntoView(mLastIndex + 200);
        }
    }


    public void onPrev(View v)
    {
        String totalString = mTvSearch.getText().toString();
        if(mLastIndex < 0)
        {
            mLastIndex = 0;
            return;
        }

        qlog.e("mLastIndex3: " + mLastIndex);
        mTvSearch.bringPointIntoView(mLastIndex);
        int index = totalString.substring(0, mLastIndex).lastIndexOf(mSEARCH_KEY_WORD);
        mLastIndex = index;
        qlog.e("mLastIndex4: " + mLastIndex);
        if(mLastIndex < 0)
        {
            mLastIndex = 0;
        }
        mTvSearch.bringPointIntoView(mLastIndex + 1);
    }


    //=========================================================================
    // check permission
    //=========================================================================
    private final int mPERMISSION_CODE_FINE_LOCATION = 1;
    private final int mPERMISSION_CODE_COARSE_LOCATION = 2;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 3;
    private final int mPERMISSION_CODE_EXTERNAL_STROAGE_WRITE = 4;


    private int checkPermission()
    {
        //===========================================================
        // check permission: external storage
        //===========================================================
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            qUtil.showDialog(this, "Need Permission", "For save favorite as file", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.mIns, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, mPERMISSION_CODE_EXTERNAL_STROAGE_WRITE);
                }
            }, null);
        }

        return 0;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case mPERMISSION_CODE_EXTERNAL_STROAGE_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "permission on EXTERNAL_STROAGE_WRITE");
                    qUtil.writeFile(qlog.mLogFileName, "");
                } else {
                    Log.d("", "permission off EXTERNAL_STROAGE_WRITE");
                }
                break;
            }
        }
    }
}
