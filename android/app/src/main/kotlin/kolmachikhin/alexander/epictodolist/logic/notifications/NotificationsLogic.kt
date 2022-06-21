package kolmachikhin.alexander.epictodolist.logic.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.Model.Companion.isVoid
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationModel
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationsRepository
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic
import kolmachikhin.alexander.epictodolist.ui.MainActivity

class NotificationsLogic(core: Core) : Logic(core) {

    private val notificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val repository by lazy { NotificationsRepository.getInstance(context) }
    private var notifications: ArrayList<NotificationModel> = ArrayList()

    override fun postInit() {
        core.currentTasksLogic
            .observer
            .addListener(CurrentTasksLogic.DELETE) { task ->
                for (id in task.notificationIds!!) {
                    delete(findById(id))
                }
            }
    }

    override fun attachRef() {
        repository.liveList.observeForever { list ->
            setNotifications(list)
        }
    }

    fun getNotifications() = ArrayList(notifications)

    private fun setNotifications(list: ArrayList<NotificationModel>) {
        notifications = list
        ready()
    }

    fun findById(id: Int) = findModel(notifications, id) ?: NotificationModel()

    fun installNotification(notification: NotificationModel) {
        if (isVoid(notification)) return
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationSender::class.java)
        intent.putExtra(NotificationSender.NOTIFICATION_ID, notification.id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notification.time, pendingIntent)
    }

    private fun cancelNotification(notification: NotificationModel) {
        if (isVoid(notification)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationSender::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun create(notification: NotificationModel): NotificationModel {
        notification.id = core.statusLogic.nextId
        notifications.add(notification)
        repository.save(notification)
        installNotification(notification)
        return notification
    }

    fun delete(notification: NotificationModel): NotificationModel {
        for (i in notifications.indices) {
            if (notification.id == notifications[i].id) {
                notifications.removeAt(i)
                break
            }
        }

        repository.delete(notification)
        cancelNotification(notification)
        return notification
    }

    fun sendNotification(notification: NotificationModel) {
        createChannelAndGroup()

        notificationManager.notify(
            notification.id,
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setGroup(GROUP_NAME)
                .setContentTitle(notification.title)
                .setContentText(notification.content)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        notification.id,
                        Intent(context, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setSmallIcon(R.drawable.ic_sword)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .build()
        )
    }

    private fun createChannelAndGroup() {
        createChannel()
        createGroup()
    }

    private fun createChannel() {
        notificationManager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                enableLights(true)
                lightColor = Color.RED
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
        )
    }

    private fun createGroup() {
        notificationManager.notify(
            GROUP_ID,
            NotificationCompat.Builder(context, "-100")
                .setContentInfo(CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_sword)
                .setChannelId(CHANNEL_ID)
                .setGroup(GROUP_NAME)
                .setAutoCancel(true)
                .setGroupSummary(true)
                .build()
        )
    }

    companion object {
        private const val CHANNEL_NAME = "To do list"
        private const val CHANNEL_ID = "To do list"
        private const val CHANNEL_DESCRIPTION = "To do list"
        private const val GROUP_NAME = "To do list"
        const val GROUP_ID = -100
    }

}