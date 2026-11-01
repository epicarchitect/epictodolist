package kolmachikhin.alexander.epictodolist.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.appwidget.tasks.TaskListWidget
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.with
import kolmachikhin.alexander.epictodolist.storage.products.FeaturesStorage
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.hero.HeroNav
import kolmachikhin.alexander.epictodolist.ui.hero.HeroTopBarFragment
import kolmachikhin.alexander.epictodolist.ui.hero.maker.HeroMakerDialog
import kolmachikhin.alexander.epictodolist.ui.loading.LoadingFragment
import kolmachikhin.alexander.epictodolist.ui.masters.FontMaster
import kolmachikhin.alexander.epictodolist.ui.products.ProductDialog

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {

    private var loadingFragment: LoadingFragment? = null

    private val requestPermissionsLauncherCallbacks =
        ArrayList<ActivityResultCallback<Map<String, Boolean>>>()
    private val requestPermissionsLauncher =
        registerForActivityResult(RequestMultiplePermissions()) { isGrantedMap ->
            for (callback in requestPermissionsLauncherCallbacks) {
                callback.onActivityResult(isGrantedMap)
            }
            requestPermissionsLauncherCallbacks.clear()
        }

    private val getContentActivityLauncherCallbacks = ArrayList<ActivityResultCallback<Uri?>>()
    private val getContentActivityLauncher = registerForActivityResult(GetContent()) { uri ->
        for (callback in getContentActivityLauncherCallbacks) {
            callback.onActivityResult(uri)
        }
        getContentActivityLauncherCallbacks.clear()
    }

    fun requestPermissions(
        permissions: Array<String>,
        callback: ActivityResultCallback<Map<String, Boolean>>
    ) {
        requestPermissionsLauncherCallbacks.add(callback)
        requestPermissionsLauncher.launch(permissions)
    }

    fun launchGetContentActivity(input: String, callback: ActivityResultCallback<Uri?>) {
        getContentActivityLauncherCallbacks.add(callback)
        getContentActivityLauncher.launch(input)
    }

    override fun attachBaseContext(newBase: Context) {
        val fontScale = newBase
            .getSharedPreferences(FontMaster.KEY_FONT_SCALE, Context.MODE_PRIVATE)
            .getFloat(FontMaster.KEY_FONT_SCALE, 1f)
        val configuration = Configuration(newBase.resources.configuration)
        configuration.fontScale = fontScale
        super.attachBaseContext(newBase.createConfigurationContext(configuration))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)
        activity = this
        ui = UI(this)
        ui!!.themeMaster.attachTheme()
        setContentView(R.layout.main_activity)
        heroTopBarContainer = findViewById(R.id.hero_top_bar_container)
        navContainer = findViewById(R.id.nav_container)
        navigationContainer = findViewById(R.id.navigation_container)
        dialogContainer = findViewById(R.id.dialog_container)
        messageContainer = findViewById(R.id.message_container)
        creatorContainer = findViewById(R.id.creator_container)
        Animations.init(this)
        loadingFragment = LoadingFragment(dialogContainer!!)
        loadingFragment!!.open()
        HeroTopBarFragment.init()
        MainNavigationFragment.init()
        initCore()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (ui != null && ui!!.fragmentStackMaster.remove()) {
                    return
                }
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        })
    }

    private fun initCore() {
        with(this) {
            core = it
            HeroTopBarFragment.setAfterCoreInit()
            MainNavigationFragment.setAfterCoreInit()
            val needsHeroCreation = !core!!.heroLogic.isHeroCreated
            if (!needsHeroCreation) {
                checkIntentFromTaskListToUnlock(core)
            }
            loadingFragment!!.close()
            if (needsHeroCreation) {
                HeroMakerDialog.open(dialogContainer!!)
            }
            core!!.taskListWidgetLogic.update()
            core!!.restartLogic.checkAll()
            core!!.statusLogic.startTimeUsing()

            if (isPostNotificationsShouldBeRequested()) {
                requestPostNotificationsPermission()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (core != null) core!!.statusLogic.endTimeUsing()
        ui!!.soundMaster.pauseBackground()
    }

    override fun onResume() {
        super.onResume()
        if (core != null) core!!.statusLogic.startTimeUsing()
        ui!!.soundMaster.playBackground()
    }

    private fun checkIntentFromTaskListToUnlock(core: Core?) {
        val intent = intent
        val action = intent.action
        if (action != null && action == TaskListWidget.UNLOCK) {
            MainNavigationFragment.select(MainNavigationFragment.HERO)
            val nav = MainNavigationFragment.instance!!.selectedNav
            if (nav is HeroNav) {
                nav.select(HeroNav.INVENTORY)
                ProductDialog.open(
                    core!!.productsLogic.getFeature(FeaturesStorage.TASK_LIST_WIDGET_ID),
                    ProductDialog.FROM_INVENTORY
                ) { _, _ ->
                    nav.select(HeroNav.INVENTORY)
                }
            }
        }
    }

    companion object {
        var activity: MainActivity? = null
        var ui: UI? = null
        var core: Core? = null
        var heroTopBarContainer: FrameLayout? = null
        var navContainer: FrameLayout? = null
        var navigationContainer: FrameLayout? = null
        var dialogContainer: FrameLayout? = null
        var messageContainer: FrameLayout? = null
        var creatorContainer: FrameLayout? = null

        fun isPostNotificationsShouldBeRequested(): Boolean {
            val productsLogic = core!!.productsLogic
            return productsLogic.isNotificationsUnlocked ||
                    productsLogic.isChallengesUnlocked ||
                    productsLogic.isRepeatableTasksUnlocked
        }

        fun requestPostNotificationsPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity!!.requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                ) {
                }
            }
        }
    }
}