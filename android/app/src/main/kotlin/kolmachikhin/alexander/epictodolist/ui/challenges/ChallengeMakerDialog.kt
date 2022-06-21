package kolmachikhin.alexander.epictodolist.ui.challenges

import android.annotation.SuppressLint
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeTaskModel
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.MakerDialog
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog.Companion.openIfNotLearned
import kolmachikhin.alexander.epictodolist.ui.icons.IconDialog
import kolmachikhin.alexander.epictodolist.ui.tasks.current.CurrentTaskMakerDialog
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper.getIcon
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class ChallengeMakerDialog : MakerDialog<ChallengeModel>(R.layout.challenge_maker_dialog) {

    private lateinit var icon: ImageView
    private lateinit var etTitle: EditText
    private lateinit var etCountDays: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var rvContainer: FrameLayout
    private lateinit var challengesTasksRVFragment: ChallengesTasksRVFragment

    override fun findViews() {
        super.findViews()
        icon = find(R.id.icon)
        etTitle = find(R.id.et_title)
        etCountDays = find(R.id.et_count_days)
        buttonAddTask = find(R.id.button_add_task)
        rvContainer = find(R.id.rv_container)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        super.start()
        challengesTasksRVFragment = ChallengesTasksRVFragment(rvContainer)
        challengesTasksRVFragment.list = model!!.tasks!!
        challengesTasksRVFragment.replace()
        set(icon) {
            IconDialog.open { iconId ->
                setIcon(iconId)
            }
        }

        set(buttonAddTask) {
            CurrentTaskMakerDialog.open { task ->
                challengesTasksRVFragment.addItem(
                    ChallengeTaskModel(
                        0,
                        task,
                        false
                    )
                )
            }.hideNotifications()
        }

        etTitle.setText(model!!.title)
        etCountDays.setText(model!!.needDays.toString())
        icon.setImageResource(model!!.iconRes())
        icon.contentDescription = model!!.iconId.toString()
    }

    override fun done(): ChallengeModel {
        model!!.title = etTitle.text.toString()
        model!!.iconId = icon.contentDescription.toString().toInt()
        var countDays = 0
        try {
            countDays = etCountDays.text.toString().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        model!!.needDays = countDays
        model!!.tasks = challengesTasksRVFragment.list
        if (modeEdit) {
            MainActivity.core!!.challengesLogic.cancelChallengeChecker(model!!)
            for (task in model!!.tasks!!) {
                task.id = MainActivity.core!!.statusLogic.nextId
                task.currentTask.id = task.id
            }
            MainActivity.core!!.challengesLogic.installChallengeChecker(model!!)
        }
        return if (modeEdit) MainActivity.core!!.challengesLogic.update(model!!) else MainActivity.core!!.challengesLogic.create(model!!)
    }

    private fun setIcon(iconId: Int) {
        icon.setImageResource(getIcon(iconId))
        icon.contentDescription = iconId.toString()
    }

    companion object {
        fun open(onDoneListener: OnDoneListener<ChallengeModel>) {
            val maker = ChallengeMakerDialog()
            maker.setModeCreate()
            maker.onDoneListener = onDoneListener
            maker.model = ChallengeModel(0, "", ArrayList(), 0, false, 0, 0, 0, 0)
            maker.openDialog()
            openIfNotLearned(LearnMessageStorage.CHALLENGE_MAKER)
        }

        fun open(challenge: ChallengeModel, onDoneListener: OnDoneListener<ChallengeModel>) {
            val maker = ChallengeMakerDialog()
            maker.setModeEdit()
            maker.onDoneListener = onDoneListener
            maker.model = challenge
            maker.openDialog()
        }
    }
}