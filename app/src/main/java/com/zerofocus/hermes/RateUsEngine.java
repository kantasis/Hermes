package com.zerofocus.hermes;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import static com.zerofocus.hermes.MainActivity.TAG;

public class RateUsEngine extends Engine {

    //TODO: put these constants in remote config

    private Dialog _dialog;
    private static int DAYS_TO_REQUEST_INT = 10;
    private static int LAUNCHES_TO_REQUEST_INT = 3;

    public RateUsEngine(MainActivity parent) {
        super(parent);
        _dialog = new Dialog(getParent());
        SharedPreferences sharedPreferences = getParent().getSharedPreferences("rateUs", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("dontShowAgain", false)) {
            return;
        }

        Long now_long = System.currentTimeMillis();
        Long firstLaunch_date = sharedPreferences.getLong("firstLaunch_date", 0);

        if (firstLaunch_date == 0) {
            firstLaunch_date = now_long;
            editor.putLong("firstLaunch_date", firstLaunch_date);
        }


        Long nextShow_long = firstLaunch_date + DAYS_TO_REQUEST_INT * 24 * 60 * 60 * 1000 ;
        if (now_long < nextShow_long) {
            Log.i(TAG,"too early " + now_long + "/" + nextShow_long );
            editor.commit();
            return;
        }

        long launch_count = sharedPreferences.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        if ( launch_count >= LAUNCHES_TO_REQUEST_INT) {
            Log.i(TAG,"lets show!");
            editor.putLong("launch_count", 0);
            showRateDialog(editor);
        }else
            Log.i(TAG,"apparently not: " + launch_count + "/" + LAUNCHES_TO_REQUEST_INT);
        editor.commit();
    }

    public void showRateDialog(final SharedPreferences.Editor editor) {
        //TODO: hard coded string here...
        _dialog.setTitle(R.string.rateus_titleStr_id);
        _dialog.setContentView(R.layout.rateus_view);
        _dialog.findViewById(R.id.rateUs_confirm_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParent().startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getParent().getPackageName()))
                );
                _dialog.dismiss();
            }
        });

        _dialog.findViewById(R.id.rateUs_later_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.findViewById(R.id.rateUs_never_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontShowAgain", true);
                    editor.commit();
                }
                _dialog.dismiss();
            }
        });

        // Show Dialog
        _dialog.show();
    }

    public void onPause(){
        _dialog.dismiss();
    }
}