package com.zerofocus.hermes;

import android.content.Context;
import android.os.Bundle;

public class Engine {

    private MainActivity _parent;

    Engine(MainActivity parent) {
        _parent = parent;
    }

    public MainActivity getParent() {
        return _parent;
    }

    public void onCreate(Bundle savedInstanceState) {}

    public void onPause() {}

    public void onResume(){}

    public void onDestroy() {}

}