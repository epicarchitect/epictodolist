package kolmachikhin.alexander.epictodolist.storage.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context
import kolmachikhin.alexander.epictodolist.logic.products.ProductType

object LocationsStorage {

    const val STEP = 10
    const val TYPE = ProductType.LOCATION
    const val DEFAULT_ID = 0 + STEP
    const val NIGHT = 1 + STEP
    const val CASTLE = 2 + STEP
    const val SAKURA = 3 + STEP
    const val HOME_OF_CREATOR = 4 + STEP

    
    val list: ArrayList<ProductModel>
        get() {
            val l = ArrayList<ProductModel>()
            l.add(LocationsStorage[DEFAULT_ID])
            l.add(LocationsStorage[NIGHT])
            l.add(LocationsStorage[CASTLE])
            l.add(LocationsStorage[SAKURA])
            l.add(LocationsStorage[HOME_OF_CREATOR])
            return l
        }

    
    operator fun get(i: Int) = when (i) {
        NIGHT -> ProductModel(
            i,
            string(R.string.night),
            100,
            10,
            TYPE,
            R.drawable.night,
            0,
            3
        )
        CASTLE -> ProductModel(
            i,
            string(R.string.castle),
            100,
            10,
            TYPE,
            R.drawable.castle,
            0,
            3
        )
        SAKURA -> ProductModel(
            i,
            string(R.string.sakura),
            100,
            10,
            TYPE,
            R.drawable.sakura,
            0,
            3
        )
        HOME_OF_CREATOR -> ProductModel(
            i,
            string(R.string.home_of_creator),
            300,
            20,
            TYPE,
            R.drawable.home_of_creator,
            0,
            5
        )
        else -> ProductModel(
            DEFAULT_ID,
            string(R.string.forest),
            0,
            0,
            TYPE,
            R.drawable.forest,
            1,
            1
        )
    }

    fun string(id: Int) = context?.getString(id) ?: "Something went wrong :("

}