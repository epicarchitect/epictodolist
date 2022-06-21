package kolmachikhin.alexander.epictodolist.ui.challenges

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity

class ChallengeDialog : Dialog<ChallengeModel>(R.layout.challenge_dialog) {

    private lateinit var tvChallengeTitle: TextView
    private lateinit var tvProgressOfLevel: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTasks: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvCountComplates: TextView
    private lateinit var tvCountFails: TextView

    override fun findViews() {
        super.findViews()
        tvChallengeTitle = find(R.id.skill_title)
        tvProgressOfLevel = find(R.id.progress_of_level)
        progressBar = find(R.id.progress_bar)
        ivIcon = find(R.id.icon)
        tvTasks = find(R.id.tv_tasks)
        tvStatus = find(R.id.tv_status)
        tvCountComplates = find(R.id.tv_count_completes)
        tvCountFails = find(R.id.tv_count_fails)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        super.start()
        val skill = MainActivity.core!!.skillsLogic.findById(-1)
        tvTitle?.text = MainActivity.ui!!.getString(R.string.challenge)
        tvChallengeTitle.text = model!!.title
        tvProgressOfLevel.text = model!!.currentDay.toString() + "/" + model!!.needDays
        ivIcon.setImageResource(skill.iconRes())
        ivIcon.setBackgroundResource(skill.frameRes())
        progressBar.progress = model!!.progressPercent()
        val tasksText = StringBuilder()
        for (i in model!!.tasks!!.indices) {
            val task = model!!.tasks!![i]
            tasksText.append(" - ")
                .append(task.currentTask.content)
            if (model!!.isActive) {
                if (task.isCompleted) {
                    tasksText.append(" (")
                        .append(MainActivity.ui!!.getString(R.string.done))
                        .append(")")
                } else {
                    tasksText.append(" (")
                        .append(MainActivity.ui!!.getString(R.string.not_done))
                        .append(")")
                }
            }
            if (i != model!!.tasks!!.size - 1) {
                tasksText.append("\n")
            }
        }
        tvTasks.text = tasksText.toString()
        if (model!!.isActive) {
            tvStatus.text = MainActivity.ui!!.getString(R.string.days_left) + ": " + (model!!.needDays - model!!.currentDay)
        } else {
            tvStatus.text = MainActivity.ui!!.getString(R.string.not_active)
        }
        tvCountComplates.text = MainActivity.ui!!.getString(R.string.completed) + ": " + model!!.countCompletes
        tvCountFails.text = MainActivity.ui!!.getString(R.string.failed) + ": " + model!!.countFails
    }

    companion object {
        fun open(challenge: ChallengeModel) {
            val d = ChallengeDialog()
            d.model = challenge
            d.openDialog()
            //CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.SKILL_DIALOG);
        }
    }
}