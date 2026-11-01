package kolmachikhin.alexander.epictodolist.logic.consent

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AdsConsent(application: Application) {

    private val prefs: SharedPreferences =
        application.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    val personalizedAdsEnabled: Boolean
        get() = prefs.getLong(CONSENT_TIME, 0L) > 0L

    fun setPersonalizedAdsEnabled(enabled: Boolean) {
        prefs.edit(commit = true) {
            if (enabled) putLong(CONSENT_TIME, System.currentTimeMillis())
            else remove(CONSENT_TIME)
        }
    }

    companion object {
        private const val PREFS = "privacy_ads"
        private const val CONSENT_TIME = "consent_time"
    }
}
