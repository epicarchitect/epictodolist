package kolmachikhin.alexander.epictodolist.database.notifications

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: NotificationModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<NotificationModel>)

    @Delete
    fun delete(model: NotificationModel)

    @Query("SELECT * FROM notifications")
    fun getLiveList(): LiveData<List<NotificationModel>>
}