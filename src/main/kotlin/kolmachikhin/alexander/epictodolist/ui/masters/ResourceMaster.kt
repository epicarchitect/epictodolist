package kolmachikhin.alexander.epictodolist.ui.masters

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat

class ResourceMaster(private val context: Context) {

    fun getString(res: Int): String = context.resources.getString(res)

    fun getStringArray(res: Int): Array<String> = context.resources.getStringArray(res)

    fun getColor(res: Int): Int = ContextCompat.getColor(context, res)

    fun getAttrColor(res: Int): Int {
        val value = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(res, value, true)
        return value.data
    }
}