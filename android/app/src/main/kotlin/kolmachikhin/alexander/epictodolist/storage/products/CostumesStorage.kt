package kolmachikhin.alexander.epictodolist.storage.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.with
import kolmachikhin.alexander.epictodolist.logic.products.ProductType
import kolmachikhin.alexander.epictodolist.storage.hero.BodyStorage

object CostumesStorage {

    const val STEP = 1000
    const val TYPE = ProductType.COSTUME
    const val DEFAULT_ID = -1 + STEP
    const val LEATHER_SUIT = 0 + STEP
    const val WARRIOR = 1 + STEP
    const val WARRIOR_OF_CHAOS = 2 + STEP
    const val THE_HEALER = 3 + STEP
    const val HERMIT = 4 + STEP
    const val ELF = 5 + STEP

    private fun getIconRes(gender: Int, body: Int) = BodyStorage[gender, body]

    
    val list: ArrayList<ProductModel>
        get() {
            val l = ArrayList<ProductModel>()
            l.add(CostumesStorage[LEATHER_SUIT])
            l.add(CostumesStorage[WARRIOR])
            l.add(CostumesStorage[WARRIOR_OF_CHAOS])
            l.add(CostumesStorage[THE_HEALER])
            l.add(CostumesStorage[HERMIT])
            l.add(CostumesStorage[ELF])
            return l
        }

    
    operator fun get(i: Int): ProductModel {
        val core = with(context!!)
        val gender = core.heroLogic.hero.gender

        return when (i) {
            WARRIOR -> ProductModel(
                i,
                string(R.string.costume_warrior),
                100,
                10,
                TYPE,
                getIconRes(gender, i),
                0,
                3
            )
            LEATHER_SUIT -> ProductModel(
                i,
                string(R.string.leather_suit),
                50,
                10,
                TYPE,
                getIconRes(gender, i),
                0,
                2
            )
            WARRIOR_OF_CHAOS -> ProductModel(
                i,
                string(R.string.warrior_of_chaos),
                100,
                10,
                TYPE,
                getIconRes(gender, i),
                0,
                4
            )
            THE_HEALER -> ProductModel(
                i,
                string(R.string.the_healer),
                200,
                15,
                TYPE,
                getIconRes(gender, i),
                0,
                4
            )
            HERMIT -> ProductModel(
                i,
                string(R.string.hermit),
                200,
                15,
                TYPE,
                getIconRes(gender, i),
                0,
                4
            )
            ELF -> ProductModel(
                i,
                string(R.string.forest_elf),
                250,
                15,
                TYPE,
                getIconRes(gender, i),
                0,
                5
            )
            else -> ProductModel()
        }
    }

    private fun string(id: Int) = context?.getString(id) ?: "Something went wrong :("

}