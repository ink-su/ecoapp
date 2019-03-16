package com.eco.acorn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private CardView mCard;
    private Button mRedeemButton;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
        bottomBar.setSelectedItemId(R.id.scan);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                showPage(menuItem);
                return true;
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mCard = findViewById(R.id.scanningCard);
        mCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final TextView title = findViewById(R.id.titleText);
                        title.setText(R.string.scanning_success_title);

                        final TextView subTitle = findViewById(R.id.scanningSubtitle);
                        subTitle.setText(R.string.scanning_success_subtitle);

                        final ImageView logo = findViewById(R.id.scanLogo);
                        logo.setImageResource(R.drawable.check_mark);

                        // Add chime
                        releaseMediaPlayer();
                        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                            mMediaPlayer = mMediaPlayer.create(MainActivity.this, R.raw.tones);
                            mMediaPlayer.start();
                            mMediaPlayer.setOnCompletionListener(mCompletionLister);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                title.setText(R.string.scanning_instruction_title);
                                subTitle.setText(R.string.scanning_instruction_subtitle);
                                logo.setImageResource(R.drawable.acorn_logo);
                            }
                        }, 5000);

                        break;
                }
                return true;
            }
        });

        mRedeemButton = findViewById(R.id.redeem_button);
        mRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPages();
                FrameLayout frame = findViewById(R.id.redeemPage);
                frame.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clearPages() {
        int[] pages = new int[] {
                R.id.earnPage,
                R.id.pointsPage,
                R.id.scanPage,
                R.id.communityPage,
                R.id.profilePage,
                R.id.redeemPage};

        for (int i = 0; i < pages.length; i++) {
            FrameLayout frame = findViewById(pages[i]);
            frame.setVisibility(View.INVISIBLE);
        }
    }

    // Shows/hides pages from navbar selection
    private void showPage(MenuItem menuItem)
    {
        clearPages();

        int id = menuItem.getItemId();
        switch (id) {
            case R.id.earn:
                FrameLayout frame = findViewById(R.id.earnPage);
                frame.setVisibility(View.VISIBLE);
                break;
            case R.id.points:
                FrameLayout frame2 = findViewById(R.id.pointsPage);
                frame2.setVisibility(View.VISIBLE);
                break;
            case R.id.scan:
                FrameLayout frame3 = findViewById(R.id.scanPage);
                frame3.setVisibility(View.VISIBLE);
                break;
            case R.id.community:
                FrameLayout frame4 = findViewById(R.id.communityPage);
                frame4.setVisibility(View.VISIBLE);
                break;
            case R.id.profile:
                FrameLayout frame5 = findViewById(R.id.profilePage);
                frame5.setVisibility(View.VISIBLE);
                break;
        }
    }
}
