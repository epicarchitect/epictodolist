package kolmachikhin.alexander.epictodolist.ui.tasks.repeatable

import android.annotation.SuppressLint
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.datetime.TimePickerDialog
import kolmachikhin.alexander.epictodolist.ui.tasks.TaskMakerDialog
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class RepeatableTaskMakerDialog : TaskMakerDialog<RepeatableTaskModel>(R.layout.repeatable_task_maker_dialog) {

    private lateinit var checkMonday: CheckBox
    private lateinit var checkTuesday: CheckBox
    private lateinit var checkWednesday: CheckBox
    private lateinit var checkThursday: CheckBox
    private lateinit var checkFriday: CheckBox
    private lateinit var checkSaturday: CheckBox
    private lateinit var checkSunday: CheckBox
    private lateinit var checkNotCreateIfExists: CheckBox
    private lateinit var timeCreationLayout: LinearLayout
    private lateinit var tvTime: TextView
    private var hour = 0
    private var minute = 0
    private val checkedDays: ArrayList<Boolean>
        get() = arrayListOf(
            checkMonday.isChecked,
            checkTuesday.isChecked,
            checkWednesday.isChecked,
            checkThursday.isChecked,
            checkFriday.isChecked,
            checkSaturday.isChecked,
            checkSunday.isChecked
        )

    override fun findViews() {
        super.findViews()
        checkMonday = find(R.id.check_monday)
        checkTuesday = find(R.id.check_tuesday)
        checkWednesday = find(R.id.check_wednesday)
        checkThursday = find(R.id.check_thursday)
        checkFriday = find(R.id.check_friday)
        checkSaturday = find(R.id.check_saturday)
        checkSunday = find(R.id.check_sunday)
        checkNotCreateIfExists = find(R.id.check_notCreateIfExists)
        timeCreationLayout = find(R.id.time_creation_layout)
        tvTime = find(R.id.tv_time)
    }

    override fun start() {
        super.start()
        set(timeCreationLayout) {
            TimePickerDialog.open { h: Int, m: Int ->
                setTvTime(h, m)
            }
        }

        checkDays()
        setTvTime(model!!.copyHour(), model!!.copyMinute())
        checkNotCreateIfExists.isChecked = model!!.notCreateIfExists
    }

    override fun done(): RepeatableTaskModel {
        super.done()
        model!!.copyDays = checkedDays
        model!!.copyTime = (hour * 60 * 60 * 1000 + minute * 60 * 1000).toLong()
        model!!.notCreateIfExists = checkNotCreateIfExists.isChecked
        return if (modeEdit) {
            MainActivity.core!!.repeatableTasksLogic.update(model!!)
        } else {
            MainActivity.core!!.repeatableTasksLogic.create(model!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTvTime(h: Int, m: Int) {
        val timeMaster = TimeMaster()
        timeMaster.hour = h
        timeMaster.minute = m
        tvTime.text = MainActivity.ui!!.getString(R.string.autocopy_time) + ": " + timeMaster.timeText
        hour = h
        minute = m
    }

    private fun checkDays() {
        val days = model!!.copyDays
        checkMonday.isChecked = days!![0]
        checkTuesday.isChecked = days[1]
        checkWednesday.isChecked = days[2]
        checkThursday.isChecked = days[3]
        checkFriday.isChecked = days[4]
        checkSaturday.isChecked = days[5]
        checkSunday.isChecked = days[6]
    }

    companion object {
        fun open(onDoneListener: OnDoneListener<RepeatableTaskModel>) {
            val maker = RepeatableTaskMakerDialog()
            maker.setModeCreate()
            maker.onDoneListener = onDoneListener
            maker.model = RepeatableTaskModel()
            maker.openDialog()
            CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.REPEATABLE_TASK_MAKER)
        }

        fun open(task: RepeatableTaskModel?, onDoneListener: OnDoneListener<RepeatableTaskModel>) {
            val maker = RepeatableTaskMakerDialog()
            maker.setModeEdit()
            maker.onDoneListener = onDoneListener
            maker.model = task
            maker.openDialog()
        }
    }
}