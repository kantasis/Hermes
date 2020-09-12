package com.zerofocus.hermes;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class AdEngine extends Engine{

    private static final int _INTERSTITIAL_PERIOD_INT = 3;

    private AdView _bannerAd_view;
    private InterstitialAd _interstitialAd;

    AdEngine(MainActivity parent){
        super(parent);
    }

    public void onCreate(Bundle savedInstanceState){

        // Initialize ads
        MobileAds.initialize(getParent());
        loadBannerAd();
        loadInterstitialAd();
    }

    public void loadBannerAd(){
        _bannerAd_view = getParent().findViewById(R.id.bannerView_id);
        AdRequest adRequest = new AdRequest.Builder().build();
        _bannerAd_view.loadAd(adRequest);

        Log.i(getParent().TAG,"initializing AdEngine");

        // Use the internal [adListener] to handle the ad's lifecycle
        _bannerAd_view.setAdListener(new BannerListener());
    }

    public void loadInterstitialAd(){
        _interstitialAd = new InterstitialAd(getParent());
        // TODO: Change the ad ID
        _interstitialAd.setAdUnitId(getParent().getResources().getString(R.string.test_interstitialAd_id));

        _interstitialAd.loadAd(new AdRequest.Builder().build());
        _interstitialAd.setAdListener(new InterstitialListener());
    }

    private class InterstitialListener extends com.google.android.gms.ads.AdListener {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.i(getParent().TAG,"Interstitial onAdLoaded");
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.i(getParent().TAG,"Interstitial onAdOpened");
        }

        @Override
        public void onAdClicked() {
            // Code to be executed when the user clicks on an ad.
            Log.i(getParent().TAG,"Interstitial onAdClicked");
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            Log.i(getParent().TAG,"Interstitial onAdLeftApplication");
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
            Log.i(getParent().TAG,"Interstitial onAdLeftApplication");
            _interstitialAd.loadAd(new AdRequest.Builder().build());
        }

        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
            // Code to be executed when an ad request fails.
            Log.i(getParent().TAG,"Failed to load ad");
        }

    }
    private class BannerListener extends com.google.android.gms.ads.AdListener{

        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.i(getParent().TAG, "onAdLoaded");
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.i(getParent().TAG,"onAdOpened");
        }

        @Override
        public void onAdClicked() {
            // Code to be executed when the user clicks on an ad.
            Log.i(getParent().TAG,"onAdClicked");
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            Log.i(getParent().TAG,"onAdLeftApplication");
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
            Log.i(getParent().TAG,"onAdLeftApplication");
        }

        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
            // Code to be executed when an ad request fails.
            Log.i(getParent().TAG,"Failed to load ad");
        }

    }

    public void onPause() {
        // Pause the AdView.
        _bannerAd_view.pause();
    }

    public void onResume() {
        // Resume the AdView.
        _bannerAd_view.resume();

        int interstitialPeriodRnD_int = new Random().nextInt(_INTERSTITIAL_PERIOD_INT);
        if (interstitialPeriodRnD_int==0)
            showInterstitial();
        Log.i(getParent().TAG,"Resuming, " + interstitialPeriodRnD_int + "/" + _INTERSTITIAL_PERIOD_INT);

    }

    public void showInterstitial(){
        if (_interstitialAd.isLoaded()) {
            Log.w(getParent().TAG, "Showing interstitial ad");
            _interstitialAd.show();
        }else
            Log.w(getParent().TAG, "Warning: Interstitial ad not loaded yet");
    }

    public void onDestroy() {
        // Destroy the AdView.
        _bannerAd_view.destroy();
    }

}
