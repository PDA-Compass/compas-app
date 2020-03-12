package net.afterday.compas.devices.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

import net.afterday.compas.R;
import net.afterday.compas.core.Controls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.Observable;

public class Sound {
    private int mRadTick;
    private int mEmissionStarts;
    private int mEmissionWarning;
    private int mEmissionHit;
    private int mEmissionPeriodical;
    private int mEmissionEnds;
    private int mAnomalyTick;
    private int mMental;
    private int mAnomalyDeath;
    private int mDie;
    private int mZombify;
    private int mControl;
    private int mBurer;
    private int mController;
    private int mHealing;
    private int mGlassBreak;
	private int mBulbBreak;
    private int mLevelUp;
    private int mCompassOn;
    private int mCompassOff;
    private int mInventoryOpen;
    private int mInventoryDrop;
    private int mInventoryUse;
    private int mInventoryClose;
    private int mPdaOn;
    private int mPdaOff;
    private int mPdaWake;
    private int mControllerPresence;
    private int mBurerPresence;
    private int mTransmutating;
    private int mWTimer;
    private int mMentalHit;
    private int mMonolithHit;
    private int mAbducted;
    private int mItemScanned;
    private int mArtefact;
    private MediaPlayer burrerPlayer = new MediaPlayer();
    private MediaPlayer controllerPlayer = new MediaPlayer();
    private static final String TAG = "SOUND";
    private SoundPool mSoundPool;
    private AudioManager mAudioManager;
    private String pckg;
    private Context ctx;
    private int currentPlaying = -1;
    private long hStarted = 0;
    private boolean burrerPlaying;
    private boolean controllerPlaying;

