package kolmachikhin.alexander.epictodolist.database.hero

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HeroDao {

    @Query("SELECT * FROM hero LIMIT 1")
    fun get(): HeroModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(hero: HeroModel)

    @Query("DELETE FROM hero")
    fun clear()
}
