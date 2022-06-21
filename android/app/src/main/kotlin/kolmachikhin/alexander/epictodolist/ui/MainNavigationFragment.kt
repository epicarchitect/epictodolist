package kolmachikhin.alexander.epictodolist.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.logic.hero.HeroLogic
import kolmachikhin.alexander.epictodolist.ui.hero.HeroNav
import kolmachikhin.alexander.epictodolist.ui.navigation.icon.IconNavigationFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.NavFragment
import kolmachikhin.alexander.epictodolist.ui.skills.SkillsNav
import kolmachikhin.alexander.epictodolist.ui.tasks.TasksNav

class MainNavigationFragment(container: ViewGroup) : UIFragment(R.layout.main_navigation_fragment, container) {

    private lateinit var fragmentContainer: FrameLayout
    private lateinit var navigation: IconNavigationFragment
    var selectedNav: NavFragment? = null
        private set

    override fun findViews() {
        fragmentContainer = find(R.id.container)
    }

    override fun start() {
        navigation = IconNavigationFragment(IconNavigationFragment.BOTTOM, fragmentContainer)
        navigation.replace()
        navigation.addButton(TASKS, R.drawable.nav_tasks) { setNav(TASKS) }
        navigation.addButton(SKILLS, R.drawable.nav_skills) { setNav(SKILLS) }
        navigation.addButton(HERO, R.drawable.nav_hero) { setNav(HERO) }
    }

    private fun setNav(id: Int) {
        when (id) {
            TASKS -> {
                selectedNav = TasksNav()
            }
            SKILLS -> {
                selectedNav = SkillsNav()
            }
            HERO -> {
                selectedNav = HeroNav()
            }
        }

        if (selectedNav != null) {
            selectedNav!!.replace()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: MainNavigationFragment? = null
            private set

        fun init() {
            instance = MainNavigationFragment(MainActivity.navigationContainer!!)
            instance!!.replace()
        }

        const val TASKS = 0
        const val SKILLS = 1
        const val HERO = 2

        fun setAfterCoreInit() {
            MainActivity.core!!.heroLogic.observer.addListener(HeroLogic.CREATE) { select(TASKS) }
            select(TASKS)
        }

        
        fun select(id: Int) {
            instance!!.navigation.select(id)
        }
    }
}