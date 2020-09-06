package com.zerofocus.hermes;

// GK: Change this for androidX compatibility
// import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "MainActivity.VVV:";
    public static final int RQ_STORY = 1;

    private Engine[] _engines_list;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private ArrayList<Story> _stories;

    public MainActivity(){
        super();
        _engines_list = new Engine[]{
                new AdEngine(this)
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (Engine engine: _engines_list)
            engine.onCreate(savedInstanceState);

        _swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_id);
        _swipeRefreshLayout.setOnRefreshListener(this);


        onRefresh();
    }

    @Override
    public void onRefresh() {
        // SwipeRefreshLayout.OnRefreshListener
        Log.i(TAG,"onRefresh");
        String [] _rssURL_string_list = getResources().getStringArray(R.array.rss_list);
        new FetchRssGear(this).execute(_rssURL_string_list);
    }

    // This function is to be used by the FetchRssGear class
    public void setRefreshing(boolean flag){
        _swipeRefreshLayout.setRefreshing(flag);
    }

    public void updateStories(ArrayList<Story> stories){
        _stories = stories;
        for (Story story: _stories){
            Log.i(TAG,story.getTitle());
        }
    }

}
