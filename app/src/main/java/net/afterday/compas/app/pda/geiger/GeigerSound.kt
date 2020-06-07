package net.afterday.compas.app.pda.geiger

import android.animation.ValueAnimator
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.view.animation.LinearInterpolator
import net.afterday.compas.app.R
import net.afterday.compas.engine.engine.Engine

class GeigerSound(ctx: Context) {
    private val mSoundPool: SoundPool = SoundPool(16, AudioManager.STREAM_MUSIC, 32)
    private val soundAnimator: ValueAnimator = ValueAnimator.ofInt(0, 60)

    // Load sound
    private val mRadTick = mSoundPool.load(ctx, R.raw.rad_click, 1)

    fun playRad(strength: Double) {
        if (strength == 0.0) {
            return
        }
        val vol = Math.random().toFloat() * 0.1f + 0.5f // From 0.5 to 0.6
        val freq = Math.random().toFloat() * 0.2f + 0.9f // From 0.9 to 1.1
        mSoundPool.play(mRadTick, vol, vol, 2, 0, freq)
        /*soundAnimator.cancel()
        soundAnimator.duration = 10000
        soundAnimator.interpolator = LinearInterpolator()
        soundAnimator.addUpdateListener { v: ValueAnimator? ->

            val rand = Math.random()
            val probability = Math.min(strength / 17.0, 0.9)
            if (rand <= probability) {
                mSoundPool.play(mRadTick, vol, vol, 2, 0, freq)
            }
        }
        soundAnimator.start()*/
    }
}