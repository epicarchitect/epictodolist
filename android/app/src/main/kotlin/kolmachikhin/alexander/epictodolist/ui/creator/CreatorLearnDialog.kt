package kolmachikhin.alexander.epictodolist.ui.creator

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.with
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessage
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.MainActivity.Companion.ui
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.animations.NavigationAnimations
import kolmachikhin.alexander.epictodolist.ui.creator.SpeechMaster.OnEndListener
import kolmachikhin.alexander.epictodolist.ui.hero.HeroBodyFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

open class CreatorLearnDialog : Dialog<LearnMessage> {

    protected lateinit var buttonQuestion0: Button
    protected lateinit var buttonQuestion1: Button
    protected lateinit var buttonQuestion2: Button
    protected lateinit var buttonGotIt: Button
    private lateinit var tvMessage: TextView
    private lateinit var creatorFrame: FrameLayout
    private lateinit var messageContainer: FrameLayout
    private lateinit var creatorBodyContainer: FrameLayout
    private lateinit var creatorBody: HeroBodyFragment
    private var speechMaster: SpeechMaster? = null
    var listener: OnDoneListener? = null

    constructor() : super(R.layout.creator_learn_dialog, MainActivity.creatorContainer!!)

    constructor(container: ViewGroup) : super(R.layout.creator_learn_dialog, container)

    override fun findViews() {
        super.findViews()
        messageContainer = find(R.id.message_container)
        creatorFrame = find(R.id.creator_frame)
        buttonQuestion0 = find(R.id.button_question_0)
        buttonQuestion1 = find(R.id.button_question_1)
        buttonQuestion2 = find(R.id.button_question_2)
        buttonGotIt = find(R.id.button_got_it)
        tvMessage = find(R.id.tv_message)
        creatorBodyContainer = find(R.id.creator_body_container)
    }

    override fun start() {
        super.start()
        speechMaster = SpeechMaster(tvMessage)
        creatorBody = HeroBodyFragment(creatorBodyContainer)
        creatorBody.setMode(HeroBodyFragment.CREATOR)
        creatorBody.add()
        creatorBody.hideLocation()
        set(buttonGotIt) { closeDialog() }
        set(buttonQuestion0) { setTvMessage(model!!.getAnswer(0)) }
        set(buttonQuestion1) { setTvMessage(model!!.getAnswer(1)) }
        set(buttonQuestion2) { setTvMessage(model!!.getAnswer(2)) }
        creatorFrame.startAnimation(NavigationAnimations.slideToUp)
        tvTitle?.text = ui!!.getString(R.string.creator)
        setTvMessage(model!!.message)
        setButtonsQuestion()
    }

    private fun setTvMessage(text: String?) {
        setTvMessage(text, null)
    }

    fun setTvMessage(text: String?, listener: OnEndListener?) {
        speechMaster!!.start(text!!, listener)
        ui!!.hideKeyboard()
    }

    private fun setButtonsQuestion() {
        buttonQuestion0.text = model!!.getQuestion(0)
        buttonQuestion1.text = model!!.getQuestion(1)
        buttonQuestion2.text = model!!.getQuestion(2)
        val length: Int = model!!.questions.size
        if (length < 3) buttonQuestion2.visibility = View.GONE
        if (length < 2) buttonQuestion1.visibility = View.GONE
        if (length < 1) buttonQuestion0.visibility = View.GONE
    }

    override fun openDialog() {
        replace()
        Animations.showView(mainLayout, null)
        ui!!.hideKeyboard()
    }

    override fun closeDialog() {
        buttonGotIt.isClickable = false
        buttonBack.isClickable = false
        creatorFrame.startAnimation(NavigationAnimations.slideToDown)
        super.closeDialog()
        MainActivity.core!!.creatorLogic.learn(model!!.id)
        listener?.onDone()

        if (isShouldRequestPostNotifications()) {
            MainActivity.requestPostNotificationsPermission()
        }
    }

    fun isShouldRequestPostNotifications() =
        model!!.id == LearnMessageStorage.UNLOCK_NOTIFICATION_FEATURE
                || model!!.id == LearnMessageStorage.UNLOCK_REPEATABLE_TASKS_FEATURE
                || model!!.id == LearnMessageStorage.CHALLENGE_MAKER

    fun interface OnDoneListener {
        fun onDone()
    }

    companion object {

        fun open(messageId: Int, listener: OnDoneListener? = null) {
            val dialog = CreatorLearnDialog()
            dialog.model = LearnMessageStorage[messageId]
            dialog.listener = listener
            dialog.openDialog()
        }


        fun openIfNotLearned(messageId: Int) {
            try {
                val core = with(context!!)
                if (core.heroLogic.isHeroCreated) {
                    if (!core.creatorLogic.isLearned(messageId)) {
                        open(messageId)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}