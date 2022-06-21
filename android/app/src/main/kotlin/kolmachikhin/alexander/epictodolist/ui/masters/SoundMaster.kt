package kolmachikhin.alexander.epictodolist.ui.masters

import android.media.MediaPlayer
import android.net.Uri
import androidx.annotation.FloatRange
import androidx.appcompat.app.AppCompatActivity
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.log
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.with
import kolmachikhin.alexander.epictodolist.logic.sound.SoundLogic
import kolmachikhin.alexander.epictodolist.logic.sound.SoundLogic.Companion.getBackgroundSoundFilePath

class SoundMaster(private val activity: AppCompatActivity) {

    private var backgroundPlayer: MediaPlayer? = null
    private var backgroundPlaying = false
    private var backgroundVolume = 0.1f

    init {
        resetBackgroundPlayer()
        with(activity) { core: Core ->
            core.soundLogic.getBackgroundVolume().observe(activity) { value: Int ->
                val volume: Float = try {
                    value / 100f
                } catch (e: Exception) {
                    0f
                }
                backgroundVolume = volume
                setPlayerVolume(backgroundPlayer, volume)
            }

            core.soundLogic.observer.addListener(SoundLogic.BACKGROUND_SOUND_UPDATED) {
                resetBackgroundPlayer()
            }
        }
    }

    private fun resetBackgroundPlayer() {
        try {
            backgroundPlayer?.release()
            backgroundPlayer = MediaPlayer.create(activity, Uri.parse(getBackgroundSoundFilePath(activity)))
            backgroundPlayer?.isLooping = true
            setPlayerVolume(backgroundPlayer, backgroundVolume)
            if (backgroundPlaying) backgroundPlayer?.start()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun playBackground() {
        try {
            if (!backgroundPlayer!!.isPlaying) {
                backgroundPlayer!!.start()
                backgroundPlaying = true
                log("SoundMaster::playBackground")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun pauseBackground() {
        try {
            if (backgroundPlayer!!.isPlaying) {
                backgroundPlayer!!.pause()
                backgroundPlaying = false
                log("SoundMaster::pauseBackground")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun setPlayerVolume(player: MediaPlayer?, @FloatRange(from = 0.0, to = 1.0) volume: Float) {
        try {
            player!!.setVolume(volume, volume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}