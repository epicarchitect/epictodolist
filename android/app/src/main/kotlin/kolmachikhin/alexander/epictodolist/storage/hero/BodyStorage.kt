package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.storage.products.CostumesStorage
import kolmachikhin.alexander.epictodolist.ui.MainActivity

object BodyStorage {

    
    operator fun get(gender: Int, id: Int): Int {
        when (gender) {
            0 -> return when (id) {
                CostumesStorage.DEFAULT_ID -> R.drawable.man_body_0
                CostumesStorage.LEATHER_SUIT -> R.drawable.man_body_1
                CostumesStorage.WARRIOR -> R.drawable.man_body_2
                CostumesStorage.WARRIOR_OF_CHAOS -> R.drawable.man_body_3
                CostumesStorage.THE_HEALER -> R.drawable.man_body_4
                CostumesStorage.HERMIT -> R.drawable.man_body_5
                CostumesStorage.ELF -> R.drawable.man_body_6
                else -> R.drawable.man_body_0
            }
            1 -> return when (id) {
                CostumesStorage.DEFAULT_ID -> R.drawable.woman_body_0
                CostumesStorage.LEATHER_SUIT -> R.drawable.woman_body_1
                CostumesStorage.WARRIOR -> R.drawable.woman_body_2
                CostumesStorage.WARRIOR_OF_CHAOS -> R.drawable.woman_body_3
                CostumesStorage.THE_HEALER -> R.drawable.woman_body_4
                CostumesStorage.HERMIT -> R.drawable.woman_body_5
                CostumesStorage.ELF -> R.drawable.woman_body_6
                else -> R.drawable.woman_body_0
            }
        }

        if (MainActivity.core != null) {
            when (MainActivity.core!!.heroLogic.hero.gender) {
                0 -> return R.drawable.man_body_0
                1 -> return R.drawable.woman_body_0
            }
        }

        return R.drawable.ic_void
    }
}