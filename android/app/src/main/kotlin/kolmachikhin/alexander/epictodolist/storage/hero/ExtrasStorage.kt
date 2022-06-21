package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R

object ExtrasStorage {

    
    fun getList(gender: Int): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (i in 0 until size()) l.add(ExtrasStorage[gender, i])
        return l
    }

    
    fun size() = 6

    
    operator fun get(gender: Int, id: Int) = when (gender) {
        0 -> when (id) {
            0 -> R.drawable.ic_void
            1 -> R.drawable.man_extras_0
            2 -> R.drawable.man_extras_1
            3 -> R.drawable.man_extras_2
            4 -> R.drawable.man_extras_3
            5 -> R.drawable.man_extras_4
            else -> R.drawable.ic_void
        }
        1 -> when (id) {
            0 -> R.drawable.ic_void
            1 -> R.drawable.woman_extras_0
            2 -> R.drawable.woman_extras_1
            3 -> R.drawable.woman_extras_2
            4 -> R.drawable.woman_extras_3
            5 -> R.drawable.woman_extras_4
            else -> R.drawable.ic_void
        }
        else -> R.drawable.ic_void
    }
}