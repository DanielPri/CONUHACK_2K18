package com.nuance.speechkitsample;




import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainMenu extends DetailActivity implements View.OnClickListener {

    View configButton = null;
    View aboutButton = null;

    public void init() {
        final Button speechkit = (Button) findViewById(R.id.speechkit);
        final Button start_game = (Button) findViewById(R.id.start_game);
        final Button combat = (Button) findViewById(R.id.combat);

        combat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent combat = new Intent(MainMenu.this, CombatUI.class);
                startActivity(combat);
            }
        });
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
