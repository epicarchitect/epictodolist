package kolmachikhin.alexander.epictodolist.ui.tasks.current

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.ui.confirming.ConfirmDialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.ui.recyclerview.SwipeRVFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class CurrentTasksRVFragment(container: ViewGroup) : SwipeRVFragment<CurrentTaskModel, CurrentTasksRVFragment.ViewHolder>(
    R.layout.current_task_item,
    RecyclerView.VERTICAL,
    container,
    MainActivity.ui!!.dp(80)
) {

    init {
        onEmptyMessage = MainActivity.ui!!.getString(R.string.there_are_no_current_tasks)
    }

    override fun newViewHolder(v: View) = ViewHolder(v)


    inner class ViewHolder(view: View) : RVViewHolder<CurrentTaskModel>(view) {
        private lateinit var task: CurrentTaskModel
        private var swipeLayout: SwipeLayout = find(R.id.swipe_layout)
        private var rightSide: LinearLayout = find(R.id.right_side)
        private var leftSide: LinearLayout = find(R.id.left_side)
        private var mainLayout: LinearLayout = find(R.id.main_layout)
        private var content: TextView = find(R.id.content)
        private var icon: ImageView = find(R.id.icon)
        private var buttonEdit: ImageView = find(R.id.button_edit)
        private var buttonDone: ImageView = find(R.id.button_done)
        private var buttonDelete: ImageView = find(R.id.button_delete)

        init {
            set(mainLayout) {
                CurrentTaskDialog.open(task)
                closeAllItems()
            }
            set(buttonDone) {
                MainActivity.core!!.currentTasksLogic.complete(task)
                deleteItem(adapterPosition)
                closeAllItems()
            }
            set(buttonEdit) {
                CurrentTaskMakerDialog.open(task) { newTask ->
                    MainActivity.core!!.currentTasksLogic.update(newTask)
                    updateItem(newTask, adapterPosition)
                }
                closeAllItems()
            }
            set(buttonDelete) {
                ConfirmDialog.open { yes ->
                    if (yes) {
                        MainActivity.core!!.currentTasksLogic.delete(task)
                        deleteItem(adapterPosition)
                    }
                }
                closeAllItems()
            }
        }

        override fun setData(m: CurrentTaskModel) {
            this.task = m
            val skill = MainActivity.core!!.skillsLogic.findById(task.skillId)
            content.text = task.content
            icon.setImageResource(skill.iconRes())
            icon.setBackgroundResource(skill.frameRes())
            swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, leftSide)
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, rightSide)
            bindSwipeView(this)
        }
    }
}