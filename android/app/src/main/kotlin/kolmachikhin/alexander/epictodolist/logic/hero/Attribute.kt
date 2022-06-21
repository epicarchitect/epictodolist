package kolmachikhin.alexander.epictodolist.logic.hero

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity

object Attribute {
    const val STRENGTH = 0
    const val INTELLECT = 1
    const val CREATION = 2
    const val HEALTH = 3

    
    fun toString(i: Int) = when (i) {
        STRENGTH -> MainActivity.ui!!.getString(R.string.strength)
        INTELLECT -> MainActivity.ui!!.getString(R.string.intellect)
        CREATION -> MainActivity.ui!!.getString(R.string.creation)
        HEALTH -> MainActivity.ui!!.getString(R.string.health)
        else -> ""
    }
}