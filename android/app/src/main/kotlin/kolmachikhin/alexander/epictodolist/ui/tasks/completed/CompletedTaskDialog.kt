package kolmachikhin.alexander.epictodolist.ui.tasks.completed

import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTaskModel
import kolmachikhin.alexander.epictodolist.ui.tasks.TaskDialog
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster

class CompletedTaskDialog : TaskDialog<CompletedTaskModel>(R.layout.completed_task_dialog) {

    private lateinit var tvCompletedDate: TextView

    override fun findViews() {
        super.findViews()
        tvCompletedDate = find(R.id.completed_date)
    }

    override fun start() {
        super.start()
        setCompletedDate()
    }

    private fun setCompletedDate() {
        val timeMaster = TimeMaster()
        timeMaster.timeInMillis = model!!.completionDate
        tvCompletedDate.text = timeMaster.timeDateText
    }

    companion object {
        fun open(task: CompletedTaskModel) {
            val dialog = CompletedTaskDialog()
            dialog.model = task
            dialog.openDialog()
            //CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.COMPLETED_TASK_DIALOG);
        }
    }
}