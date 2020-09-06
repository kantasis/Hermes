package com.zerofocus.hermes;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FetchRssGear extends AsyncTask<String, Integer, ArrayList<Story>> {

    private static final String TAG = "FetchRssGear.VVV:";

    private MainActivity _parent;

    public FetchRssGear(MainActivity parent){
        _parent=parent;
    }

    @Override
    protected void onPreExecute() {
        _parent.setRefreshing(true);
        Log.i(TAG,"Getting feed");
    }

    @Override
    protected ArrayList<Story> doInBackground(String... url_str_list) {

        ArrayList <Story> stories = new ArrayList<>();
        for ( String url_str : url_str_list){
            Log.i(TAG,"parsing "+url_str);
            if (TextUtils.isEmpty(url_str))
                continue;
            if (!url_str.startsWith("http://") && !url_str.startsWith("https://"))
                url_str = "http://" + url_str;
            try {
                URL url = new URL(url_str);
                InputStream inputStream = url.openConnection().getInputStream();
                stories.addAll(RssParserGear.parseFeed(inputStream));
            } catch (IOException e) {
                Log.e(TAG, "Error IOException", e);
            } catch (XmlPullParserException e) {
                // TODO: perhaps this should be checked inside RssParser class
                Log.e(TAG, "Error XmlPullParserException", e);
            }
            // TODO: I think malformed URL exception should be checked too here

            publishProgress(0);
        }
        return stories;
    }

    @Override
    protected void onPostExecute(ArrayList<Story> stories) {
        _parent.setRefreshing(false);
        _parent.updateStories(stories);
//        manageFeed();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
//        manageFeed();
    }

}