    public Sound(Context ctx) {
        this.ctx = ctx;
        pckg = ctx.getPackageName();

        mSoundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 32);
        mAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);

        preparePlayer(burrerPlayer, R.raw.burer_presence, (s) -> {burrerPlaying = false;});
        preparePlayer(controllerPlayer, R.raw.controller_presence, (s) -> {controllerPlaying = false;});

        // Load samples
        mRadTick = mSoundPool.load(ctx, R.raw.rad_click, 1);
        mAnomalyTick = mSoundPool.load(ctx, R.raw.anomaly, 1);
        mMental = mSoundPool.load(ctx, R.raw.mental, 1);
        mEmissionStarts = mSoundPool.load(ctx, R.raw.pda_emission_begins, 1);
        mEmissionWarning = mSoundPool.load(ctx, R.raw.pda_emission_warning, 1);
        mEmissionHit = mSoundPool.load(ctx, R.raw.emission_hit, 1);
        mEmissionPeriodical = mSoundPool.load(ctx, R.raw.emission_periodical, 1);
        mEmissionEnds = mSoundPool.load(ctx, R.raw.pda_emission_ends, 1);
        mAnomalyDeath = mSoundPool.load(ctx, R.raw.ano_kill, 1);
        mDie = mSoundPool.load(ctx, R.raw.die, 1);
        mZombify = mSoundPool.load(ctx, R.raw.zombified, 1);
        mControl = mSoundPool.load(ctx, R.raw.controlled, 1);
        mBurer = mSoundPool.load(ctx, R.raw.burer, 1);
        mController = mSoundPool.load(ctx, R.raw.controller, 1);
        mControllerPresence = mSoundPool.load(ctx, R.raw.controller_presence, 1);
        mHealing = mSoundPool.load(ctx, R.raw.healing, 1);
        mGlassBreak = mSoundPool.load(ctx, R.raw.glass_break, 1);
		mBulbBreak = mSoundPool.load(ctx, R.raw.bulb_break, 1);
        mLevelUp = mSoundPool.load(ctx, R.raw.pda_level_up, 1);
        mCompassOn = mSoundPool.load(ctx, R.raw.compass_on, 1);
        mCompassOff = mSoundPool.load(ctx, R.raw.compass_off, 1);
        mInventoryOpen = mSoundPool.load(ctx, R.raw.inv_open, 1);
        mInventoryDrop = mSoundPool.load(ctx, R.raw.inv_drop, 1);
        mInventoryUse = mSoundPool.load(ctx, R.raw.inv_use, 1);
        mInventoryClose = mSoundPool.load(ctx, R.raw.inv_close, 1);
        mPdaOn = mSoundPool.load(ctx, R.raw.pda_app_start, 1);
        mPdaOff = mSoundPool.load(ctx, R.raw.pda_app_stop, 1);
        mPdaWake = mSoundPool.load(ctx, R.raw.pda_app_wake_up, 1);
        mBurerPresence = mSoundPool.load(ctx, R.raw.burer_presence, 1);
        mTransmutating = mSoundPool.load(ctx, R.raw.transmutating, 1);
        mWTimer = mSoundPool.load(ctx, R.raw.w_timer_begins, 1);
        mMentalHit = mSoundPool.load(ctx, R.raw.mental_hit, 1);
        mMonolithHit = mSoundPool.load(ctx, R.raw.monolith_call_1, 1);
        mAbducted = mSoundPool.load(ctx, R.raw.abducted, 1);
        mItemScanned = mSoundPool.load(ctx, R.raw.pda_qr_scanned, 1);
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
        mSoundPool.play(mGlassBreak, 1f, 1f, 2, 0, 1f);
    }

    public void playBulbBreak()
    {
        mSoundPool.play(mBulbBreak, 1f, 1f, 2, 0, 1f);
    }
    public void playLevelUp()
    {
        mSoundPool.play(mLevelUp, 1f, 1f, 2, 0, 1f);
    }

    public void playHealing()
    {
        mSoundPool.play(mHealing,
                1f,
                1f,
                2,
                0,
                1f);

    }

    public void stopHealing()
    {
        mSoundPool.stop(mHealing);
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
                mEmissionWarning, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playEmissionStarts() {
        mSoundPool.play(
                mEmissionStarts, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playEmissionHit() {
        mSoundPool.play(
                mEmissionHit, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playEmissionPeriodical() {
        mSoundPool.play(
                mEmissionPeriodical, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playEmissionEnds() {
        mSoundPool.play(
                mEmissionEnds, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playCompassOn() {
        mSoundPool.play(
                mCompassOn, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playCompassOff() {
        mSoundPool.play(
                mCompassOff, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playInventoryOpen() {
        mSoundPool.play(
                mInventoryOpen, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playInventoryDrop() {
        mSoundPool.play(
                mInventoryDrop, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playInventoryUse() {
        mSoundPool.play(
                mInventoryUse, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playInventoryClose() {
        mSoundPool.play(
                mInventoryClose, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playPdaOn() {
        mSoundPool.play(
                mPdaOn, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playPdaOff() {
        mSoundPool.play(
                mPdaOff, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                1, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playPdaWake() {
        mSoundPool.play(
                mPdaWake, // Sound
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
                2, // Priority
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

    public void playMonolithHit() {
        mSoundPool.play(
                mMonolithHit, // Sound
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Left volume
                1f, //mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC),// / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Right volume
                2, // Priority
                0, // Loop
                1f // Frequency
        );
    }

    public void playItemScanned()
    {
        mSoundPool.play(
                mItemScanned, // Sound
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
        if(!controllerPlaying)
        {
            this.controllerPlaying = true;
            controllerPlayer.start();
        }
    }

    public void playBurerPresence () {
        if(!burrerPlaying)
        {
            burrerPlaying = true;
            burrerPlayer.start();
        }
    }

    private boolean preparePlayer(MediaPlayer mediaPlayer, int resId, MediaPlayer.OnCompletionListener mp)
    {
        boolean prepared;
        try
        {
            mediaPlayer.setDataSource(ctx, getSounUri(resId));
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp);
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener()
            {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1)
                {
                    mp.onCompletion(mediaPlayer);
                    return true;
                }
            });
            prepared = true;
        }catch (Exception e)
        {
            prepared = false;
        }
        return prepared;
    }

    private Uri getSounUri(int res)
    {
        return Uri.parse("android.resource://" + pckg + "/" + res);
    }
}
