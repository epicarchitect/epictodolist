package kolmachikhin.alexander.epictodolist.logic.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity

object ProductType {
    const val COSTUME = 0
    const val THEME = 1
    const val WEAPON = 2
    const val LOCATION = 3
    const val FEATURE = 4

    
    fun toString(t: Int) = when (t) {
        COSTUME -> s(R.string.costume)
        THEME -> s(R.string.theme)
        WEAPON -> s(R.string.weapon)
        LOCATION -> s(R.string.location)
        FEATURE -> s(R.string.feature)
        else -> ""
    }

    private fun s(id: Int): String {
        return MainActivity.ui!!.getString(id)
    }
}