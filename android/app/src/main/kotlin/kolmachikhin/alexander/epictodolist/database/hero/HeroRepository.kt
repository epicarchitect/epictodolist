package kolmachikhin.alexander.epictodolist.database.hero

import android.content.Context
import androidx.core.content.edit
import kolmachikhin.alexander.epictodolist.util.json.JsonUtils

class HeroRepository internal constructor(context: Context) {
    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun save(hero: HeroModel) {
        preferences.edit { putString(KEY, JsonUtils.toJson(hero)) }
    }

    val hero: HeroModel?
        get() = preferences.getString(KEY, "").let { json ->
            if (json.isNullOrEmpty()) {
                null
            } else {
                JsonUtils.fromJson(json, HeroModel::class.java)
            }
        }

    fun clearData() {
        preferences.edit { putString(KEY, "") }
    }

    companion object {
        const val NAME = "HeroRepository"
        const val KEY = "Hero"

        var instance: HeroRepository? = null

        
        fun getInstance(context: Context): HeroRepository {
            if (instance == null) instance = HeroRepository(context)
            return instance!!
        }
    }

}