package kolmachikhin.alexander.epictodolist.storage.hero

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.storage.products.WeaponStorage

object BackWeaponStorage {

    
    operator fun get(id: Int) = when (id) {
        WeaponStorage.DEFAULT_ID -> R.drawable.ic_void
        WeaponStorage.HERO_SWORD -> R.drawable.back_weapon_0
        WeaponStorage.MAGICIAN_STAFF -> R.drawable.back_weapon_1
        WeaponStorage.BLADES_OF_FURY -> R.drawable.back_weapon_2
        WeaponStorage.CHAOS_AXES -> R.drawable.back_weapon_3
        WeaponStorage.ELVEN_BOW -> R.drawable.back_weapon_4
        else -> R.drawable.ic_void
    }
}