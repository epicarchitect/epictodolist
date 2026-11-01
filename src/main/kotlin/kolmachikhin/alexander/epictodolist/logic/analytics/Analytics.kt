package kolmachikhin.alexander.epictodolist.logic.analytics

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kolmachikhin.alexander.epictodolist.BuildConfig
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

class Analytics(application: Application) {

    private val prefs: SharedPreferences =
        application.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    val analyticsEnabled: Boolean
        get() = prefs.getLong(CONSENT_TIME, 0L) > 0L

    init {
        val config = AppMetricaConfig.newConfigBuilder(BuildConfig.APPMETRICA_API_KEY)
            .withDataSendingEnabled(false)
            .withCrashReporting(true)
            .withNativeCrashReporting(false)
            .withAnrMonitoring(true)
            .withLocationTracking(false)
            .withAdvIdentifiersTracking(false)
            .build()
        AppMetrica.activate(application, config)
        AppMetrica.enableActivityAutoTracking(application)
        AppMetrica.setLocationTracking(false)
        AppMetrica.setAdvIdentifiersTracking(false)
        AppMetrica.setDataSendingEnabled(analyticsEnabled)
    }

    fun setAnalyticsEnabled(enabled: Boolean) {
        prefs.edit(commit = true) {
            if (enabled) putLong(CONSENT_TIME, System.currentTimeMillis())
            else remove(CONSENT_TIME)
        }
        AppMetrica.setDataSendingEnabled(enabled)
    }

    companion object {
        private const val PREFS = "privacy_analytics"
        private const val CONSENT_TIME = "consent_time"
    }
}
