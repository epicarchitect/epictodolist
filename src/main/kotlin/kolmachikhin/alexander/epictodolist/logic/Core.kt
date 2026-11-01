package kolmachikhin.alexander.epictodolist.logic

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import kolmachikhin.alexander.epictodolist.App.Companion.runOnMainThread
import kolmachikhin.alexander.epictodolist.logic.achievements.AchievementsLogic
import kolmachikhin.alexander.epictodolist.logic.ads.AdLogic
import kolmachikhin.alexander.epictodolist.logic.appstate.AppStateLogic
import kolmachikhin.alexander.epictodolist.logic.billing.BillingLogic
import kolmachikhin.alexander.epictodolist.logic.challenges.ChallengesLogic
import kolmachikhin.alexander.epictodolist.logic.creator.CreatorLogic
import kolmachikhin.alexander.epictodolist.logic.dataexport.DataExportLogic
import kolmachikhin.alexander.epictodolist.logic.dataimport.DataImportLogic
import kolmachikhin.alexander.epictodolist.logic.hero.HeroLogic
import kolmachikhin.alexander.epictodolist.logic.notifications.NotificationsLogic
import kolmachikhin.alexander.epictodolist.logic.products.ProductsLogic
import kolmachikhin.alexander.epictodolist.logic.restarting.RestartLogic
import kolmachikhin.alexander.epictodolist.logic.settings.SettingsLogic
import kolmachikhin.alexander.epictodolist.logic.skills.SkillsLogic
import kolmachikhin.alexander.epictodolist.logic.sound.SoundLogic
import kolmachikhin.alexander.epictodolist.logic.status.StatusLogic
import kolmachikhin.alexander.epictodolist.logic.tasks.completed.CompletedTasksLogic
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic
import kolmachikhin.alexander.epictodolist.logic.tasks.repeatable.RepeatableTasksLogic
import kolmachikhin.alexander.epictodolist.logic.widgets.tasks.TaskListWidgetLogic
import kolmachikhin.alexander.epictodolist.ui.MainActivity

@SuppressLint("StaticFieldLeak")
class Core private constructor(applicationContext: Context) {

    val applicationContext: Context = applicationContext.applicationContext
    private val onReadyListeners = ArrayList<OnReadyListener>()
    private val logics = ArrayList<Logic>()

    lateinit var statusLogic: StatusLogic
    lateinit var skillsLogic: SkillsLogic
    lateinit var notificationsLogic: NotificationsLogic
    lateinit var heroLogic: HeroLogic
    lateinit var currentTasksLogic: CurrentTasksLogic
    lateinit var repeatableTasksLogic: RepeatableTasksLogic
    lateinit var completedTasksLogic: CompletedTasksLogic
    lateinit var achievementsLogic: AchievementsLogic
    lateinit var productsLogic: ProductsLogic
    lateinit var taskListWidgetLogic: TaskListWidgetLogic
    lateinit var settingsLogic: SettingsLogic
    lateinit var soundLogic: SoundLogic
    lateinit var restartLogic: RestartLogic
    lateinit var creatorLogic: CreatorLogic
    lateinit var adLogic: AdLogic
    lateinit var challengesLogic: ChallengesLogic
    lateinit var billingLogic: BillingLogic
    lateinit var dataExportLogic: DataExportLogic
    lateinit var dataImportLogic: DataImportLogic
    lateinit var appStateLogic: AppStateLogic

    init {
        Log.d("Core", "Core::init")
        initLogic()
        postInitLogic()
        setLogicOnReadyListeners()
        loadData()
    }

    @Synchronized
    fun notifyOnReadyListeners() {
        for (l in onReadyListeners) {
            l.onReady(instance!!)
        }
        onReadyListeners.clear()
    }

    private fun addOnReadyListener(onReadyListener: OnReadyListener?) {
        if (onReadyListener != null) {
            onReadyListeners.add(onReadyListener)
            if (isReady) notifyOnReadyListeners()
        }
    }

    fun interface OnReadyListener {
        fun onReady(core: Core)
    }

    private fun initLogic() {
        statusLogic = StatusLogic(this)
        skillsLogic = SkillsLogic(this)
        notificationsLogic = NotificationsLogic(this)
        heroLogic = HeroLogic(this)
        currentTasksLogic = CurrentTasksLogic(this)
        repeatableTasksLogic = RepeatableTasksLogic(this)
        completedTasksLogic = CompletedTasksLogic(this)
        achievementsLogic = AchievementsLogic(this)
        productsLogic = ProductsLogic(this)
        taskListWidgetLogic = TaskListWidgetLogic(this)
        settingsLogic = SettingsLogic(this)
        soundLogic = SoundLogic(this)
        restartLogic = RestartLogic(this)
        creatorLogic = CreatorLogic(this)
        adLogic = AdLogic(this)
        challengesLogic = ChallengesLogic(this)
        billingLogic = BillingLogic(this)
        dataExportLogic = DataExportLogic(this)
        dataImportLogic = DataImportLogic(this)
        appStateLogic = AppStateLogic(this)
        logics.add(statusLogic)
        logics.add(skillsLogic)
        logics.add(notificationsLogic)
        logics.add(heroLogic)
        logics.add(currentTasksLogic)
        logics.add(repeatableTasksLogic)
        logics.add(completedTasksLogic)
        logics.add(achievementsLogic)
        logics.add(productsLogic)
        logics.add(taskListWidgetLogic)
        logics.add(settingsLogic)
        logics.add(soundLogic)
        logics.add(restartLogic)
        logics.add(creatorLogic)
        logics.add(adLogic)
        logics.add(challengesLogic)
        logics.add(billingLogic)
        logics.add(dataExportLogic)
        logics.add(dataImportLogic)
        logics.add(appStateLogic)
    }

    private fun postInitLogic() {
        for (l in logics) l.postInit()
    }

    private fun setLogicOnReadyListeners() {
        for (l in logics) {
            l.setOnReadyListener {
                if (isReady) {
                    notifyOnReadyListeners()
                }
            }
        }
    }

    private fun loadData() {
        attachReferences()
    }

    private val isReady: Boolean
        get() {
            var isReady = true
            for (l in logics) {
                if (!l.isReady) {
                    isReady = false
                    break
                }
            }
            return isReady
        }

    private fun attachReferences() {
        for (l in logics) l.attachRef()
    }

    val isOnline: Boolean
        get() {
            val connectivityManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }

    companion object {
        private var instance: Core? = null

        /**
         * DUNGEROS
         * KOSTIL FOR ACHIEVEMENTS AND PRODUCTS STORAGE
         */

        val context: Context?
            get() {
                var c: Context? = MainActivity.activity
                if (c == null && instance != null) c = instance!!.applicationContext
                return c
            }

        fun with(context: Context, onReadyListener: OnReadyListener? = null): Core {
            if (instance == null || instance!!.applicationContext !== context.applicationContext) {
                instance =
                    Core(context)
            }
            instance!!.addOnReadyListener(onReadyListener)
            return instance!!
        }

        fun destroy() {
            instance = null
        }

        fun say(o: Any) {
            try {
                runOnMainThread {
                    Toast.makeText(
                        instance!!.applicationContext,
                        o.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            log("say: $o")
        }

        fun log(o: Any?) {
            if (o != null) {
                Log.d("EpicCore", o.toString())
            } else {
                Log.d("EpicCore", "null")
            }
        }
    }
}