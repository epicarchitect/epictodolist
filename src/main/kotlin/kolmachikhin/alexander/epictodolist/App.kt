package kolmachikhin.alexander.epictodolist

import android.app.Application
import android.os.Handler
import kolmachikhin.alexander.epictodolist.database.migrations.LegacyStateMigration
import kolmachikhin.alexander.epictodolist.logic.analytics.Analytics
import kolmachikhin.alexander.epictodolist.logic.consent.AdsConsent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class App : Application() {

    private lateinit var executorService: ExecutorService
    private lateinit var mainThreadHandler: Handler

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        executorService = Executors.newCachedThreadPool()
        mainThreadHandler = Handler(mainLooper)
        LegacyStateMigration.migrate(this)
        analytics = Analytics(this)
        adsConsent = AdsConsent(this)
    }

    companion object {
        private lateinit var instance: App

        lateinit var analytics: Analytics
            private set

        lateinit var adsConsent: AdsConsent
            private set

        fun runOnMainThread(runnable: Runnable) {
            instance.mainThreadHandler.post(runnable)
        }

        fun runOnBackgroundThread(runnable: Runnable) {
            instance.executorService.submit(runnable)
        }
    }
}
