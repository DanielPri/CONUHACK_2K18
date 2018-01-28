package com.nuance.speechkitsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Initial screen.
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    View asrButton = null;
    View nluButton = null;
    View textNluButton = null;
    View ttsButton = null;

    View configButton = null;
    View aboutButton = null;

    View audioButton = null;

//    View btn_listen = null;
    View btn_speak = null;
    View btn_startgame = null;
    View btn2 = null;
    View btn3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout mainContent = (LinearLayout) findViewById(R.id.main_content);

        LinearLayout conu = inflateCategoryView("CONUHACKS 3", mainContent);

        btn_speak = inflateRowView("Speak", "Cloud based ASR", conu);
        btn_startgame = inflateRowView("Start Game", "Cloud based ASR with NLU", conu);
        btn2 = inflateRowView("2", "Cloud based ASR", conu);
        btn3 = inflateRowView("3", "Cloud based ASR", conu);


        LinearLayout coreTech = inflateCategoryView("CORE TECHNOLOGIES", mainContent);

//        btn_listen = inflateRowView("Listen", "Cloud based ASR", coreTech);

        asrButton = inflateRowView("Speech Recognition", "Cloud based ASR", coreTech);
        nluButton = inflateRowView("Speech and Natural Language", "Cloud based ASR with NLU", coreTech);
        textNluButton = inflateRowView("Text and Natural Language", "Cloud based NLU (text input)", coreTech);
        ttsButton = inflateRowView("Speech Synthesis", "Cloud based TTS", coreTech);



        LinearLayout utils = inflateCategoryView("UTILITIES", mainContent);

        audioButton = inflateRowView("Audio Playback", "Loading and playing a resource", utils);

        LinearLayout misc = inflateCategoryView("MISCELLANEOUS", mainContent);

        configButton = inflateRowView("Configuration", "Host URL, App ID, etc", misc);
        aboutButton = inflateRowView("About", "Learn more about SpeechKit", misc);

    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        if(v == asrButton) {
            intent = new Intent(this, ASRActivity.class);
        } else if(v == nluButton) {
            intent = new Intent(this, NLUActivity.class);
        } else if(v == textNluButton) {
            intent = new Intent(this, TextNLUActivity.class);
        } else if(v == ttsButton) {
            intent = new Intent(this, TTSActivity.class);
        } else if(v == audioButton) {
            intent = new Intent(this, AudioActivity.class);
        }else if(v == btn_speak) {
            intent = new Intent(this,SLTActivity.class);
        }else if(v == btn2) {
            intent = new Intent(this,Main2Activity.class);
        }else if(v == btn3) {
            intent = new Intent(this,Main3Activity.class);
        } else if(v == btn_startgame) {
            intent = new Intent(this, GameUI.class);
        }

        if(intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.enter_left, R.anim.exit_left);
        }
    }

    private LinearLayout inflateCategoryView(String title, LinearLayout parent) {
        View v = (View) getLayoutInflater().inflate(R.layout.activity_main_category, null);
        ((TextView)v.findViewById(R.id.title)).setText(title);
        parent.addView(v);
        return ((LinearLayout)v.findViewById(R.id.list));
    }

    private View inflateRowView(String mainText, String subText, LinearLayout parent) {
        View v = (View) getLayoutInflater().inflate(R.layout.activity_main_row, null);
        ((TextView)v.findViewById(R.id.mainText)).setText(mainText);
        ((TextView)v.findViewById(R.id.subText)).setText(subText);
        parent.addView(v);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.config:
                onClickConfig(configButton);
                return true;
            case R.id.about:
                onClickAbout(aboutButton);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickConfig(View v){
        Intent intent = null;

        if(v == configButton) {
            intent = new Intent(this, ConfigActivity.class);
        }


        if(intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.enter_left, R.anim.exit_left);
        }
    }
    public void onClickAbout(View v){
        Intent intent = null;

        if(v == aboutButton) {
            intent = new Intent(this, AboutActivity.class);
        }


        if(intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.enter_left, R.anim.exit_left);
        }
    }

}
