package kolmachikhin.alexander.epictodolist.database.creator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CreatorDao {

    @Query("SELECT * FROM creator LIMIT 1")
    fun get(): CreatorModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(creator: CreatorModel)

    @Query("DELETE FROM creator")
    fun clear()
}
