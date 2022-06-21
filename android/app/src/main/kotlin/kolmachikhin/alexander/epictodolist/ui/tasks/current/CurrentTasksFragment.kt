package kolmachikhin.alexander.epictodolist.ui.tasks.current

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.appwidget.tasks.TaskListWidget.Companion.update
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.drag.DragHelper
import kolmachikhin.alexander.epictodolist.ui.drag.ModelOrderManager
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class CurrentTasksFragment(container: ViewGroup) : UIFragment(R.layout.current_tasks_fragment, container) {

    private val orderManager = ModelOrderManager<CurrentTaskModel>(context, "CurrentTasksFragment")
    private lateinit var buttonCreate: ImageView
    private lateinit var rvContainer: FrameLayout
    private lateinit var currentTasksRVFragment: CurrentTasksRVFragment

    override fun findViews() {
        buttonCreate = find(R.id.button_create)
        rvContainer = find(R.id.rv_container)
    }

    override fun start() {
        set(buttonCreate) {
            CurrentTaskMakerDialog.open { task: CurrentTaskModel ->
                MainActivity.core!!.currentTasksLogic.create(task)
                currentTasksRVFragment.addItem(task)
            }
        }
        setTasks()
        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.CURRENT_TASKS_NAV)
    }

    private fun setTasks() {
        currentTasksRVFragment = CurrentTasksRVFragment(rvContainer)
        currentTasksRVFragment.list = orderManager.sorted(MainActivity.core!!.currentTasksLogic.currentTasks)
        currentTasksRVFragment.replace()
        DragHelper.addDrag(currentTasksRVFragment, orderManager) { update(context) }
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