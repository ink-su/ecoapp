package com.eco.acorn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CardView mCard;

    //initialize mediaPlayer object
    private MediaPlayer mMediaPlayer;

    //initialize AudioManager object
    private AudioManager mAudioManager;

    //initialize OnAudioFocusChangeListener to release mediaPlayer
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if (i == AudioManager.AUDIOFOCUS_LOSS ||
                    i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                releaseMediaPlayer();
            }
        }
    };

    //release media on completion of media
    private MediaPlayer.OnCompletionListener mCompletionLister = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    //releases mediaPlayer app
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mCard = findViewById(R.id.scanningCard);
        mCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final TextView subTitle = findViewById(R.id.scanningSubtitle);
                        subTitle.setText(R.string.scanning_success);

                        final ImageView logo = findViewById(R.id.scanLogo);
                        logo.setImageResource(R.drawable.check_mark);

                        // Add chime
                        releaseMediaPlayer();
                        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                            mMediaPlayer = mMediaPlayer.create(MainActivity.this, R.raw.chime);
                            mMediaPlayer.start();
                            mMediaPlayer.setOnCompletionListener(mCompletionLister);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                subTitle.setText(R.string.scanning_instruction_subtitle);
                                logo.setImageResource(R.drawable.acorn_logo);
                            }
                        }, 5000);

                        break;
                }
                return true;
            }
        });
    }
}
