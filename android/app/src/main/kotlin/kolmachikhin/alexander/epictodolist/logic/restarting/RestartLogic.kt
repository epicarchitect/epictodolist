package kolmachikhin.alexander.epictodolist.logic.restarting

import android.app.PendingIntent
import android.content.Intent
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.tasks.repeatable.RepeatableTasksScheduler
import kolmachikhin.alexander.epictodolist.logic.challenges.ChallengeChecker
import kolmachikhin.alexander.epictodolist.logic.notifications.NotificationSender

class RestartLogic(core: Core) : Logic(core) {

    init {
        ready()
    }

    override fun postInit() {}

    override fun attachRef() {}

    fun checkAll() {
        App.runOnBackgroundThread {
            checkNotifications()
            checkRepeatableTasks()
            checkActiveChallenges()
        }
    }

    private fun checkNotifications() {
        for (notification in core.notificationsLogic.getNotifications()) {
            if (!isAlarmExists(notification.id, NotificationSender::class.java)) {
                core.notificationsLogic.installNotification(notification)
            }
        }
    }

    private fun checkRepeatableTasks() {
        for (task in core.repeatableTasksLogic.repeatableTasks) {
            if (!isAlarmExists(task.id, RepeatableTasksScheduler::class.java)) {
                core.repeatableTasksLogic.installRepeatableTask(task)
            }
        }
    }

    private fun checkActiveChallenges() {
        for (challenge in core.challengesLogic.activeChallenges) {
            if (!isAlarmExists(challenge.id, ChallengeChecker::class.java)) {
                core.challengesLogic.installChallengeChecker(challenge)
            }
        }
    }

    private fun isAlarmExists(id: Int, cls: Class<*>?) = PendingIntent.getBroadcast(
        context,
        id,
        Intent(context, cls),
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    ) != null
}