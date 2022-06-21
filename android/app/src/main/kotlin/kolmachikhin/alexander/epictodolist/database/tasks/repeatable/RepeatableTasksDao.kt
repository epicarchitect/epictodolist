package kolmachikhin.alexander.epictodolist.database.tasks.repeatable

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RepeatableTasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: RepeatableTaskModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<RepeatableTaskModel>)

    @Delete
    fun delete(model: RepeatableTaskModel)

    @Query("SELECT * FROM repeatable_tasks")
    fun getLiveList(): LiveData<List<RepeatableTaskModel>>

}