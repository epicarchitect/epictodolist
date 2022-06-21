package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity

object EarsStorage {

    
    fun getList(gender: Int): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (i in 0 until size()) l.add(EarsStorage[gender, i])
        return l
    }

    
    fun size() = 3

    
    operator fun get(gender: Int, id: Int): Int {
        when (gender) {
            0 -> return when (id) {
                0 -> R.drawable.man_ears_0
                1 -> R.drawable.man_ears_1
                2 -> R.drawable.man_ears_2
                else -> R.drawable.man_ears_0
            }
            1 -> return when (id) {
                0 -> R.drawable.woman_ears_0
                1 -> R.drawable.woman_ears_1
                2 -> R.drawable.woman_ears_2
                else -> R.drawable.woman_ears_0
            }
        }

        if (MainActivity.core != null) {
            when (MainActivity.core!!.heroLogic.hero.gender) {
                0 -> return R.drawable.man_ears_0
                1 -> return R.drawable.woman_ears_0
            }
        }

        return R.drawable.ic_void
    }
}