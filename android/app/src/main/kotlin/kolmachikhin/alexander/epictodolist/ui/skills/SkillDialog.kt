package kolmachikhin.alexander.epictodolist.ui.skills

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.logic.hero.Attribute.toString
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity

class SkillDialog : Dialog<SkillModel?>(R.layout.skill_dialog) {

    private lateinit var tvSkillTitle: TextView
    private lateinit var tvLevel: TextView
    private lateinit var tvAttribute: TextView
    private lateinit var tvProgressOfLevel: TextView
    private lateinit var tvWholeProgress: TextView
    private lateinit var tvNumberCompletedTasks: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var progressBar: ProgressBar

    override fun findViews() {
        super.findViews()
        tvSkillTitle = find(R.id.skill_title)
        tvLevel = find(R.id.level)
        tvAttribute = find(R.id.tv_attribute)
        tvProgressOfLevel = find(R.id.progress_of_level)
        tvWholeProgress = find(R.id.whole_progress)
        tvNumberCompletedTasks = find(R.id.number_completed_tasks)
        progressBar = find(R.id.progress_bar)
        ivIcon = find(R.id.icon)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        super.start()
        tvTitle?.text = MainActivity.ui!!.getString(R.string.skill)
        tvAttribute.text = MainActivity.ui!!.getString(R.string.attribute) + ": " + toString(model!!.attribute)
        tvSkillTitle.text = model!!.title
        tvLevel.text = model!!.level().toString()
        tvProgressOfLevel.text = model!!.progressOfLevel().toString() + "/" + model!!.pointOfLevelUp()
        tvWholeProgress.text = MainActivity.ui!!.getString(R.string.whole_progress) + ": " + model!!.progress + "/" + model!!.wholeNeedProgress()
        tvNumberCompletedTasks.text = MainActivity.ui!!.getString(R.string.count_completed_tasks) + ": " + MainActivity.core!!.completedTasksLogic.getCountCompletedTasksBySkillId(model!!.id)
        ivIcon.setImageResource(model!!.iconRes())
        ivIcon.setBackgroundResource(model!!.frameRes())
        progressBar.progress = model!!.progressPercent()
    }

    companion object {
        fun open(skill: SkillModel?) {
            val skillDialog = SkillDialog()
            skillDialog.model = skill
            skillDialog.openDialog()
            //CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.SKILL_DIALOG);
        }
    }
}