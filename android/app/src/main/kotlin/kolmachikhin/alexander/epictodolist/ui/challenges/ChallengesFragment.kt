package kolmachikhin.alexander.epictodolist.ui.challenges

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.storage.products.FeaturesStorage
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog.Companion.openIfNotLearned
import kolmachikhin.alexander.epictodolist.ui.drag.DragHelper.addDrag
import kolmachikhin.alexander.epictodolist.ui.drag.ModelOrderManager
import kolmachikhin.alexander.epictodolist.ui.products.LockedProductFragment.Companion.open
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class ChallengesFragment(container: ViewGroup) : UIFragment(R.layout.challenges_fragment, container) {

    private val orderManager = ModelOrderManager<ChallengeModel>(context, "ChallengesFragment")
    private lateinit var buttonCreate: ImageView
    private lateinit var unlockContainer: FrameLayout
    private lateinit var rvContainer: FrameLayout
    private lateinit var challengesRVFragment: ChallengesRVFragment

    override fun findViews() {
        buttonCreate = find(R.id.button_create)
        rvContainer = find(R.id.rv_container)
        unlockContainer = find(R.id.unlock_container)
    }

    override fun start() {
        set(buttonCreate) {
            ChallengeMakerDialog.open {
                challengesRVFragment.addItem(it)
            }
        }

        if (MainActivity.core!!.productsLogic.isChallengesUnlocked) {
            setChallenges()
        } else {
            buttonCreate.visibility = View.GONE
            open(MainActivity.core!!.productsLogic.getFeature(FeaturesStorage.CHALLENGES_ID), unlockContainer) {
                if (MainActivity.core!!.productsLogic.isChallengesUnlocked) {
                    setChallenges()
                    unlockContainer.visibility = View.GONE
                    buttonCreate.visibility = View.VISIBLE
                }
            }
        }

        openIfNotLearned(LearnMessageStorage.CHALLENGES_FRAGMENT)
    }

    private fun setChallenges() {
        challengesRVFragment = ChallengesRVFragment(rvContainer)
        challengesRVFragment.list = orderManager.sorted(MainActivity.core!!.challengesLogic.getChallenges())
        challengesRVFragment.replace()
        addDrag(challengesRVFragment, orderManager)
    }

    override fun remove() {
        Animations.hideView(container) { super.remove() }
    }

    override fun replace() {
        Animations.hideView(container) { super.replace() }
    }

    override fun add() {
        super.add()
        Animations.showView(container, null)
    }
}