package kolmachikhin.alexander.epictodolist

import android.app.Application
import android.os.Handler
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
    }

    companion object {
        private lateinit var instance: App

        fun runOnMainThread(runnable: Runnable) {
            instance.mainThreadHandler.post(runnable)
        }

        fun runOnBackgroundThread(runnable: Runnable) {
            instance.executorService.submit(runnable)
        }
        
        fun runRepeatingBackgroundThread(runnable: Runnable, delay: Int) {
            Executors.newSingleThreadExecutor().submit<Any> {
                while (true) {
                    try {
                        runnable.run()
                        Thread.sleep(delay.toLong())
                    } catch (ignored: Throwable) {
                    }
                }
            }
        }
    }
}