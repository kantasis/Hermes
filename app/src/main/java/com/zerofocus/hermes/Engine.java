package com.zerofocus.hermes;

import android.content.Context;
import android.os.Bundle;

public class Engine {

    private MainActivity _parent;

    Engine(MainActivity ctx) {
        _parent = ctx;
    }

    public MainActivity getParent() {
        return _parent;
    }

    public void onCreate(Bundle savedInstanceState) {

    }
}