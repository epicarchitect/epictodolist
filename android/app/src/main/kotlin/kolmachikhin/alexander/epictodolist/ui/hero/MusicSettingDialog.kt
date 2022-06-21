package kolmachikhin.alexander.epictodolist.ui.hero

import android.Manifest
import android.net.Uri
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.App.Companion.runOnBackgroundThread
import kolmachikhin.alexander.epictodolist.App.Companion.runOnMainThread
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class MusicSettingDialog : Dialog<Any?>(R.layout.music_settings_dialog) {

    private lateinit var selectBackgroundSoundButton: Button
    private lateinit var backgroundSoundSeekbar: SeekBar
    private lateinit var tvBackgroundVolume: TextView

    override fun findViews() {
        super.findViews()
        tvBackgroundVolume = find(R.id.tv_background_volume)
        selectBackgroundSoundButton = find(R.id.button_select_background_sound)
        backgroundSoundSeekbar = find(R.id.seekbar_background_sound)
    }

    override fun start() {
        super.start()
        tvTitle?.text = context.getString(R.string.music_settings_dialog_title)
        backgroundSoundSeekbar.max = 100
        backgroundSoundSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tvBackgroundVolume.text = context.getString(R.string.music_settings_dialog_background_volume, progress)
                MainActivity.core!!.soundLogic.updateBackgroundVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        set(selectBackgroundSoundButton) {
            MainActivity.activity!!.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)) { map ->
                if (map[Manifest.permission.WRITE_EXTERNAL_STORAGE]!!) {
                    startBackgroundSoundSelection()
                } else {
                    MessageFragment.show(context.getString(R.string.music_settings_dialog_files_access_denied))
                }
            }
        }
        val progress = MainActivity.core!!.soundLogic.getBackgroundVolume().value!!
        backgroundSoundSeekbar.progress = progress
        tvBackgroundVolume.text = context.getString(R.string.music_settings_dialog_background_volume, progress)
    }

    private fun startBackgroundSoundSelection() {
        MainActivity.activity!!.launchGetContentActivity("audio/*") { uri: Uri? ->
            runOnBackgroundThread {
                try {
                    MainActivity.core!!.soundLogic.updateBackgroundFileFromContentUri(uri)
                    runOnMainThread { MessageFragment.show(context.getString(R.string.music_settings_dialog_file_loaded)) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnMainThread { MessageFragment.show(context.getString(R.string.music_settings_dialog_file_not_loaded)) }
                }
            }
        }
    }

    companion object {
        
        fun open() {
            MusicSettingDialog().openDialog()
        }
    }
}