package kolmachikhin.alexander.epictodolist.ui.achievements

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.achievements.AchievementModel
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVFragment
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class AchievementsRVFragment(
    container: ViewGroup
) : RVFragment<AchievementModel, AchievementsRVFragment.ViewHolder>(
    R.layout.achievement_item,
    RecyclerView.VERTICAL,
    container,
    MainActivity.ui!!.dp(80)
) {

    override fun newViewHolder(v: View) = ViewHolder(v)

    inner class ViewHolder(v: View) : RVViewHolder<AchievementModel>(v) {
        private lateinit var achievement: AchievementModel
        private val title: TextView = find(R.id.title)
        private val description: TextView = find(R.id.description)
        private val icon: ImageView = find(R.id.icon)
        private val mainLayout: LinearLayout = find(R.id.main_layout)

        init {
            set(mainLayout) { AchievementDialog.open(achievement) }
        }

        override fun setData(m: AchievementModel) {
            this.achievement = m
            icon.setImageResource(achievement.iconRes)
            title.text = achievement.title
            description.text = achievement.description
            if (achievement.isAchieved) {
                crossOut(title)
                crossOut(description)
            } else {
                clearCrossOut(title)
                clearCrossOut(description)
            }
        }

        private fun crossOut(tv: TextView) {
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        private fun clearCrossOut(tv: TextView) {
            tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}