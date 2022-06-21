package kolmachikhin.alexander.epictodolist.ui.tasks.repeatable

import android.annotation.SuppressLint
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.tasks.TaskDialog
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster
import java.util.*

class RepeatableTaskDialog : TaskDialog<RepeatableTaskModel>(R.layout.repeatable_task_dialog) {

    private lateinit var tvRepeatDays: TextView
    private lateinit var tvTimeCreation: TextView
    private lateinit var tvNextDateCreate: TextView

    override fun findViews() {
        super.findViews()
        tvRepeatDays = find(R.id.copy_days)
        tvTimeCreation = find(R.id.copy_time)
        tvNextDateCreate = find(R.id.next_date_create)
    }

    override fun start() {
        super.start()
        setRepeatDays()
        setTvTimeCreation()
        setTvNextDateCreate()
    }

    @SuppressLint("SetTextI18n")
    private fun setRepeatDays() {
        var days = ""
        val repeatDays = model!!.copyDays
        var needN = false
        if (repeatDays!![0]) {
            days += MainActivity.ui!!.getString(R.string.monday)
            needN = true
        }
        if (repeatDays[1]) {
            if (needN) days += ", "
            days += MainActivity.ui!!.getString(R.string.tuesday)
            needN = true
        }
        if (repeatDays[2]) {
            if (needN) days += ", "
            days += MainActivity.ui!!.getString(R.string.wednesday)
            needN = true
        }
        if (repeatDays[3]) {
            if (needN) days += ", "
            days += MainActivity.ui!!.getString(R.string.thursday)
            needN = true
        }
        if (repeatDays[4]) {
            if (needN) days += ", "
            days += MainActivity.ui!!.getString(R.string.friday)
            needN = true
        }
        if (repeatDays[5]) {
            if (needN) days += ", "
            days += MainActivity.ui!!.getString(R.string.saturday)
            needN = true
        }
        if (repeatDays[6]) {
            if (needN) days += ", "
            days += MainActivity.ui!!.getString(R.string.sunday)
        }
        tvRepeatDays.text = MainActivity.ui!!.getString(R.string.autocopy_days) + ": " + days.lowercase(Locale.getDefault())
    }

    @SuppressLint("SetTextI18n")
    fun setTvTimeCreation() {
        val master = TimeMaster()
        master.hour = model!!.copyHour()
        master.minute = model!!.copyMinute()
        tvTimeCreation.text = MainActivity.ui!!.getString(R.string.autocopy_time) + ": " + master.timeText
    }

    @SuppressLint("SetTextI18n")
    fun setTvNextDateCreate() {
        val timeMaster = TimeMaster()
        timeMaster.timeInMillis = MainActivity.core!!.repeatableTasksLogic.getNextDateCreate(model!!)
        tvNextDateCreate.text = MainActivity.ui!!.getString(R.string.next_in) + ": " + timeMaster.timeDateText
    }

    companion object {
        fun open(task: RepeatableTaskModel?) {
            val dialog = RepeatableTaskDialog()
            dialog.model = task
            dialog.openDialog()
            //CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.REPEATABLE_TASK_DIALOG);
        }
    }
}