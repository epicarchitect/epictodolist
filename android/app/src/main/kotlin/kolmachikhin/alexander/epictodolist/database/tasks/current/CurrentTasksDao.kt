package kolmachikhin.alexander.epictodolist.database.tasks.current

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrentTasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: CurrentTaskModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<CurrentTaskModel>)

    @Delete
    fun delete(model: CurrentTaskModel)

    @Query("SELECT * FROM current_tasks")
    fun getLiveList(): LiveData<List<CurrentTaskModel>>

}