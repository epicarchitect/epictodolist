package kolmachikhin.alexander.epictodolist.ui.masters

import android.content.Context
import androidx.core.content.edit
import androidx.appcompat.app.AppCompatActivity

class FontMaster(private val activity: AppCompatActivity) {

    fun saveFontScale(scale: Float) {
        val preferences = activity.getSharedPreferences(KEY_FONT_SCALE, Context.MODE_PRIVATE)
        preferences.edit { putFloat(KEY_FONT_SCALE, scale) }
    }

    val fontScale: Float
        get() {
            val preferences = activity.getSharedPreferences(KEY_FONT_SCALE, Context.MODE_PRIVATE)
            return preferences.getFloat(KEY_FONT_SCALE, 1f)
        }

    companion object {
        const val KEY_FONT_SCALE = "font_scale"

        val FONT_SCALES = floatArrayOf(
            1f,
            1.1f,
            1.2f,
            1.3f
        )
    }
}
