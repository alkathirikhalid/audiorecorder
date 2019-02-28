/*
 * Copyright (c) 2015 Al-Kathiri Khalid www.alkathirikhalid.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.alkathirikhalid.audiorecorder;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * <p><strong>Audio Recorder<strong/></p>
 * <p>This application has a buttons that changes functionality and behavior using a Flag,
 * allowing users to use the same button to achieve multiple tasks in the same Activity,
 * the tasks being Record, Stop and Play Audio.</p>
 * <p>This application will also expose candidates in using MediaRecorder and MediaPlayer
 * including to adding the required permissions in the Manifest to save data to the device.</p>
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, OnCompletionListener {
    /**
     * Button to record, stop and play audio
     */
    private Button button;
    /**
     * TextView to provide instructions to record, stop and play audio
     */
    private TextView textView;
    /**
     * Audio file storage location name
     */
    private static String fileName;
    /**
     * MediaRecorder Obj for recording
     */
    private MediaRecorder mediaRecorder;
    /**
     * MediaPlayer Obj for playing
     */
    private MediaPlayer mediaPlayer;
    /**
     * Toggle button function by using flags isStartRecording,
     * isStopRecording, isStartPlaying and isStopPlaying
     */
    private boolean isStartRecording, isStopRecording, isStartPlaying, isStopPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find View Objects/Button by its ID
        button = (Button) findViewById(R.id.button);
        // Find View Objects/TextView by its ID
        textView = (TextView) findViewById(R.id.textView);
        // Avoid possible button null pointer
        assert button != null;
        // Bind the OnClickLister to button
        button.setOnClickListener(this);
        // Sets the file location name using Android OS Environment
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecorder.3gp";
        // Allow user to start Recording
        isStartRecording = Boolean.TRUE;
        // Disable stop recording, start playing and stop playing
        isStopRecording = isStartPlaying = isStopPlaying = Boolean.FALSE;

    }

    /**
     * Implemented method only either Record, Play or Stop isActive.
     * Based on the set flag
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Only this button ID will be triggered
            case R.id.button:
                if (isStartRecording) {
                    startRecordingActions();
                } else if (isStopRecording) {
                    stopRecordingActions();
                } else if (isStartPlaying) {
                    startPlayingActions();
                } else if (isStopPlaying) {
                    stopPlayingActions();
                } else {
                    // This line will never be executed added for best practice
                    Toast.makeText(this, R.string.unknown_action, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                // This line will never be executed added for best practice
                Toast.makeText(this, R.string.unknown_button_click, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Start Recording
     */
    private void startRecording() {
        // Instantiate a new MediaRecorder Obj named mediaRecorder
        mediaRecorder = new MediaRecorder();
        // Set Audio Source from Microphone
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // Set audio format as .3gp
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // Set audio file location to be saved
        mediaRecorder.setOutputFile(fileName);
        // Set audio encoder to AMR for backward compatibility
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // Initialize mediaRecorder to start and catch the IOException
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Start Recording
        mediaRecorder.start();
    }

    /**
     * Stop Recording
     */
    private void stopRecording() {
        // Release the Obj resources
        mediaRecorder.release();
    }

    /**
     * Start Playing
     */
    private void startPlaying() {
        // Instantiate a new MediaPlayer Obj named mediaPlayer
        mediaPlayer = new MediaPlayer();
        // Bind the onCompletionListener to mediaPlayer to be notified of successful playback
        mediaPlayer.setOnCompletionListener(this);
        // Set audio data source and catch the IOException
        try {
            mediaPlayer.setDataSource(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Initialize mediaPlayer to start and catch the IOException
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Start Playing
        mediaPlayer.start();
    }

    /**
     * Stop Playing
     */
    private void stopPlaying() {
        // Release the Obj resources
        mediaPlayer.release();
    }

    /**
     * Release resources when Activity is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Release the mediaRecorder resources
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
        // Release the mediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /**
     * Release resources when Activity is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        // Release the mediaRecorder resources
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
        // Release the mediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /**
     * Listening on Play Completion
     *
     * @param mediaPlayer Notifies playback completion
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlayingActions();
    }

    /**
     * Sequence in Start Recording
     */
    private void startRecordingActions() {
        // Start Recording
        startRecording();
        // Deactivate Start Recording
        isStartRecording = !isStartRecording;
        // Activate Stop Recording
        isStopRecording = !isStopRecording;
        // Change button image
        button.setBackgroundResource(R.drawable.ic_audio_stop_record);
        // Change textView text
        textView.setText(R.string.stop_record);
    }

    /**
     * Sequence in Stop Recording
     */
    private void stopRecordingActions() {
        // Stop Recording
        stopRecording();
        // Deactivate Stop Recording
        isStopRecording = !isStopRecording;
        // Activate Start Playing
        isStartPlaying = !isStartPlaying;
        // Change button image
        button.setBackgroundResource(R.drawable.ic_audio_play);
        // Change textView text
        textView.setText(R.string.play);
    }

    /**
     * Sequence in Start Playing
     */
    private void startPlayingActions() {
        // Start Playing
        startPlaying();
        // Deactivate Start Playing
        isStartPlaying = !isStartPlaying;
        // Activate stop Playing
        isStopPlaying = !isStopPlaying;
        // Change button image
        button.setBackgroundResource(R.drawable.ic_audio_stop_play);
        // Change textView text
        textView.setText(R.string.stop_play);
    }

    /**
     * Sequence in Stop Playing
     */
    private void stopPlayingActions() {
        // Stop Recording
        stopPlaying();
        // Deactivate Stop Recording
        isStopPlaying = !isStopPlaying;
        // Activate Start Recording
        isStartRecording = !isStopRecording;
        // Change button image
        button.setBackgroundResource(R.drawable.ic_audio_record);
        // Change textView text
        textView.setText(R.string.record);
    }

}
