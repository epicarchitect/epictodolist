package kolmachikhin.alexander.epictodolist.database.achievements

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IncompleteAchievementsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: IncompleteAchievementModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<IncompleteAchievementModel>)

    @Delete
    fun delete(model: IncompleteAchievementModel)

    @Query("SELECT * FROM incomplete_achievements WHERE id = :id")
    fun getById(id: Int): IncompleteAchievementModel?

    @Query("SELECT * FROM incomplete_achievements")
    fun getLiveList(): LiveData<List<IncompleteAchievementModel>>

    @Query("SELECT * FROM incomplete_achievements")
    fun getList(): List<IncompleteAchievementModel>
}