package kolmachikhin.alexander.epictodolist.database.settings

import android.content.Context
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class SettingsRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).settingsDao()

    fun save(settings: SettingsModel) {
        dao.save(settings)
    }

    val settings: SettingsModel
        get() = dao.get() ?: SettingsModel()

    fun clearData() {
        dao.clear()
    }

    companion object {
        var instance: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository {
            if (instance == null) instance = SettingsRepository(context.applicationContext)
            return instance!!
        }
    }
}
