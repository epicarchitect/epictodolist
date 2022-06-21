package kolmachikhin.alexander.epictodolist.database.notifications

import android.content.Context
import androidx.lifecycle.Transformations
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class NotificationsRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).notificationsDao()
    val liveList = Transformations.map(dao.getLiveList()) { ArrayList(it) }

    fun save(model: NotificationModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<NotificationModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: NotificationModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: NotificationsRepository? = null

        
        fun getInstance(context: Context): NotificationsRepository {
            if (instance == null) instance = NotificationsRepository(context)
            return instance!!
        }
    }
}