package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R

object BeardStorage {

    const val WITHOUT_BEARD = 0

    
    fun getList(gender: Int): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (i in 0 until size()) l.add(BeardStorage[gender, i])
        return l
    }

    
    fun size() = 5

    
    operator fun get(gender: Int, id: Int) = when (gender) {
        1 -> R.drawable.ic_void
        else -> when (id) {
            0 -> R.drawable.ic_void
            1 -> R.drawable.man_beard_0
            2 -> R.drawable.man_beard_1
            3 -> R.drawable.man_beard_2
            4 -> R.drawable.man_beard_3
            else -> R.drawable.ic_void
        }
    }
}