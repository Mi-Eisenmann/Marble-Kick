package com.example.android.marblekick;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.marblekick.view.FieldView;

public class MainActivity extends AppCompatActivity {
    private FieldView gameView;

    public static int xWidth;
    public static int yHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = findViewById(R.id.relativeLayout);
        gameView = new FieldView(this,getPreferences(Context.MODE_PRIVATE), layout);

        layout.addView(gameView, 0);

        // Screen Size information etc.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        xWidth = metrics.widthPixels;
        yHeight = metrics.heightPixels;

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume(this);
    }

    public void resetButtonOnClick(View view) {
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
        gameView.resetGame();
    }
}
