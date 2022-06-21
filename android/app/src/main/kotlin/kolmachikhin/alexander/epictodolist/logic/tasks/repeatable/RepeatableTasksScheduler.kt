package kolmachikhin.alexander.epictodolist.logic.tasks.repeatable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.Model.Companion.isVoid
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationModel
import kolmachikhin.alexander.epictodolist.logic.Core

class RepeatableTasksScheduler : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Core.with(context) { core ->
            val task = core.repeatableTasksLogic.findById(intent.getIntExtra(TASK_ID, -1))

            if (isVoid(task)) return@with

            if (task.notCreateIfExists) {
                for (currentTask in core.currentTasksLogic.currentTasks) {
                    if (task.content == currentTask.content && task.skillId == currentTask.skillId) {
                        core.repeatableTasksLogic.installRepeatableTask(task)
                        return@with
                    }
                }
            }

            core.currentTasksLogic.create(task)

            if (core.settingsLogic.getSettings().showNewTaskNotification) {
                core.notificationsLogic.sendNotification(
                    NotificationModel(
                        System.currentTimeMillis().toInt(),
                        context.getString(R.string.new_task),
                        task.content!!,
                        0
                    )
                )
            }

            core.repeatableTasksLogic.installRepeatableTask(task)
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}