package kolmachikhin.alexander.epictodolist.ui.graphs.holo

import android.graphics.Color

object Utils {
    
    fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.8f
        return Color.HSVToColor(hsv)
    }
}