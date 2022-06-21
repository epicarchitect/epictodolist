package kolmachikhin.alexander.epictodolist.ui.hero

import android.annotation.SuppressLint
import android.widget.FrameLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorFragment
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.NavFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.icon.IconNavigationFragment

class HeroNav : NavFragment(R.layout.hero_nav) {

    private lateinit var fragmentContainer: FrameLayout
    private lateinit var navigationContainer: FrameLayout
    private lateinit var navigation: IconNavigationFragment

    @SuppressLint("ClickableViewAccessibility")
    override fun start() {
        navigation = IconNavigationFragment(IconNavigationFragment.TOP, navigationContainer)
        navigation.replace()
        navigation.addButton(HERO, R.drawable.nav_hero) { setNav(HERO) }
        navigation.addButton(INVENTORY, R.drawable.nav_inventory) { setNav(INVENTORY) }
        navigation.addButton(CREATOR, R.drawable.nav_creator) { setNav(CREATOR) }
        select(HERO)
    }

    override fun findViews() {
        fragmentContainer = find(R.id.container)
        navigationContainer = find(R.id.navigation_container)
    }

    fun setNav(id: Int) {
        var f: UIFragment? = null
        when (id) {
            HERO -> {
                f = HeroFragment(fragmentContainer)
                showTitle(MainActivity.ui!!.getString(R.string.hero))
            }
            INVENTORY -> {
                f = InventoryFragment(fragmentContainer)
                showTitle(MainActivity.ui!!.getString(R.string.inventory))
            }
            CREATOR -> {
                f = CreatorFragment(fragmentContainer)
                showTitle(MainActivity.ui!!.getString(R.string.creator))
            }
        }
        f?.replace()
    }

    fun select(id: Int) {
        navigation.select(id)
    }

    companion object {
        const val HERO = 0
        const val INVENTORY = 1
        const val CREATOR = 2
    }
}