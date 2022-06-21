package kolmachikhin.alexander.epictodolist.ui.tasks

import android.annotation.SuppressLint
import android.widget.FrameLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.icon.IconNavigationFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.NavFragment
import kolmachikhin.alexander.epictodolist.ui.tasks.completed.CompletedTasksFragment
import kolmachikhin.alexander.epictodolist.ui.tasks.current.CurrentTasksFragment
import kolmachikhin.alexander.epictodolist.ui.tasks.repeatable.RepeatableTasksFragment

class TasksNav : NavFragment(R.layout.tasks_nav) {

    private lateinit var navigationContainer: FrameLayout
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var navigation: IconNavigationFragment

    override fun findViews() {
        navigationContainer = find(R.id.navigation_container)
        fragmentContainer = find(R.id.container)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun start() {
        navigation = IconNavigationFragment(IconNavigationFragment.TOP, navigationContainer)
        navigation.replace()
        navigation.addButton(CURRENT, R.drawable.nav_tasks) { setNav(CURRENT) }
        navigation.addButton(REPEATABLE, R.drawable.nav_repeatable_tasks) { setNav(REPEATABLE) }
        navigation.addButton(COMPLETED, R.drawable.nav_completed_tasks) { setNav(COMPLETED) }
        select(CURRENT)
    }

    fun setNav(id: Int) {
        var f: UIFragment? = null
        when (id) {
            CURRENT -> {
                f = CurrentTasksFragment(fragmentContainer)
                showTitle(MainActivity.ui!!.getString(R.string.current_tasks))
            }
            REPEATABLE -> {
                f = RepeatableTasksFragment(fragmentContainer)
                showTitle(MainActivity.ui!!.getString(R.string.copy_tasks))
            }
            COMPLETED -> {
                f = CompletedTasksFragment(fragmentContainer)
                showTitle(MainActivity.ui!!.getString(R.string.completed_tasks))
            }
        }
        f?.replace()
    }

    fun select(id: Int) {
        navigation.select(id)
    }

    companion object {
        const val CURRENT = 0
        const val REPEATABLE = 1
        const val COMPLETED = 2
    }
}