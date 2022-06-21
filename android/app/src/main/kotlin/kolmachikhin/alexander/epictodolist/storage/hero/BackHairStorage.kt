package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R

object BackHairStorage {

    
    fun getList(gender: Int): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (i in 0 until size()) l.add(BackHairStorage[gender, i])
        return l
    }

    fun size() = 6

    
    operator fun get(gender: Int, id: Int) = when (gender) {
        0 -> when (id) {
            HairStorage.HAIR_0 -> R.drawable.man_back_hair_0
            HairStorage.HAIR_1 -> R.drawable.man_back_hair_1
            HairStorage.HAIR_2 -> R.drawable.man_back_hair_2
            HairStorage.HAIR_3 -> R.drawable.man_back_hair_3
            HairStorage.HAIR_4 -> R.drawable.man_back_hair_4
            else -> R.drawable.ic_void
        }
        1 -> when (id) {
            HairStorage.HAIR_0 -> R.drawable.woman_back_hair_0
            HairStorage.HAIR_1 -> R.drawable.woman_back_hair_1
            HairStorage.HAIR_2 -> R.drawable.woman_back_hair_2
            HairStorage.HAIR_3 -> R.drawable.woman_back_hair_3
            HairStorage.HAIR_4 -> R.drawable.woman_back_hair_4
            else -> R.drawable.ic_void
        }
        else -> R.drawable.ic_void
    }
}