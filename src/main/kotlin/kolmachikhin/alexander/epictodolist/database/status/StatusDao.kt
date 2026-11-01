package kolmachikhin.alexander.epictodolist.database.status

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StatusDao {

    @Query("SELECT * FROM status LIMIT 1")
    fun get(): StatusModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(status: StatusModel)

    @Query("DELETE FROM status")
    fun clear()
}
