package kolmachikhin.alexander.epictodolist.ui.graphs.holo.pie

import android.graphics.Path
import android.graphics.Region
import kolmachikhin.alexander.epictodolist.ui.graphs.holo.Utils.darkenColor

class PieSlice {
    val path = Path()
    val region = Region()
    var color = -0xcc4a1b
    private var mSelectedColor = -1
    var value = 0f
    var oldValue = 0f
    var goalValue = 0f
    var title: String? = null
    var selectedColor: Int
        get() {
            if (-1 == mSelectedColor) mSelectedColor = darkenColor(color)
            return mSelectedColor
        }
        set(selectedColor) {
            mSelectedColor = selectedColor
        }
}