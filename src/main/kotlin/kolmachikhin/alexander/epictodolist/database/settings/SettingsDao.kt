package kolmachikhin.alexander.epictodolist.database.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings LIMIT 1")
    fun get(): SettingsModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(settings: SettingsModel)

    @Query("DELETE FROM settings")
    fun clear()
}
