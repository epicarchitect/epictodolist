package kolmachikhin.alexander.epictodolist.logic.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kolmachikhin.alexander.epictodolist.logic.Core

class NotificationSender : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Core.with(context) { core ->
            val notificationId = intent.getIntExtra(NOTIFICATION_ID, -1)
            val notification = core.notificationsLogic.findById(notificationId)
            core.notificationsLogic.sendNotification(notification)
            core.notificationsLogic.delete(notification)
        }
    }

    companion object {
        const val NOTIFICATION_ID = "notification_id"
    }
}