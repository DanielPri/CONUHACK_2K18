package com.nuance.speechkitsample;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CombatUI extends DetailActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);

        Button attack = (Button) findViewById(R.id.attack);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressBar.setRotation(180);
        }
        textView = (TextView) findViewById(R.id.textView);
        // Start long running operation in a background thread

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    Button attack = (Button) findViewById(R.id.attack);
                    if(progressStatus <= 100){
                        progressStatus--;
                    }

                    attack.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            progressStatus+=20;
                            if(progressStatus > 100){
                                progressStatus = 100;
                            }
                        }
                    });

                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
//                            textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



}
