package kolmachikhin.alexander.epictodolist.ui.creator

import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.notifications.NotificationMaker
import kolmachikhin.alexander.epictodolist.util.rewards.Difficulty
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class CreatorRateAppDialog : CreatorLearnDialog(MainActivity.dialogContainer!!) {

    private lateinit var learnButtonsLayout: LinearLayout
    private lateinit var reminderOfferTaskLayout: LinearLayout
    private lateinit var buttonDone: Button
    private lateinit var buttonCancel: Button
    private lateinit var notificationMaker: NotificationMaker
    private lateinit var notificationMakerContainer: FrameLayout

    override fun openDialog() {
        add()
        Animations.showView(mainLayout, null)
        MainActivity.ui!!.hideKeyboard()
    }

    override fun findViews() {
        super.findViews()
        learnButtonsLayout = find(R.id.learn_buttons_layout)
        reminderOfferTaskLayout = find(R.id.reminder_offer_task_layout)
        buttonDone = find(R.id.button_done)
        buttonCancel = find(R.id.button_cancel)
        notificationMakerContainer = find(R.id.notification_maker_container)
    }

    override fun start() {
        super.start()
        buttonGotIt.visibility = View.GONE
        notificationMaker = NotificationMaker(notificationMakerContainer)
        notificationMaker.add()

        set(buttonDone) {
            val task = CurrentTaskModel()
            task.difficulty = Difficulty.LEGENDARY
            task.skillId = -1
            task.content = MainActivity.ui!!.getString(R.string.rate_epic_to_do_list_on_play_market)
            val notification = notificationMaker.createBy(task)
            task.notificationIds!!.add(notification.id)
            MainActivity.core!!.currentTasksLogic.create(task)
            closeDialog()
        }

        set(buttonCancel) {
            learnButtonsLayout.visibility = View.VISIBLE
            reminderOfferTaskLayout.visibility = View.GONE
            Animations.showView(learnButtonsLayout, null)
            Animations.hideView(reminderOfferTaskLayout, null)
        }

        set(buttonQuestion0) {
            setTvMessage(model!!.getAnswer(0)) {
                MainActivity.ui!!.goToAppInStore()
                closeDialog()
            }
        }

        set(buttonQuestion1) {
            setTvMessage(model!!.getAnswer(1)) {
                learnButtonsLayout.visibility = View.GONE
                reminderOfferTaskLayout.visibility = View.VISIBLE
                Animations.showView(reminderOfferTaskLayout, null)
                Animations.hideView(learnButtonsLayout, null)
                notificationMaker.openTimeAndDateDialogs()
            }
        }

        set(buttonQuestion2) {
            setTvMessage(model!!.getAnswer(2)) {
                closeDialog()
            }
        }
    }
}