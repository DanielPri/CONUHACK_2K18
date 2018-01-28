package com.nuance.speechkitsample;




import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


public class MainMenu extends DetailActivity implements View.OnClickListener {

    public void init() {
        final Button speechkit = (Button) findViewById(R.id.speechkit);
        final Button start_game = (Button) findViewById(R.id.start_game);


        start_game.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent start_game = new Intent(MainMenu.this, GameUI.class);
                startActivity(start_game);
            }
        });
        speechkit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent speechkit = new Intent(MainMenu.this, MainActivity.class);
                startActivity(speechkit);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        init();
    }

    @Override
    public void onClick(View view) {

    }

}
