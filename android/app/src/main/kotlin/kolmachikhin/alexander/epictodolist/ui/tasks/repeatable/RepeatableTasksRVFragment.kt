package kolmachikhin.alexander.epictodolist.ui.tasks.repeatable

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel
import kolmachikhin.alexander.epictodolist.ui.confirming.ConfirmDialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.ui.recyclerview.SwipeRVFragment
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class RepeatableTasksRVFragment(
    container: ViewGroup
) : SwipeRVFragment<RepeatableTaskModel, RepeatableTasksRVFragment.ViewHolder>(
    R.layout.repeatable_task_item,
    RecyclerView.VERTICAL,
    container,
    MainActivity.ui!!.dp(80)
) {

    init {
        onEmptyMessage = MainActivity.ui!!.getString(R.string.there_are_no_repeatable_tasks)
    }

    override fun newViewHolder(v: View) = ViewHolder(v)

    inner class ViewHolder(view: View) : RVViewHolder<RepeatableTaskModel>(view) {
        private lateinit var task: RepeatableTaskModel
        private var swipeLayout: SwipeLayout = find(R.id.swipe_layout)
        private var rightSide: LinearLayout = find(R.id.right_side)
        private var leftSide: LinearLayout = find(R.id.left_side)
        private var mainLayout: LinearLayout = find(R.id.main_layout)
        private var content: TextView = find(R.id.content)
        private var nextDateCreation: TextView = find(R.id.next_date_creation)
        private var icon: ImageView = find(R.id.icon)
        private var buttonEdit: ImageView = find(R.id.button_edit)
        private var buttonCopy: ImageView = find(R.id.button_copy)
        private var buttonDelete: ImageView = find(R.id.button_delete)

        init {
            set(mainLayout) {
                RepeatableTaskDialog.open(task)
                closeAllItems()
            }

            set(buttonEdit) {
                RepeatableTaskMakerDialog.open(task) {
                    updateItem(it, adapterPosition)
                }
                closeAllItems()
            }

            set(buttonDelete) {
                ConfirmDialog.open { yes ->
                    if (yes) {
                        MainActivity.core!!.repeatableTasksLogic.delete(task)
                        deleteItem(adapterPosition)
                    }
                }
                closeAllItems()
            }

            set(buttonCopy) {
                MainActivity.core!!.currentTasksLogic.create(task)
                closeAllItems()
            }
        }

        @SuppressLint("SetTextI18n")
        override fun setData(m: RepeatableTaskModel) {
            this.task = m
            val skill = MainActivity.core!!.skillsLogic.findById(task.skillId)
            val timeMaster = TimeMaster()
            timeMaster.timeInMillis = MainActivity.core!!.repeatableTasksLogic.getNextDateCreate(task)
            nextDateCreation.text = MainActivity.ui!!.getString(R.string.next_in) + ": " + timeMaster.timeDateText
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