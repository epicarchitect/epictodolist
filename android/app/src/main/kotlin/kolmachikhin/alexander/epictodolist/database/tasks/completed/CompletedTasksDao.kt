package kolmachikhin.alexander.epictodolist.database.tasks.completed

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CompletedTasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: CompletedTaskModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<CompletedTaskModel>)

    @Delete
    fun delete(model: CompletedTaskModel)

    @Query("SELECT * FROM completed_tasks")
    fun getLiveList(): LiveData<List<CompletedTaskModel>>

}