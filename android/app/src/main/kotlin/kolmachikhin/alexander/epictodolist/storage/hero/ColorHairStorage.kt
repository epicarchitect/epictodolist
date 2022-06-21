package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R

object ColorHairStorage {

    
    val list: ArrayList<Int>
        get() {
            val l = ArrayList<Int>()
            for (i in 0 until size()) l.add(ColorHairStorage[i])
            return l
        }

    
    fun size() = 14

    
    operator fun get(id: Int) = when (id) {
        0 -> R.color.colorHair0
        1 -> R.color.colorHair1
        2 -> R.color.colorHair2
        3 -> R.color.colorHair3
        4 -> R.color.colorHair4
        5 -> R.color.colorHair5
        6 -> R.color.colorHair6
        7 -> R.color.colorHair7
        8 -> R.color.colorHair8
        9 -> R.color.colorHair9
        10 -> R.color.colorHair10
        11 -> R.color.colorHair11
        12 -> R.color.colorHair12
        13 -> R.color.colorHair13
        else -> R.color.colorHair0
    }
}