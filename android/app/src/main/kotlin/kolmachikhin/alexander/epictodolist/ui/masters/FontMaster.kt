package kolmachikhin.alexander.epictodolist.ui.masters

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity

class FontMaster(private val activity: AppCompatActivity) {

    fun saveFontScale(scale: Float) {
        val preferences = activity.getSharedPreferences(KEY_FONT_SCALE, Context.MODE_PRIVATE)
        preferences.edit().putFloat(KEY_FONT_SCALE, scale).apply()
    }

    val fontScale: Float
        get() {
            val preferences = activity.getSharedPreferences(KEY_FONT_SCALE, Context.MODE_PRIVATE)
            return preferences.getFloat(KEY_FONT_SCALE, 1f)
        }

    fun attachFontScale() {
        val fontScale = fontScale
        val res = activity.resources
        val configuration = Configuration(res.configuration)
        configuration.fontScale = fontScale
        res.updateConfiguration(configuration, res.displayMetrics)
    }

    companion object {
        const val KEY_FONT_SCALE = "font_scale"

        
        val FONT_SCALES = floatArrayOf(
            1f, 1.1f, 1.2f, 1.3f
        )
    }
}