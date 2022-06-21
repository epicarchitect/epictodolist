package kolmachikhin.alexander.epictodolist.database.creator

import android.content.Context
import androidx.core.content.edit
import kolmachikhin.alexander.epictodolist.util.json.JsonUtils

class CreatorRepository internal constructor(context: Context) {

    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun save(creator: CreatorModel) {
        preferences.edit {
            putString(KEY, JsonUtils.toJson(creator))
        }
    }

    val creator: CreatorModel
        get() = try {
            JsonUtils.fromJson(
                preferences.getString(KEY, "")!!,
                CreatorModel::class.java
            )
        } catch (th: Throwable) {
            CreatorModel()
        }

    fun clearData() {
        preferences.edit { putString(KEY, "") }
    }

    companion object {
        private const val NAME = "CreatorRepository"
        private const val KEY = "Creator"

        var instance: CreatorRepository? = null

        
        fun getInstance(context: Context): CreatorRepository {
            if (instance == null) instance = CreatorRepository(context.applicationContext)
            return instance!!
        }
    }

}