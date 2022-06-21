package kolmachikhin.alexander.epictodolist.ui.hero

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.App.Companion.runOnBackgroundThread
import kolmachikhin.alexander.epictodolist.App.Companion.runOnMainThread
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.AppDatabase
import kolmachikhin.alexander.epictodolist.database.creator.CreatorRepository
import kolmachikhin.alexander.epictodolist.database.hero.HeroRepository
import kolmachikhin.alexander.epictodolist.database.status.StatusRepository
import kolmachikhin.alexander.epictodolist.logic.dataexport.DataExportLogic.ExportDataCallback
import kolmachikhin.alexander.epictodolist.logic.dataimport.DataImportLogic.ImportDataCallback
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.confirming.ConfirmDialog
import kolmachikhin.alexander.epictodolist.ui.masters.FontMaster
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set
import java.io.File

class SettingsFragment(container: ViewGroup) : UIFragment(R.layout.settings_fragment, container) {

    private lateinit var checkPromptNav: CheckBox
    private lateinit var checkNewTaskNotification: CheckBox
    private lateinit var buttonForgetLearned: Button
    private lateinit var buttonSkipLearned: Button
    private lateinit var buttonExportData: Button
    private lateinit var buttonImportData: Button
    private lateinit var buttonRemoveHero: Button
    private lateinit var buttonBack: ImageView
    private lateinit var buttonDone: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var mainLayout: LinearLayout
    private lateinit var fontBar: SeekBar
    private lateinit var fontExample: TextView
    private var fontScale = 1f

    @SuppressLint("SetTextI18n")
    override fun findViews() {
        mainLayout = find(R.id.main_layout)
        buttonBack = find(R.id.button_back)
        buttonDone = find(R.id.button_done)
        tvTitle = find(R.id.title)
        checkPromptNav = find(R.id.check_prompt_nav)
        checkNewTaskNotification = find(R.id.check_new_task_notification)
        buttonForgetLearned = find(R.id.button_forget_learned)
        buttonSkipLearned = find(R.id.button_skip_learned)
        buttonExportData = find(R.id.button_export_data)
        buttonImportData = find(R.id.button_import_data)
        buttonRemoveHero = find(R.id.button_remove_hero)
        fontBar = find(R.id.font_bar)
        fontExample = find(R.id.tv_font_example)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        set(buttonBack) {
            Animations.hideView(mainLayout) {
                this.remove()
            }
        }

        set(buttonExportData) {
            MainActivity.activity!!.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) { map ->
                val writeGranted = (map.containsKey(Manifest.permission.WRITE_EXTERNAL_STORAGE) && map[Manifest.permission.WRITE_EXTERNAL_STORAGE]!!)
                val readGranted = (map.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE) && map[Manifest.permission.READ_EXTERNAL_STORAGE]!!)
                if (writeGranted && readGranted) {
                    MainActivity.core!!.dataExportLogic.exportData(
                        object : ExportDataCallback {
                            override fun onCompleted(file: File?) {
                                MessageFragment.show("Saved to: " + file!!.path, 10)
                            }

                            override fun onError(throwable: Throwable?) {
                                MessageFragment.show("${MainActivity.ui!!.getString(R.string.error)}:${throwable!!.message}".trimIndent(), 10)
                            }
                        }
                    )
                }
            }
        }

        set(buttonImportData) {
            MainActivity.activity!!.launchGetContentActivity("application/json") { uri: Uri? ->
                if (uri == null) return@launchGetContentActivity

                runOnBackgroundThread {
                    try {
                        MainActivity.core!!.dataImportLogic.importData(uri, object : ImportDataCallback {
                            override fun onCompleted() {
                                MainActivity.ui!!.restart()
                            }

                            override fun onError(throwable: Throwable) {
                                runOnMainThread {
                                    MessageFragment.show("${MainActivity.ui!!.getString(R.string.error)}: ${throwable.message}".trimIndent(), 10)
                                }
                            }
                        })
                    } catch (ignored: Throwable) {
                    }
                }
            }
        }

        set(buttonDone) {
            MainActivity.ui!!.fontMaster.saveFontScale(fontScale)
            val settings = MainActivity.core!!.settingsLogic.getSettings()
            settings.showTitleNav = checkPromptNav.isChecked
            settings.showNewTaskNotification = checkNewTaskNotification.isChecked
            MainActivity.core!!.settingsLogic.update(settings)
            MainActivity.ui!!.recreate()
        }

        set(buttonForgetLearned) {
            MainActivity.core!!.creatorLogic.forgetAllLearned()
            MessageFragment.show(MainActivity.ui!!.getString(R.string.learning_is_forgotten))
        }

        set(buttonSkipLearned) {
            MainActivity.core!!.creatorLogic.skipAllLearned()
            MessageFragment.show(MainActivity.ui!!.getString(R.string.learning_missed))
        }

        set(buttonRemoveHero) {
            ConfirmDialog.open { yes1 ->
                if (yes1) {
                    ConfirmDialog.open(MainActivity.ui!!.getString(R.string.confirm_remove_hero)) { yes2 ->
                        if (yes2) {
                            AppDatabase.getInstance(context).clearAllTables()
                            HeroRepository.getInstance(context).clearData()
                            CreatorRepository.getInstance(context).clearData()
                            StatusRepository.getInstance(context).clearData()
                            MainActivity.ui!!.recreate()
                        }
                    }
                }
            }
        }

        tvTitle.text = MainActivity.ui!!.getString(R.string.settings)

        val settings = MainActivity.core!!.settingsLogic.getSettings()
        checkPromptNav.isChecked = settings.showTitleNav
        checkNewTaskNotification.isChecked = settings.showNewTaskNotification
        fontScale = MainActivity.ui!!.fontMaster.fontScale
        fontExample.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.ui!!.dp(16) * fontScale)

        when (fontScale) {
            FontMaster.FONT_SCALES[0] -> {
                fontBar.progress = 0
            }
            FontMaster.FONT_SCALES[1] -> {
                fontBar.progress = 1
            }
            FontMaster.FONT_SCALES[2] -> {
                fontBar.progress = 2
            }
            FontMaster.FONT_SCALES[3] -> {
                fontBar.progress = 3
            }
        }

        fontBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, position: Int, fromUser: Boolean) {
                fontScale = FontMaster.FONT_SCALES[position]
                fontExample.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.ui!!.dp(16) * fontScale)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    companion object {
        
        fun open(container: ViewGroup) {
            SettingsFragment(container).add()
        }
    }
}