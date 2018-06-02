package net.afterday.compass.devices.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

import net.afterday.compass.R;
import net.afterday.compass.core.Controls;

public class Sound {
    private int mRadTick;
    private int mMessage;
    private int mEmissionMessage;
    private int mAnomalyTick;
    private int mMental;
    private int mAnomalyDeath;
    private int mDie;
    private int mZombify;
    private int mControl;
    private int mBurer;
    private int mController;
    private int healing;
    private int glassBreak;
    private int LevelUp;
    private int mControllerPresence;
    private int mBurerPresence;
    private int mTransmutating;
    private int mWTimer;
    private int mMentalHit;
    private int mAbducted;
    private int itemScanned;
    private int mArtefact;
    private MediaPlayer mediaPlayer;
    private static final String TAG = "SOUND";
    private SoundPool mSoundPool;
    private AudioManager mAudioManager;
    private String pckg;
    private Context ctx;
    private int currentPlaying = -1;
    private long hStarted = 0;

    public Sound(Context ctx) {
        this.ctx = ctx;
        pckg = ctx.getPackageName();
        mediaPlayer = new MediaPlayer();
//        try
//        {
//            mediaPlayer.setDataSource(ctx, getSounUri(R.raw.controlled));
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }

        mSoundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
        mAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);

        // Load samples
        mRadTick = mSoundPool.load(ctx, R.raw.rad_click, 1);
        mAnomalyTick = mSoundPool.load(ctx, R.raw.anomaly, 1);
        mMental = mSoundPool.load(ctx, R.raw.mental, 1);
        mMessage = mSoundPool.load(ctx, R.raw.pda_news, 1);
        mEmissionMessage = mSoundPool.load(ctx, R.raw.pda_sos_vybros, 1);
        mAnomalyDeath = mSoundPool.load(ctx, R.raw.ano_kill, 1);
        mDie = mSoundPool.load(ctx, R.raw.die, 1);
        mZombify = mSoundPool.load(ctx, R.raw.zombified, 1);
        mControl = mSoundPool.load(ctx, R.raw.controlled, 1);
        mBurer = mSoundPool.load(ctx, R.raw.burer, 1);
        mController = mSoundPool.load(ctx, R.raw.controller, 1);
        mControllerPresence = mSoundPool.load(ctx, R.raw.controller_presence, 1);
        healing = mSoundPool.load(ctx, R.raw.healing, 1);
        glassBreak = mSoundPool.load(ctx, R.raw.glass_break, 1);
        LevelUp = mSoundPool.load(ctx, R.raw.pda_level_up, 1);
        mBurerPresence = mSoundPool.load(ctx, R.raw.burer_presence, 1);
        mTransmutating = mSoundPool.load(ctx, R.raw.transmutating, 1);
        mWTimer = mSoundPool.load(ctx, R.raw.w_timer_begins, 1);
        mMentalHit = mSoundPool.load(ctx, R.raw.mental_hit, 1);
        mAbducted = mSoundPool.load(ctx, R.raw.abducted, 1);
        itemScanned = mSoundPool.load(ctx, R.raw.pda_qr_scanned, 1);
        mArtefact = mSoundPool.load(ctx, R.raw.pda_artefact, 1);
    }

    public void playRadClick() {
        float vol = (float) Math.random() * 0.1f + 0.5f; // From 0.5 to 0.6
        float freq = (float) Math.random() * 0.2f + 0.9f; // From 0.9 to 1.1
        int soundStream = mSoundPool.play(
                mRadTick, // Sound
                vol, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                vol, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                2, // Priority
                0, // Loop
                freq // Frequency
        );
    }

    public void playGlassBreak()
    {
        mSoundPool.play(glassBreak, 1f, 1f, 2, 0, 1f);
    }
    public void playLevelUp()
    {
        mSoundPool.play(LevelUp, 1f, 1f, 2, 0, 1f);
    }

    public void playHealing()
    {
        mSoundPool.play(healing,
                1f,
                1f,
                2,
                0,
                1f);

    }

    public void stopHealing()
    {
        mSoundPool.stop(healing);
    }

    public void playAnomalyClick() {
        mSoundPool.play(
                mAnomalyTick, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                2, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playMental() {
        mSoundPool.play(
                mMental, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                2, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playAnomalyDeath() {
        mSoundPool.play(
                mAnomalyDeath, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                2, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playEmissionWarning() {

        mSoundPool.play(
                mEmissionMessage, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playMessage() {
        mSoundPool.play(
                mMessage, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playWTimer() {
        mSoundPool.play(
                mWTimer, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playDeath() {
        mSoundPool.play(
                mDie, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playTransmutating() {
        mSoundPool.play(
                mTransmutating, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playAbducted() {
        mSoundPool.play(
                mAbducted, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playZombify() {
        mSoundPool.play(
                mZombify, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playControlled() {
        mSoundPool.play(
                mControl, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playController() {
        mSoundPool.play(
                mController, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playBurer() {
        mSoundPool.play(
                mBurer, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playMentalHit() {
        mSoundPool.play(
                mMentalHit, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playItemScanned()
    {
        mSoundPool.play(
                itemScanned, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                2, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playArtefact()
    {
        mSoundPool.play(
                mArtefact, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                2, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playControllerPresence() {
        Log.d(TAG, "playControllerPresence");
        if (currentPlaying == R.raw.controller_presence) {
            Log.d(TAG, "currentPlaying == R.raw.controller_presence");
            return;
        }
        if (currentPlaying > -1) {
            Log.d(TAG, "currentPlaying > -1");
            mediaPlayer.reset();
        }
        currentPlaying = R.raw.controller_presence;
        playSound(getSounUri(R.raw.controller_presence));
    }


//        mSoundPool.play(
//                mControllerPresence, // Sound
//                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
//                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
//                1, // Priority
//                0, // Loop
//                1f // Frequency
//        );
    public void playBurerPresence () {
        Log.d(TAG, "playBurerPresence");
        if (currentPlaying == R.raw.burer_presence) {
            Log.d(TAG, "currentPlaying == R.raw.burer_presence");
            return;
        }
        if (currentPlaying > -1) {
            Log.d(TAG, "currentPlaying > -1");
            mediaPlayer.reset();
        }
        currentPlaying = R.raw.burer_presence;
        playSound(getSounUri(R.raw.burer_presence));
    }

    private void playSound(Uri soundUri)
    {
        if(currentPlaying > -1)
        {
            Log.d(TAG, "mediaPlayer.isPlaying.  playSound " + soundUri);
            mediaPlayer.reset();
        }
        try
        {
            Log.d(TAG, "mediaPlayer.play " + soundUri);
            mediaPlayer.setDataSource(ctx, soundUri);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener((s) -> {this.currentPlaying = -1; this.mediaPlayer.reset();});
            mediaPlayer.start();
        }catch (Exception e)
        {
            Log.d(TAG, "exception " + soundUri);
            e.printStackTrace();
        }
    }

    private Uri getSounUri(int res)
    {
        return Uri.parse("android.resource://" + pckg + "/" + res);
    }
}
