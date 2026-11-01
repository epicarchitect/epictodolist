package kolmachikhin.alexander.epictodolist.ui.creator

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.isVisible
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
    private lateinit var buttonRate: Button
    private lateinit var buttonSupportCreator: Button
    private lateinit var buttonPrivacyPolicy: Button
    private lateinit var buttonGithub: Button
    private lateinit var buttonBreakBadHabits: FrameLayout

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
        set(buttonRate) { MainActivity.ui!!.goToAppInStore() }
        set(buttonPrivacyPolicy) { MainActivity.ui!!.goToPrivacyPolicy() }
        set(buttonBreakBadHabits) { MainActivity.ui!!.goToBreakBadHabits() }
        set(buttonGithub) { MainActivity.ui!!.goToGithub() }
        set(buttonAsk) { showQuestions() }
        set(buttonHideQuestions) { hideQuestions() }
        setRvQuestions()
        animator =
            AnimatorInflater.loadAnimator(context, R.animator.hide_after_speech) as AnimatorSet
        animator.setTarget(tvAnswer)
        set(buttonSupportCreator) {
            SupportCreatorDialog.open {
                scrollView.smoothScrollTo(0, 0)
                showAnswer(MainActivity.ui!!.getString(R.string.thanks_for_your_support))
            }
        }

        MainActivity.core!!.billingLogic.isAvailable.observeForever {
            buttonSupportCreator.isVisible = it
        }
    }

    override fun findViews() {
        buttonAsk = find(R.id.button_ask)
        buttonRate = find(R.id.button_rate)
        buttonSupportCreator = find(R.id.button_support_creator)
        buttonPrivacyPolicy = find(R.id.button_privacy_policy)
        buttonHideQuestions = find(R.id.button_hide_questions)
        buttonBreakBadHabits = find(R.id.button_breakBadHabits)
        questionsLayout = find(R.id.questions_layout)
        buttonGithub = find(R.id.button_open_source_on_github)
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