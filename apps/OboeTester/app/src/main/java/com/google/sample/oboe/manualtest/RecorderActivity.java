/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sample.oboe.manualtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

/**
 * Activity to record and playKickDrum back audio.
 */
public class RecorderActivity extends TestInputActivity {

    private static final int STATE_RECORDING = 5;
    private static final int STATE_PLAYING = 6;
    private int mRecorderState = STATE_STOPPED;
    private Button mRecordButton;
    private Button mStopButton;
    private Button mPlayButton;
    private Button mShareButton;
    private boolean mGotRecording;

    @Override
    protected void inflateActivity() {
        setContentView(R.layout.activity_recorder);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecordButton = (Button) findViewById(R.id.button_start_recording);
        mStopButton = (Button) findViewById(R.id.button_stop_record_play);
        mPlayButton = (Button) findViewById(R.id.button_start_playback);
        mShareButton = (Button) findViewById(R.id.button_share);
        mRecorderState = STATE_STOPPED;
        mGotRecording = false;
        updateButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setActivityType(ACTIVITY_RECORD_PLAY);
    }

    public void onStartRecording(View view) {
        try {
            openAudio();
            startAudio();
            mRecorderState = STATE_RECORDING;
            mGotRecording = true;
            updateButtons();
        } catch (IOException e) {
            showErrorToast(e.getMessage());
        }
    }

    public void onStopRecordPlay(View view) {
        stopAudio();
        closeAudio();
        mRecorderState = STATE_STOPPED;
        updateButtons();
    }

    public void onStartPlayback(View view) {
        startPlayback();
        mRecorderState = STATE_PLAYING;
        updateButtons();
    }

    private void updateButtons() {
        mRecordButton.setEnabled(mRecorderState == STATE_STOPPED);
        mStopButton.setEnabled(mRecorderState != STATE_STOPPED);
        mPlayButton.setEnabled(mRecorderState == STATE_STOPPED && mGotRecording);
        mShareButton.setEnabled(mRecorderState == STATE_STOPPED && mGotRecording);
    }

    public void startPlayback() {
        try {
            mAudioInputTester.startPlayback();
            updateStreamConfigurationViews();
            updateEnabledWidgets();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorToast(e.getMessage());
        }
    }

    @Override
    String getWaveTag() {
        return "recording";
    }

}
