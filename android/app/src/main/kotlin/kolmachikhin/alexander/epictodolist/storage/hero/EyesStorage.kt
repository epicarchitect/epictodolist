package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity

object EyesStorage {

    
    fun getList(gender: Int): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (i in 0 until size()) l.add(EyesStorage[gender, i])
        return l
    }

    
    fun size() = 11

    
    operator fun get(gender: Int, id: Int): Int {
        when (gender) {
            0 -> return when (id) {
                0 -> R.drawable.man_eyes_0
                1 -> R.drawable.man_eyes_1
                2 -> R.drawable.man_eyes_2
                3 -> R.drawable.man_eyes_3
                4 -> R.drawable.man_eyes_4
                5 -> R.drawable.man_eyes_5
                6 -> R.drawable.man_eyes_6
                7 -> R.drawable.man_eyes_7
                8 -> R.drawable.man_eyes_8
                9 -> R.drawable.man_eyes_9
                10 -> R.drawable.man_eyes_10
                else -> R.drawable.man_eyes_0
            }
            1 -> return when (id) {
                0 -> R.drawable.woman_eyes_0
                1 -> R.drawable.woman_eyes_1
                2 -> R.drawable.woman_eyes_2
                3 -> R.drawable.woman_eyes_3
                4 -> R.drawable.woman_eyes_4
                5 -> R.drawable.woman_eyes_5
                6 -> R.drawable.woman_eyes_6
                7 -> R.drawable.woman_eyes_7
                8 -> R.drawable.woman_eyes_8
                9 -> R.drawable.woman_eyes_9
                10 -> R.drawable.woman_eyes_10
                else -> R.drawable.woman_eyes_0
            }
        }

        if (MainActivity.core != null) {
            when (MainActivity.core!!.heroLogic.hero.gender) {
                0 -> return R.drawable.man_eyes_0
                1 -> return R.drawable.woman_eyes_0
            }
        }

        return R.drawable.ic_void
    }
}