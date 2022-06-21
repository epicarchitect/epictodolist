package kolmachikhin.alexander.epictodolist.ui.creator

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.ViewGroup
import android.widget.*
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog.Companion.openIfNotLearned
import kolmachikhin.alexander.epictodolist.ui.hero.HeroBodyFragment
import kolmachikhin.alexander.epictodolist.ui.hero.HeroBodyFragment.Companion.needHeight
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class CreatorFragment(container: ViewGroup) : UIFragment(R.layout.creator_fragment, container) {

    private lateinit var buttonHideQuestions: ImageView
    private lateinit var buttonAsk: Button
    private lateinit var questionsLayout: FrameLayout
    private lateinit var creatorFrame: FrameLayout
    private lateinit var tvAnswer: TextView
    private lateinit var creatorBody: HeroBodyFragment
    private lateinit var creatorBodyContainer: FrameLayout
    private lateinit var rvContainer: FrameLayout
    private lateinit var scrollView: ScrollView
    private lateinit var speechMaster: SpeechMaster
    private lateinit var questionsRVFragment: QuestionsRVFragment
    private lateinit var animator: AnimatorSet

    override fun start() {
        openIfNotLearned(LearnMessageStorage.CREATOR_NAV)
        speechMaster = SpeechMaster(tvAnswer)
        creatorBody = HeroBodyFragment(creatorBodyContainer)
        creatorBody.setMode(HeroBodyFragment.CREATOR)
        creatorBody.add()
        val params = creatorFrame.layoutParams
        params.height = needHeight
        creatorFrame.layoutParams = params
        set(buttonAsk) { showQuestions() }
        set(buttonHideQuestions) { hideQuestions() }
        setRvQuestions()
        animator = AnimatorInflater.loadAnimator(context, R.animator.hide_after_speech) as AnimatorSet
        animator.setTarget(tvAnswer)
    }

    override fun findViews() {
        buttonAsk = find(R.id.button_ask)
        buttonHideQuestions = find(R.id.button_hide_questions)
        questionsLayout = find(R.id.questions_layout)
        rvContainer = find(R.id.rv_container)
        tvAnswer = find(R.id.tv_answer)
        creatorFrame = find(R.id.creator_frame)
        creatorBodyContainer = find(R.id.creator_body_container)
        scrollView = find(R.id.scroll_view)
    }

    private fun showQuestions() {
        Animations.slideView(questionsLayout, questionsLayout.height, view.height)
    }

    private fun hideQuestions() {
        Animations.slideView(questionsLayout, questionsLayout.height, 0)
    }

    private fun setRvQuestions() {
        questionsRVFragment = QuestionsRVFragment(rvContainer)
        questionsRVFragment.listener = QuestionsRVFragment.Listener { message ->
            hideQuestions()
            showAnswer(message.answer)
        }
        questionsRVFragment.replace()
    }

    private fun showAnswer(answer: String?) {
        animator.cancel()
        Animations.showView(tvAnswer, null)
        speechMaster.start(answer!!) { animator.start() }
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