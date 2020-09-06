package com.zerofocus.hermes;

// GK: Change this for androidX compatibility
// import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Engine[] engines_list;

    public MainActivity(){
        super();
        engines_list = new Engine[]{
                new AdEngine(this)
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (Engine engine: engines_list)
            engine.onCreate(savedInstanceState);

    }
}
