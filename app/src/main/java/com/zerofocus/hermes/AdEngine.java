package com.zerofocus.hermes;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdEngine extends Engine{

    private AdView _ad_view;

    AdEngine(MainActivity parent){
        super(parent);
    }

    public void onCreate(Bundle savedInstanceState){

        // Initialize ad
        MobileAds.initialize(getParent());
        _ad_view = getParent().findViewById(R.id.bannerView_id);
        AdRequest adRequest = new AdRequest.Builder().build();
        _ad_view.loadAd(adRequest);

        Log.i("VVV","initializing AdEngine");

        // Use the internal [adListener] to handle the ad's lifecycle
        _ad_view.setAdListener(adListener);

    }

    private static final AdListener adListener = new AdListener(){

        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.i("VVV","onAdLoaded");
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.i("VVV","onAdOpened");
        }

        @Override
        public void onAdClicked() {
            // Code to be executed when the user clicks on an ad.
            Log.i("VVV","onAdClicked");
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            Log.i("VVV","onAdLeftApplication");
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
            Log.i("VVV","onAdLeftApplication");
        }

        /*
        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
            // Code to be executed when an ad request fails.
        }
        */
    };

    public void onPause() {
        // Pause the AdView.
        _ad_view.pause();
    }

    public void onResume() {
        // Resume the AdView.
        _ad_view.resume();
    }

    public void onDestroy() {
        // Destroy the AdView.
        _ad_view.destroy();
    }

}
