package kolmachikhin.alexander.epictodolist.ui.tasks.completed

import android.view.ViewGroup
import android.widget.FrameLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment

class CompletedTasksFragment(container: ViewGroup) : UIFragment(R.layout.completed_tasks_fragment, container) {

    private lateinit var rvContainer: FrameLayout
    private lateinit var completedTasksRVFragment: CompletedTasksRVFragment

    override fun findViews() {
        rvContainer = find(R.id.rv_container)
    }

    override fun start() {
        setTasks()
        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.COMPLETED_TASKS_NAV)
    }

    private fun setTasks() {
        completedTasksRVFragment = CompletedTasksRVFragment(rvContainer)
        val list = MainActivity.core!!.completedTasksLogic.notDeletedCompletedTasks
        list.sortWith { o1, o2 -> o2.completionDate.compareTo(o1.completionDate) }
        completedTasksRVFragment.list = list
        completedTasksRVFragment.replace()
    }

    override fun remove() {
        Animations.hideView(container) { super.remove() }
    }

    override fun replace() {
        Animations.hideView(container) { super.replace() }
    }

    override fun add() {
        super.add()
        Animations.showView(container, null)
    }
}