package kolmachikhin.alexander.epictodolist.ui.challenges

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeTaskModel
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVFragment
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.ui.tasks.current.CurrentTaskMakerDialog
import kolmachikhin.alexander.epictodolist.util.ui.Clicks

class ChallengesTasksRVFragment(
    container: ViewGroup
) : RVFragment<ChallengeTaskModel, ChallengesTasksRVFragment.ViewHolder>(
    R.layout.challenge_task_item,
    RecyclerView.VERTICAL,
    container
) {

    init {
        onEmptyMessage = MainActivity.ui!!.getString(R.string.there_are_no_tasks)
    }

    override fun newViewHolder(v: View) = ViewHolder(v)

    inner class ViewHolder(view: View) : RVViewHolder<ChallengeTaskModel>(view) {
        lateinit var task: ChallengeTaskModel
        var content: TextView = find(R.id.content)
        var icon: ImageView = find(R.id.icon)
        var buttonDelete: ImageView = find(R.id.button_delete)

        init {
            Clicks.set(view) {
                val currentTaskMakerDialog = CurrentTaskMakerDialog.open(task.currentTask) { newTask ->
                    task.currentTask = newTask
                    updateItem(task, adapterPosition)
                }
                currentTaskMakerDialog.hideNotifications()
            }
            Clicks.set(buttonDelete) { deleteItem(adapterPosition) }
        }

        override fun setData(m: ChallengeTaskModel) {
            this.task = m
            val currentTask = task.currentTask
            val skill = MainActivity.core!!.skillsLogic.findById(currentTask.skillId)
            content.text = currentTask.content
            icon.setImageResource(skill.iconRes())
        }
    }
}