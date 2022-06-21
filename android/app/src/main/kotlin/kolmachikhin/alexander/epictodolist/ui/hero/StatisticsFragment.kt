package kolmachikhin.alexander.epictodolist.ui.hero

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.storage.achievements.AchievementsStorage.size
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment

class StatisticsFragment(container: ViewGroup) : UIFragment(R.layout.statistics_fragment, container) {

    private lateinit var tvStatus: TextView

    override fun findViews() {
        tvStatus = find(R.id.status)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        val hero = MainActivity.core!!.heroLogic.hero
        val status = MainActivity.core!!.statusLogic.status
        val timeInSec = status.timeInApp / 1000
        var minute = timeInSec / 60
        val hour = minute / 60
        minute %= 60
        var timeUse = MainActivity.ui!!.getString(R.string.time_spent_here) + ": "
        if (hour > 0) timeUse += " " + hour + " " + MainActivity.ui!!.getString(R.string.hours)
        timeUse += " " + minute + " " + MainActivity.ui!!.getString(R.string.minutes)
        tvStatus.text = """
            ${MainActivity.ui!!.getString(R.string.whole_progress)}: ${hero.progress}/${hero.wholeNeedProgress()}
            ${MainActivity.ui!!.getString(R.string.earned_coins)}: ${status.earnedCoins}
            ${MainActivity.ui!!.getString(R.string.used_coins)}: ${status.wastedCoins}
            ${MainActivity.ui!!.getString(R.string.earned_crystals)}: ${status.earnedCrystals}
            ${MainActivity.ui!!.getString(R.string.used_crystals)}: ${status.wastedCrystals}
            ${MainActivity.ui!!.getString(R.string.count_completed_tasks)}: ${MainActivity.core!!.completedTasksLogic.completedTasks.size}
            ${MainActivity.ui!!.getString(R.string.achievements_achieved)}: ${MainActivity.core!!.achievementsLogic.countAchievedAchievements}/${size()}
            ${MainActivity.ui!!.getString(R.string.unlocked_items)}: ${MainActivity.core!!.productsLogic.countUnlockedProducts}/${MainActivity.core!!.productsLogic.countProducts}
            $timeUse
            """.trimIndent()
    }

    companion object {
        
        fun open(viewGroup: ViewGroup) {
            StatisticsFragment(viewGroup).replace()
        }
    }
}