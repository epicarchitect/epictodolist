package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R

object ColorBodyStorage {

    
    val list: ArrayList<Int>
        get() {
            val l = ArrayList<Int>()
            for (i in 0 until size()) l.add(ColorBodyStorage[i])
            return l
        }

    
    fun size() = 6

    
    operator fun get(id: Int) = when (id) {
        0 -> R.color.colorBody0
        1 -> R.color.colorBody1
        2 -> R.color.colorBody2
        3 -> R.color.colorBody3
        4 -> R.color.colorBody4
        5 -> R.color.colorBody5
        else -> R.color.colorBody0
    }
}