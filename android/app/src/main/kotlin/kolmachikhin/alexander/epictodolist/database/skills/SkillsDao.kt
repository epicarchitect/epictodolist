package kolmachikhin.alexander.epictodolist.database.skills

import androidx.lifecycle.LiveData
import androidx.room.*

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