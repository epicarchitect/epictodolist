package kolmachikhin.alexander.epictodolist.ui.masters

import android.app.Activity
import android.graphics.Point
import android.view.View
import android.view.inputmethod.InputMethodManager

class DisplayMaster(private val activity: Activity) {

    fun getViewXY(v: View): Point {
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        return Point(location[0], location[1])
    }

    val screenWidth: Int
        get() {
            val display = activity.windowManager.defaultDisplay
            val point = Point()
            display.getSize(point)
            return point.x
        }

    fun hideKeyboard() {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var v = activity.currentFocus
        if (v == null) v = View(activity)
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun dp(dp: Int) = (activity.resources.displayMetrics.density * dp).toInt()

}