package kolmachikhin.alexander.epictodolist.storage.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context
import kolmachikhin.alexander.epictodolist.logic.products.ProductType

object WeaponStorage {

    const val STEP = 10000
    const val TYPE = ProductType.WEAPON
    const val DEFAULT_ID = -1 + STEP
    const val HERO_SWORD = 0 + STEP
    const val MAGICIAN_STAFF = 1 + STEP
    const val BLADES_OF_FURY = 2 + STEP
    const val CHAOS_AXES = 3 + STEP
    const val ELVEN_BOW = 4 + STEP

    val list: ArrayList<ProductModel>
        get() {
            val l = ArrayList<ProductModel>()
            l.add(WeaponStorage[HERO_SWORD])
            l.add(WeaponStorage[MAGICIAN_STAFF])
            l.add(WeaponStorage[BLADES_OF_FURY])
            l.add(WeaponStorage[CHAOS_AXES])
            l.add(WeaponStorage[ELVEN_BOW])
            return l
        }

    operator fun get(i: Int) = when (i) {
        HERO_SWORD -> ProductModel(
            i,
            string(R.string.hero_sword),
            100,
            5,
            TYPE,
            R.drawable.back_weapon_0,
            0,
            2
        )
        MAGICIAN_STAFF -> ProductModel(
            i,
            string(R.string.magicians_staff),
            100,
            5,
            TYPE,
            R.drawable.back_weapon_1,
            0,
            3
        )
        BLADES_OF_FURY -> ProductModel(
            i,
            string(R.string.blades_of_fury),
            200,
            10,
            TYPE,
            R.drawable.back_weapon_2,
            0,
            3
        )
        CHAOS_AXES -> ProductModel(
            i,
            string(R.string.chaos_axes),
            200,
            10,
            TYPE,
            R.drawable.back_weapon_3,
            0,
            3
        )
        ELVEN_BOW -> ProductModel(
            i,
            string(R.string.elven_bow),
            250,
            15,
            TYPE,
            R.drawable.back_weapon_4,
            0,
            3
        )
        else -> ProductModel()
    }

    fun string(id: Int) = context?.getString(id) ?: "Something went wrong :("

}