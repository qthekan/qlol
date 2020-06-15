package com.qthekan.qlol;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.qthekan.qlol.rank.RankManager;
import com.qthekan.util.qUtil;
import com.qthekan.util.qlog;

public class MainActivity extends AppCompatActivity
{
    private Button mBtnSearch;
    private Button mBtnClear;
    private Button mBtnNext;
    private Button mBtnPrev;
    public static TextView mTvSearch;
    private EditText mEtWinRate;
    private Spinner mSpiTier;

    public static final String mSEARCH_KEY_WORD = "==============================";

    public static AssetManager mAsset;


    public static void printTextView(String log)
    {
        mTvSearch.append(log + "\n");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mAsset = getAssets();
    }


    //=========================================================================
    // 버튼 클릭 이벤트 처리
    //=========================================================================
    public void onSearch(View v)
    {
        mTvSearch.setText("");
        mLastIndex = 0;

        String tier = mSpiTier.getSelectedItem().toString();
        int winRate = qUtil.parseInt(mEtWinRate.getText().toString().replace(" ", ""), 70);
        String ret = RankManager.getRankers(tier, winRate);
        //mTvSearch.append(ret);
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
}
