package com.nuance.speechkitsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nuance.speechkit.Audio;
import com.nuance.speechkit.AudioPlayer;
import com.nuance.speechkit.Language;
import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;
import com.nuance.speechkit.TransactionException;

/**
 * This Activity is built to demonstrate how to perform TTS.
 *
 * TTS is the transformation of text into speech.
 *
 * When performing speech synthesis with SpeechKit, you have a variety of options. Here we demonstrate
 * Language. But you can also specify the Voice. If you do not, then the default voice will be used
 * for the given language.
 *
 * Supported languages and voices can be found here:
 * http://developer.nuance.com/public/index.php?task=supportedLanguages
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class TTSCall{

    private String ttsText;

    private Session speechSession;
    private Transaction ttsTransaction;
    private State state = State.IDLE;

    public TTSCall(AudioActivity activity) {

        //Create a session
        speechSession = Session.Factory.session(activity, Configuration.SERVER_URI, Configuration.APP_KEY);
        speechSession.getAudioPlayer().setListener(activity);

        setState(State.IDLE);
    }


    /* TTS transactions */

    public void talk(String input){
        ttsText = input;
        toggleTTS();
    }

    private void toggleTTS() {
        switch (state) {
            case IDLE:
                //If we are not loading TTS from the server, then we should do so.
                if(ttsTransaction == null) {
                    synthesize();
                }
                //Otherwise lets attempt to cancel that transaction
                else {
                    cancel();
                }
                break;
            case PLAYING:
                speechSession.getAudioPlayer().pause();
                setState(State.PAUSED);
                break;
            case PAUSED:
                speechSession.getAudioPlayer().play();
                setState(State.PLAYING);
                break;
        }
    }

    /**
     * Speak the text that is in the ttsText EditText, using the language in the language EditText.
     */
    private void synthesize() {
        //Setup our TTS transaction options.
        Transaction.Options options = new Transaction.Options();
        options.setLanguage(new Language("eng-USA"));
        //options.setVoice(new Voice(Voice.SAMANTHA)); //optionally change the Voice of the speaker, but will use the default if omitted.

        //Start a TTS transaction
        ttsTransaction = speechSession.speakString(ttsText, options, new Transaction.Listener() {
            @Override
            public void onAudio(Transaction transaction, Audio audio) {


                //The TTS audio has returned from the server, and has begun auto-playing.
                ttsTransaction = null;

            }

            @Override
            public void onSuccess(Transaction transaction, String s) {


                //Notification of a successful transaction. Nothing to do here.
            }

            @Override
            public void onError(Transaction transaction, String s, TransactionException e) {


                //Something went wrong. Check Configuration.java to ensure that your settings are correct.
                //The user could also be offline, so be sure to handle this case appropriately.

                ttsTransaction = null;
            }
        });
    }

    /**
     * Cancel the TTS transaction.
     * This will only cancel if we have not received the audio from the server yet.
     */
    private void cancel() {
        ttsTransaction.cancel();
    }

    public void onBeginPlaying() {


        //The TTS Audio will begin playing.

        setState(State.PLAYING);
    }

    public void onFinishedPlaying() {


        //The TTS Audio has finished playing

        setState(State.IDLE);
    }

    /* State Logic: IDLE <-> PLAYING <-> PAUSED */

    private enum State {
        IDLE,
        PLAYING,
        PAUSED
    }

    /**
     * Set the state and update the button text.
     */
    private void setState(State newState) {
        state = newState;
        switch (newState) {
            case IDLE:
                // Next possible action is speaking

                break;
            case PLAYING:
                // Next possible action is pausing

                break;
            case PAUSED:
                // Next possible action is resuming the speech

                break;
        }
    }

}
