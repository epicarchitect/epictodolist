package kolmachikhin.alexander.epictodolist.database.skills

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SkillsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: SkillModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<SkillModel>)

    @Delete
    fun delete(model: SkillModel)

    @Query("SELECT * FROM skills")
    fun getLiveList(): LiveData<List<SkillModel>>
}