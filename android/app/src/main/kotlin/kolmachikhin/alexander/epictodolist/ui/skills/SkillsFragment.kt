package kolmachikhin.alexander.epictodolist.ui.skills

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.drag.DragHelper
import kolmachikhin.alexander.epictodolist.ui.drag.ModelOrderManager
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class SkillsFragment(container: ViewGroup) : UIFragment(R.layout.skills_fragment, container) {

    private val orderManager = ModelOrderManager<SkillModel>(context, "SkillsFragment")
    private lateinit var buttonCreate: ImageView
    private lateinit var rvContainer: FrameLayout
    private lateinit var skillsRVFragment: SkillsRVFragment

    override fun findViews() {
        buttonCreate = find(R.id.button_create)
        rvContainer = find(R.id.rv_container)
    }

    override fun start() {
        set(buttonCreate) {
            SkillMakerDialog.open {
                skillsRVFragment.addItem(it)
            }
        }
        setSkills()
        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.SKILLS_NAV)
    }

    fun setSkills() {
        skillsRVFragment = SkillsRVFragment(rvContainer)
        skillsRVFragment.list = orderManager.sorted(MainActivity.core!!.skillsLogic.getSkills())
        skillsRVFragment.replace()
        DragHelper.addDrag(skillsRVFragment, orderManager)
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