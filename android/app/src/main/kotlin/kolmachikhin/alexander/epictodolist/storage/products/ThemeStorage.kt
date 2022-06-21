package kolmachikhin.alexander.epictodolist.storage.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context
import kolmachikhin.alexander.epictodolist.logic.products.ProductType

object ThemeStorage {

    const val STEP = 100000
    const val TYPE = ProductType.THEME
    const val DEFAULT_THEME = -1 + STEP
    const val PEACH_THEME = 0 + STEP
    const val IRON_THEME = 1 + STEP
    const val WOODY_THEME = 2 + STEP
    const val NIGHT_THEME = 3 + STEP
    const val PINK_THEME = 4 + STEP

    val list: ArrayList<ProductModel>
        get() {
            val l = ArrayList<ProductModel>()
            l.add(ThemeStorage[DEFAULT_THEME])
            l.add(ThemeStorage[PEACH_THEME])
            l.add(ThemeStorage[IRON_THEME])
            l.add(ThemeStorage[WOODY_THEME])
            l.add(ThemeStorage[NIGHT_THEME])
            l.add(ThemeStorage[PINK_THEME])
            return l
        }

    operator fun get(i: Int) = when (i) {
        PEACH_THEME -> ProductModel(i, string(R.string.peach_theme), 100, 5, TYPE, R.drawable.ic_peach_theme, 0, 5)
        IRON_THEME -> ProductModel(i, string(R.string.iron_theme), 200, 10, TYPE, R.drawable.icon_5, 0, 5)
        WOODY_THEME -> ProductModel(i, string(R.string.woody_theme), 200, 10, TYPE, R.drawable.ic_woody_theme, 0, 5)
        NIGHT_THEME -> ProductModel(i, string(R.string.night_theme), 200, 10, TYPE, R.drawable.ic_night_theme, 0, 5)
        PINK_THEME -> ProductModel(i, string(R.string.pink_theme), 200, 10, TYPE, R.drawable.ic_woody_theme, 0, 5)
        else -> ProductModel(DEFAULT_THEME, string(R.string.system_theme), 0, 0, TYPE, R.drawable.icon_18, 1, 1)
    }

    fun string(id: Int) = context?.getString(id) ?: "Something went wrong :("

}