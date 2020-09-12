package com.zerofocus.hermes;

// GK: Change this for androidX compatibility
// import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "MainActivity.VVV:";
    public static final int RQ_STORY = 1;

    private Engine[] _engines_list;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private View _emptyListView;
    private RecyclerView _recyclerView;
    private ArrayList<Story> _stories;
    private StoryAdapterGear _storyAdapterGear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _stories = new ArrayList<>();

        _engines_list = new Engine[]{
                new AdEngine(this)
        };

        for (Engine engine: _engines_list)
            engine.onCreate(savedInstanceState);

        _swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_id);
        _swipeRefreshLayout.setOnRefreshListener(this);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerView_id);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _storyAdapterGear = new StoryAdapterGear(_stories);
        _recyclerView.setAdapter(_storyAdapterGear);

        _emptyListView = findViewById(R.id.emptyView_id);

        fetchFeed();
    }

    @Override
    public void onRefresh() {
        // SwipeRefreshLayout.OnRefreshListener
        fetchFeed();
    }

    public void fetchFeed(){
        _stories.clear();
        String [] _rssURL_string_list = getResources().getStringArray(R.array.rss_list);
        new FetchRssGear(this).execute(_rssURL_string_list);
    }

    // This function is to be used by the FetchRssGear class
    public void setRefreshing(boolean flag){
        _swipeRefreshLayout.setRefreshing(flag);
    }

    public void updateStories(ArrayList<Story> stories){
        Log.i(TAG,"Updating stories");

        _stories.addAll(stories);

        if (stories == null || stories.size() == 0){
            Log.i(TAG,"No stories found, bye bye");
            _emptyListView.setVisibility(View.VISIBLE);
            _recyclerView.setVisibility(View.GONE);
            return;
        }

        _emptyListView.setVisibility(View.GONE);
        _recyclerView.setVisibility(View.VISIBLE);

        Collections.sort(_stories);
        for (Story story: _stories){
            Log.i(TAG, story.getTitle());
        }

        Parcelable recyclerViewState = _recyclerView.getLayoutManager().onSaveInstanceState();
        _recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        _storyAdapterGear.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        for (Engine engine: _engines_list)
            engine.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        for (Engine engine: _engines_list)
            engine.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Engine engine: _engines_list)
            engine.onDestroy();
    }

}
