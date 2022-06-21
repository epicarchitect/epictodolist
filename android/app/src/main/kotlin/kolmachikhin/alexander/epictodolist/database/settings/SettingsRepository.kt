package kolmachikhin.alexander.epictodolist.database.settings

import android.content.Context
import androidx.core.content.edit
import kolmachikhin.alexander.epictodolist.util.json.JsonUtils

class SettingsRepository internal constructor(context: Context) {

    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun save(creator: SettingsModel) {
        preferences.edit { putString(KEY, JsonUtils.toJson(creator)) }
    }

    val settings: SettingsModel
        get() = try {
            JsonUtils.fromJson(
                preferences.getString(KEY, "")!!,
                SettingsModel::class.java
            )
        } catch (th: Throwable) {
            SettingsModel()
        }

    fun clearData() {
        preferences.edit { putString(KEY, "") }
    }

    companion object {
        const val NAME = "SettingsRepository"
        const val KEY = "Settings"

        var instance: SettingsRepository? = null

        
        fun getInstance(context: Context): SettingsRepository {
            if (instance == null) instance = SettingsRepository(context.applicationContext)
            return instance!!
        }
    }
}