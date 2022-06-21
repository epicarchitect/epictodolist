package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R

object HairStorage {

    const val WITHOUT_HAIR = 0
    const val HAIR_0 = 1
    const val HAIR_1 = 2
    const val HAIR_2 = 3
    const val HAIR_3 = 4
    const val HAIR_4 = 5

    
    fun getList(gender: Int): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (i in 0 until size()) l.add(HairStorage[gender, i])
        return l
    }

    
    fun size() = 6

    
    operator fun get(gender: Int, id: Int) = when (gender) {
        0 -> when (id) {
            WITHOUT_HAIR -> R.drawable.ic_void
            HAIR_0 -> R.drawable.man_hair_0
            HAIR_1 -> R.drawable.man_hair_1
            HAIR_2 -> R.drawable.man_hair_2
            HAIR_3 -> R.drawable.man_hair_3
            HAIR_4 -> R.drawable.man_hair_4
            else -> R.drawable.ic_void
        }
        1 -> when (id) {
            WITHOUT_HAIR -> R.drawable.ic_void
            HAIR_0 -> R.drawable.woman_hair_0
            HAIR_1 -> R.drawable.woman_hair_1
            HAIR_2 -> R.drawable.woman_hair_2
            HAIR_3 -> R.drawable.woman_hair_3
            HAIR_4 -> R.drawable.woman_hair_4
            else -> R.drawable.ic_void
        }
        else -> R.drawable.ic_void
    }
}