package com.nuance.speechkitsample;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nuance.speechkit.Audio;
import com.nuance.speechkit.AudioPlayer;
import com.nuance.speechkit.DetectionType;
import com.nuance.speechkit.Interpretation;
import com.nuance.speechkit.Language;
import com.nuance.speechkit.Recognition;
import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;
import com.nuance.speechkit.TransactionException;

import org.json.JSONException;
import org.json.JSONObject;
import com.nuance.speechkitsample.TTSCall;

/**
 * This Activity is built to demonstrate how to perform NLU (Natural Language Understanding).
 *
 * This Activity is very similar to ASRActivity. Much of the code is duplicated for clarity.
 *
 * NLU is the transformation of text into meaning.
 *
 * When performing speech recognition with SpeechKit, you have a variety of options. Here we demonstrate
 * Context Tag and Language.
 *
 * The Context Tag is assigned in the system configuration upon deployment of an NLU model.
 * Combined with the App ID, it will be used to find the correct NLU version to query.
 *
 * Languages can also be configured. Supported languages can be found here:
 * http://developer.nuance.com/public/index.php?task=supportedLanguages
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class GameUI extends AudioActivity implements View.OnClickListener {


    private Audio startEarcon;
    private Audio stopEarcon;
    private Audio errorEarcon;

    private RadioGroup detectionType;
    private EditText nluContextTag;
//    private EditText language;

//    private TextView logs;
    private TextView narratorText;
    private TextView replyBox;
    private TextView yourLocation;
//    private Button clearLogs;

    private Button toggleReco;

    private ProgressBar volumeBar;

    private Session speechSession;
    private Transaction recoTransaction;
    private State state = State.IDLE;
    private finiteState current = finiteState.FOREST;

    private String literal = "";
    private String intent = "";
    private TTSCall ttsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ui);

        ImageView img = (ImageView) findViewById(R.id.imageView);


        ttsCall = new TTSCall(this);

        detectionType = (RadioGroup)findViewById(R.id.detection_type_picker );
//        nluContextTag = (EditText)findViewById(R.id.nlu_context_tag);
//        nluContextTag.setText(Configuration.CONTEXT_TAG);
//
//        language = (EditText)findViewById(R.id.language);
//        language.setText(Configuration.LANGUAGE_CODE);

//       logs = (TextView)findViewById(R.id.logs);
//        clearLogs = (Button)findViewById(R.id.clear_logs);
//        clearLogs.setOnClickListener(this);

        toggleReco = (Button)findViewById(R.id.toggle_reco);
        toggleReco.setOnClickListener(this);

        narratorText = (TextView)findViewById(R.id.narratorBox);
        replyBox  = (TextView)findViewById(R.id.replyBox);
        yourLocation = (TextView)findViewById(R.id.yourLocation);

        volumeBar = (ProgressBar)findViewById(R.id.volume_bar);

        //Create a session
        speechSession = Session.Factory.session(this, Configuration.SERVER_URI, Configuration.APP_KEY);

        loadEarcons();

        setState(State.IDLE);
        narratorText.setText("Welcome adventurer. You are tasked with taking down the Werewolf that was terrorizing our village." +
                "He was last seen going into the woods...\nAs you enter the forest, you notice claw marks on a " +
                "tree, and nearby grows a tree that seems easy to climb. Down the center, a path goes deeper into the woods.\nWhat do you do?\n");
        yourLocation.setText("Location: Forest");
        ttsCall.talk(narratorText.getText().toString());
    }

    // Another activity comes into the foreground. Let's release the server resources if in used.
    // Ref: https://developer.android.com/guide/components/activities/activity-lifecycle.html
    @Override
    protected void onPause() {
        switch (state) {
            case IDLE:
                // Nothing to do since there is no ongoing recognition
                break;
            case LISTENING:
                // End the ongoing recording
                stopRecording();
                break;
            case PROCESSING:
                // End the ongoing recording and cancel the server recognition
                // This cancel request will generate an internal onError callback even if the server
                // returns a successful recognition.
                cancel();
                break;
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if(v == toggleReco) {
            toggleReco();
        }
    }

    /* Reco transactions */

    private void toggleReco() {
        switch (state) {
            case IDLE:
                recognize();
                break;
            case LISTENING:
                stopRecording();
                break;
            case PROCESSING:
                cancel();
                break;
        }
    }

    /**
     * Start listening to the user and streaming their voice to the server.
     */
    private void recognize() {
        //Setup our Reco transaction options.
        Transaction.Options options = new Transaction.Options();
        options.setDetection(resourceIDToDetectionType(R.id.long_endpoint));
        options.setLanguage(new Language("eng-USA"));
        options.setEarcons(startEarcon, stopEarcon, errorEarcon, null);

        //Add properties to appServerData for use with custom service. Leave empty for use with NLU.
        JSONObject appServerData = new JSONObject();
        //Start listening
        recoTransaction = speechSession.recognizeWithService("M10253_A3221", appServerData, options, recoListener);
    }

    private Transaction.Listener recoListener = new Transaction.Listener() {
        @Override
        public void onStartedRecording(Transaction transaction) {
//            logs.append("\nonStartedRecording");

            //We have started recording the users voice.
            //We should update our state and start polling their volume.
            setState(State.LISTENING);
            startAudioLevelPoll();
        }

        @Override
        public void onFinishedRecording(Transaction transaction) {
//            logs.append("\nonFinishedRecording");

            //We have finished recording the users voice.
            //We should update our state and stop polling their volume.
            setState(State.PROCESSING);
            stopAudioLevelPoll();
        }

        @Override
        public void onServiceResponse(Transaction transaction, org.json.JSONObject response) {
//            try {
//                // 2 spaces for tabulations.
////                logs.append("\nonServiceResponse: " + response.toString(2));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            // We have received a service response. In this case it is our NLU result.
            // Note: this will only happen if you are doing NLU (or using a service)
        }

        @Override
        public void onRecognition(Transaction transaction, Recognition recognition) {
//            logs.append("\nonRecognition: " + recognition.getText());

            //We have received a transcription of the users voice from the server.
        }

        @Override
        public void onInterpretation(Transaction transaction, Interpretation interpretation) {
            try {
                //logs.append("\nonInterpretation: " + interpretation.getResult().toString(2));
                intent = interpretation.getResult().getJSONArray("interpretations").getJSONObject(0).getJSONObject("action").getJSONObject("intent").optString("value");
                literal = interpretation.getResult().getJSONArray("interpretations").getJSONObject(0).getString("literal");
//                logs.append("\n" + intent + "\n" + literal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            replyBox.setText(literal);
            manageState();


            // We have received a service response. In this case it is our NLU result.
            // Note: this will only happen if you are doing NLU (or using a service)
        }

        //manages the state
        private void manageState(){
            switch (current) {
                case FOREST:
                    //if the choice is to climb tree
                    if(intent.equals("climbTreeUp")){
                        narratorText.setText("You climb up the tree, and you see a clear sky with a full moon." +
                                "You hear a howl further into the woods.\nWhat do you do?");
                        current = finiteState.TREE_TOP;
                        yourLocation.setText("Location: Tree-Top");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else if(intent.equals("examineClaws")){
                        narratorText.setText("You examine the claw marks. They are large and cut deeply into the woods " +
                                "Clearly caused by a monster of some sort.");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else if(intent.equals("lookAtTree")){
                        narratorText.setText("All trees look the same, save the one with claw marks");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else if(intent.equals("goPath")){
                        narratorText.setText("You go down the path. You are now deeper in the woods. You see a path covered in tracks" +
                                " and a large hole in a tree, at about chest height.\nWhat do you do?");
                        current = finiteState.FOREST_DEEPER;
                        yourLocation.setText("Location: Deep Forest");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else {
                        narratorText.setText("You either cannot do that, or are an idiot");
                        ttsCall.talk(narratorText.getText().toString());
                    }

                    break;
                case TREE_TOP:
                    if(intent.equals("jumpFromTree")){
                        narratorText.setText("You cannot jump from this height, you risk death!");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else if(intent.equals("climbTreeDown")){
                        narratorText.setText("You go back down. You are now once again in the forest.\nWhat do you do?");
                        current = finiteState.FOREST;
                        yourLocation.setText("Location: Forest");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else {
                        narratorText.setText("I can't understand you up there in the trees sometimes.");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    break;
                case FOREST_DEEPER:
                    if(intent.equals("tracksPath")){
                        narratorText.setText("You follow the tracks ever deeper into the darkness of the forest. You see that" +
                                " the footprints are both human and animal.");
                        current = finiteState.FOREST_FINAL;
                        yourLocation.setText("Location: Complete Wilderness");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else if(intent.equals("holeInTree")){
                        narratorText.setText("You notice a harmless and cute squirrel in the tree.");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else {
                        narratorText.setText("You either cannot do that, or I do not understand you.");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                case FOREST_FINAL:
                    if(intent.equals("examineBones")){
                        narratorText.setText("The bones seem fresh and licked clean.");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else if(intent.equals("examineBlood")){
                        narratorText.setText("The blood is fresh and still warm. An ominous vapor rises from it on this cold night");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else if(intent.equals("goPath") && current == finiteState.FOREST_FINAL){
                        narratorText.setText("You encounter the werewolf. In an extremely anticlimatic battle, you win.");
                        current = finiteState.VICTORY;
                        yourLocation.setText("Location: VICTORY");
                        ttsCall.talk(narratorText.getText().toString());
                    }
                    else {
                        narratorText.setText("You either cannot do that, or I do not understand you.");
                        ttsCall.talk(narratorText.getText().toString());
                    }
            }

        }

        @Override
        public void onSuccess(Transaction transaction, String s) {
//            logs.append("\nonSuccess");

            //Notification of a successful transaction.
            setState(State.IDLE);
        }

        @Override
        public void onError(Transaction transaction, String s, TransactionException e) {
//            logs.append("\nonError: " + e.getMessage() + ". " + s);

            //Something went wrong. Check Configuration.java to ensure that your settings are correct.
            //The user could also be offline, so be sure to handle this case appropriately.
            //We will simply reset to the idle state.
            setState(State.IDLE);
        }
    };

    /**
     * Stop recording the user
     */
    private void stopRecording() {
        recoTransaction.stopRecording();
    }

    /**
     * Cancel the Reco transaction.
     * This will only cancel if we have not received a response from the server yet.
     */
    private void cancel() {
        recoTransaction.cancel();
    }

    /* Audio Level Polling */

    private Handler handler = new Handler();

    /**
     * Every 50 milliseconds we should update the volume meter in our UI.
     */
    private Runnable audioPoller = new Runnable() {
        @Override
        public void run() {
            float level = recoTransaction.getAudioLevel();
            volumeBar.setProgress((int)level);
            handler.postDelayed(audioPoller, 50);
        }
    };

    /**
     * Start polling the users audio level.
     */
    private void startAudioLevelPoll() {
        audioPoller.run();
    }

    /**
     * Stop polling the users audio level.
     */
    private void stopAudioLevelPoll() {
        handler.removeCallbacks(audioPoller);
        volumeBar.setProgress(0);
    }


    /* State Logic: IDLE -> LISTENING -> PROCESSING -> repeat */

    @Override
    public void onBeginPlaying(AudioPlayer audioPlayer, Audio audio) {
        super.onBeginPlaying(audioPlayer, audio);

        ttsCall.onBeginPlaying();
    }

    @Override
    public void onFinishedPlaying(AudioPlayer audioPlayer, Audio audio) {
        super.onFinishedPlaying(audioPlayer, audio);

        ttsCall.onFinishedPlaying();
        if(current == finiteState.VICTORY){
            finishActivity(0);

        }
    }

    private enum State {
        IDLE,
        LISTENING,
        PROCESSING
    }
    private enum finiteState {
        FOREST,
        TREE_TOP,
        FOREST_DEEPER,
        FOREST_FINAL,
        VICTORY
    }

    /**
     * Set the state and update the button text.
     */
    private void setState(State newState) {
        state = newState;
        switch (newState) {
            case IDLE:
                toggleReco.setText("What do you do?");
                break;
            case LISTENING:
                toggleReco.setText(getResources().getString(R.string.listening));
                break;
            case PROCESSING:
                toggleReco.setText(getResources().getString(R.string.processing));
                break;
        }
    }

    /* Earcons */

    private void loadEarcons() {
        //Load all of the earcons from disk
        startEarcon = new Audio(this, R.raw.sk_start, Configuration.PCM_FORMAT);
        stopEarcon = new Audio(this, R.raw.sk_stop, Configuration.PCM_FORMAT);
        errorEarcon = new Audio(this, R.raw.sk_error, Configuration.PCM_FORMAT);
    }

    /* Helpers */

    private DetectionType resourceIDToDetectionType(int id) {
        if(id == R.id.long_endpoint) {
            return DetectionType.Long;
        }
        if(id == R.id.short_endpoint) {
            return DetectionType.Short;
        }
        if(id == R.id.none) {
            return DetectionType.None;
        }
        return null;
    }
}

