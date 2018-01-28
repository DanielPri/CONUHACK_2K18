package com.nuance.speechkitsample;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class CombatUI extends DetailActivity implements View.OnClickListener {

    int maxValue = 90;
    int progressValue = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);

        init();

        final Button attack = (Button) findViewById(R.id.attack);

        attack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                getProgress();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void init(){
        ProgressBar simpleProgressBar=(ProgressBar) findViewById(R.id.progressbar); // initiate the progress bar
        simpleProgressBar.setRotation(180);
        maxValue=simpleProgressBar.getMax(); // get maximum value of the progress bar

    }

    public void getProgress(){
        ProgressBar simpleProgressBar=(ProgressBar)findViewById(R.id.progressbar); // initiate the progress bar
        progressValue=simpleProgressBar.getProgress();
    }

}
