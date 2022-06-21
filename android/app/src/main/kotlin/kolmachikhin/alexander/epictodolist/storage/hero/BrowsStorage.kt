package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R

object BrowsStorage {

    
    fun getList(gender: Int): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (i in 0 until size()) l.add(BrowsStorage[gender, i])
        return l
    }

    
    fun size() = 12

    
    operator fun get(gender: Int, id: Int) = when (gender) {
        0 -> {
            when (id) {
                0 -> R.drawable.man_brows_0
                1 -> R.drawable.man_brows_1
                2 -> R.drawable.man_brows_2
                3 -> R.drawable.man_brows_3
                4 -> R.drawable.man_brows_4
                5 -> R.drawable.man_brows_5
                6 -> R.drawable.man_brows_6
                7 -> R.drawable.man_brows_7
                8 -> R.drawable.man_brows_8
                9 -> R.drawable.man_brows_9
                10 -> R.drawable.man_brows_10
                11 -> R.drawable.man_brows_11
                else -> R.drawable.ic_void
            }
        }
        1 -> {
            when (id) {
                0 -> R.drawable.woman_brows_0
                1 -> R.drawable.woman_brows_1
                2 -> R.drawable.woman_brows_2
                3 -> R.drawable.woman_brows_3
                4 -> R.drawable.woman_brows_4
                5 -> R.drawable.woman_brows_5
                6 -> R.drawable.woman_brows_6
                7 -> R.drawable.woman_brows_7
                8 -> R.drawable.woman_brows_8
                9 -> R.drawable.woman_brows_9
                10 -> R.drawable.woman_brows_10
                11 -> R.drawable.woman_brows_11
                else -> R.drawable.ic_void
            }
        }
        else -> R.drawable.ic_void
    }
}