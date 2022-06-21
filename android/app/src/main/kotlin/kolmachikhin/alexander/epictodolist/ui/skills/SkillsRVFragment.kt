package kolmachikhin.alexander.epictodolist.ui.skills

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.ui.confirming.ConfirmDialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.ui.recyclerview.SwipeRVFragment
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper.getFrame
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper.getIcon
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class SkillsRVFragment(
    container: ViewGroup
) : SwipeRVFragment<SkillModel, SkillsRVFragment.ViewHolder>(
    R.layout.skill_item,
    RecyclerView.VERTICAL,
    container,
    MainActivity.ui!!.dp(80)
) {

    init {
        onEmptyMessage = MainActivity.ui!!.getString(R.string.no_skills)
    }

    override fun newViewHolder(v: View) = ViewHolder(v)

    inner class ViewHolder(view: View) : RVViewHolder<SkillModel>(view) {
        private lateinit var skill: SkillModel
        private val mainLayout: LinearLayout = find(R.id.main_layout)
        private val swipeLayout: SwipeLayout = find(R.id.swipe_layout)
        private val icon: ImageView = find(R.id.icon)
        private val progressBar: ProgressBar = find(R.id.progress_bar)
        private val progress: TextView = find(R.id.progress_of_level)
        private val title: TextView = find(R.id.title)
        private val level: TextView = find(R.id.level)
        private val buttonDelete: ImageView = find(R.id.button_delete)
        private val buttonEdit: ImageView = find(R.id.button_edit)

        @SuppressLint("SetTextI18n")
        override fun setData(m: SkillModel) {
            this.skill = m
            title.text = skill.title
            icon.setImageResource(getIcon(skill.iconId))
            icon.setBackgroundResource(getFrame(skill.attribute))
            level.text = skill.level().toString()
            progressBar.progress = skill.progressPercent()
            progress.text = "${skill.progressOfLevel()} / ${skill.pointOfLevelUp()}"
            swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.left_side))
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.right_side))
            bindSwipeView(this)
        }

        init {
            set(buttonEdit) {
                SkillMakerDialog.open(skill) { newSkill -> updateItem(newSkill, adapterPosition) }
                closeAllItems()
            }

            set(buttonDelete) {
                ConfirmDialog.open { yes ->
                    if (yes) {
                        MainActivity.core!!.skillsLogic.delete(skill)
                        deleteItem(adapterPosition)
                    }
                }
                closeAllItems()
            }

            set(mainLayout) {
                SkillDialog.open(skill)
                closeAllItems()
            }
        }
    }
}