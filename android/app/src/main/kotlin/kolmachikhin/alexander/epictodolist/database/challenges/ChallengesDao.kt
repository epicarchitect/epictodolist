package kolmachikhin.alexander.epictodolist.database.challenges

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChallengesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: ChallengeModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<ChallengeModel>)

    @Delete
    fun delete(model: ChallengeModel)

    @Query("SELECT * FROM challenges")
    fun getLiveList(): LiveData<List<ChallengeModel>>
}