package kolmachikhin.alexander.epictodolist.database.status

import android.content.Context
import androidx.core.content.edit
import kolmachikhin.alexander.epictodolist.util.json.JsonUtils

class StatusRepository internal constructor(context: Context) {

    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun save(statusModel: StatusModel) {
        preferences.edit { putString(KEY, JsonUtils.toJson(statusModel)) }
    }

    val status: StatusModel
        get() = try {
            JsonUtils.fromJson(
                preferences.getString(KEY, "")!!,
                StatusModel::class.java
            )
        } catch (t: Throwable) {
            StatusModel()
        }

    fun clearData() {
        preferences.edit { putString(KEY, "") }
    }

    companion object {
        const val NAME = "StatusRepository"
        const val KEY = "Status"

        var instance: StatusRepository? = null

        
        fun getInstance(context: Context): StatusRepository {
            if (instance == null) instance = StatusRepository(context.applicationContext)
            return instance!!
        }
    }

}