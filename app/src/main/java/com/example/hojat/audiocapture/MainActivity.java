package com.example.hojat.audiocapture;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {
    private MediaRecorder mediaRecorder = new MediaRecorder();
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    SoundPool soundPool;
    /*int explosion1 = 0;
    int explosion2= 0;
    int explosion3 = 0;*/
    private int soundID;
    int counter;
    boolean plays = false, loaded = false;
    SeekBar seekBar;
    double sliderval;

    int amp = 10000;
    double twopi = 8.*Math.atan(1.);
    double fr = 440.f;
    double ph = 0.0;




    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            /*START SoundPool*/
            /*

            final Handler handler = new Handler();
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);
            explosion1 = soundPool.load(mFileName,0);
            explosion2 = soundPool.load(mFileName,1);
            explosion3 = soundPool.load(mFileName,2);
           // if (explosion!=0){

                soundPool.play(explosion1, 1, 1, 0, 1, .5f);
                soundPool.play(explosion2, 1, 1, 0, 1, 2f);
                soundPool.play(explosion3, 1, 1, 0, 1, 5f);

                handler.postDelayed(delayedUnload, 5 * 1000);
            //}
            /*END SoundPool*/
            // Load the sounds
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    loaded = true;
                }
            });

            soundID = soundPool.load(mFileName, 1);

            if (loaded && !plays) {
                //soundPool.play(soundID, 1, 1, 1, 0, 1f);
                // the sound will play for ever if we put the loop parameter -1
                //fr =  440 + 440*sliderval;

                soundPool.play(soundID, 1, 1, 1, -1, (float) sliderval);
                counter = counter++;
                Toast.makeText(this, "Played sound", Toast.LENGTH_SHORT).show();
                plays = true;
            }





        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
    /*private Runnable delayedUnload = new Runnable()
    {
        @Override
        public void run()
        {
            soundPool.unload(explosion1);
            soundPool.unload(explosion2);
            soundPool.unload(explosion3);
            soundPool.release();
        }
    };*/

    private void stopPlaying() {
        if (plays) {
            soundPool.pause(soundID);
            soundID = soundPool.load(mFileName, counter);
            Toast.makeText(this, "Pause sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }


        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {

        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        mRecorder.start();
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };
        //test git
        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public MainActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        /*soundPool = null;*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);
        Button captureBtn = (Button)findViewById(R.id.captureBtn);*/
        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));


        seekBar = new SeekBar(this);
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {


            public void onProgressChanged(SeekBar seekBar1,
                                          int progress,
                                          boolean fromUser) {
                if(fromUser) sliderval = progress / (double)seekBar1.getMax();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBar.setOnSeekBarChangeListener(listener);

        counter = 0;
        ll.addView(seekBar,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
        setContentView(ll);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void captureAudio(View captureBtn){

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile("AudioCaptured");
        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopAudio(View stopBtn){

        mediaRecorder.stop();
        mediaRecorder.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
