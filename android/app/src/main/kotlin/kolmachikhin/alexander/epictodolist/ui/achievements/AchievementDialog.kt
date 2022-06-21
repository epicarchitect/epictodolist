package kolmachikhin.alexander.epictodolist.ui.achievements

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.achievements.AchievementModel
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity

class AchievementDialog : Dialog<AchievementModel>(R.layout.achievement_dialog) {

    private lateinit var tvCondition: TextView
    private lateinit var tvCoins: TextView
    private lateinit var tvCrystals: TextView
    private lateinit var tvProgress: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var progressCount: TextView
    private lateinit var progressBar: ProgressBar

    override fun findViews() {
        super.findViews()
        tvCondition = find(R.id.condition)
        tvCoins = find(R.id.coins)
        tvCrystals = find(R.id.crystals)
        tvProgress = find(R.id.progress)
        ivIcon = find(R.id.icon)
        progressBar = find(R.id.progress_bar)
        progressCount = find(R.id.progress_count)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        super.start()
        tvTitle?.text = model!!.title
        tvCondition.text = model!!.description
        ivIcon.setImageResource(model!!.iconRes)
        tvCoins.text = model!!.reward.coins.toString()
        tvCrystals.text = model!!.reward.crystals.toString()
        tvProgress.text = model!!.reward.progress.toString()

        if (model!!.isAchieved) {
            progressCount.text = MainActivity.ui!!.getString(R.string.achieved)
            progressBar.progress = 100
        } else {
            val currentCount = model!!.currentCountCalculator.getCurrentCount()
            val count = model!!.needCount
            val currentCountPercent = ((currentCount * 100).toFloat() / count).toInt()
            progressBar.progress = currentCountPercent
            progressCount.text = "$currentCount/$count"
        }
    }

    companion object {
        fun open(achievement: AchievementModel?) {
            try {
                val dialog = AchievementDialog()
                dialog.model = achievement
                dialog.openDialog()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.ACHIEVEMENT_DIALOG);
        }
    }
}