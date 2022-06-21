package kolmachikhin.alexander.epictodolist.ui.challenges

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.confirming.ConfirmDialog
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.ui.recyclerview.SwipeRVFragment
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper.getFrame
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class ChallengesRVFragment(
    container: ViewGroup
) : SwipeRVFragment<ChallengeModel, ChallengesRVFragment.ViewHolder>(
    R.layout.challenge_item,
    RecyclerView.VERTICAL,
    container,
    MainActivity.ui!!.dp(80)
) {

    init {
        onEmptyMessage = MainActivity.ui!!.getString(R.string.there_are_no_challenges)
    }

    override fun newViewHolder(v: View) = ViewHolder(v)

    inner class ViewHolder(view: View) : RVViewHolder<ChallengeModel>(view) {
        private lateinit var challenge: ChallengeModel
        private var mainLayout: LinearLayout = find(R.id.main_layout)
        private var swipeLayout: SwipeLayout = find(R.id.swipe_layout)
        private var icon: ImageView = find(R.id.icon)
        private var title: TextView = find(R.id.title)
        private var tvStatus: TextView = find(R.id.tv_status)
        private var buttonDelete: ImageView = find(R.id.button_delete)
        private var buttonEdit: ImageView = find(R.id.button_edit)
        private var buttonActivate: Button = find(R.id.button_activate)

        init {
            set(buttonEdit) {
                ChallengeMakerDialog.open(challenge) { newChallenge ->
                    updateItem(newChallenge, adapterPosition)
                }
                closeAllItems()
            }
            set(buttonDelete) {
                ConfirmDialog.open { yes ->
                    if (yes) {
                        MainActivity.core!!.challengesLogic.delete(challenge)
                        deleteItem(adapterPosition)
                    }
                    closeAllItems()
                }
            }
            set(mainLayout) {
                ChallengeDialog.open(challenge)
                closeAllItems()
            }
            set(buttonActivate) {
                ConfirmDialog.open { yes ->
                    if (yes) {
                        if (challenge.isActive) {
                            MainActivity.core!!.challengesLogic.fail(challenge)
                            updateItem(MainActivity.core!!.challengesLogic.findById(challenge.id), adapterPosition)
                        } else {
                            MainActivity.core!!.challengesLogic.restart(challenge)
                            updateItem(MainActivity.core!!.challengesLogic.findById(challenge.id), adapterPosition)
                        }
                    }
                }
                closeAllItems()
            }
        }

        @SuppressLint("SetTextI18n")
        override fun setData(m: ChallengeModel) {
            this.challenge = m
            title.text = challenge.title
            icon.setImageResource(challenge.iconRes())
            icon.setBackgroundResource(getFrame(-1))
            if (challenge.isActive) {
                tvStatus.text = MainActivity.ui!!.getString(R.string.days_left) + ": " + (challenge.needDays - challenge.currentDay)
                buttonActivate.text = MainActivity.ui!!.getString(R.string.fail)
            } else {
                tvStatus.text = MainActivity.ui!!.getString(R.string.not_active)
                buttonActivate.text = MainActivity.ui!!.getString(R.string.start)
            }
            swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.left_side))
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.right_side))
            bindSwipeView(this)
        }
    }
}