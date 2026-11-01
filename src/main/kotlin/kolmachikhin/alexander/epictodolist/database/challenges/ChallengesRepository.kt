package kolmachikhin.alexander.epictodolist.database.challenges

import android.content.Context
import androidx.lifecycle.map
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class ChallengesRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).challengesDao()
    val liveList = dao.getLiveList().map { ArrayList(it) }

    fun save(model: ChallengeModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<ChallengeModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: ChallengeModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: ChallengesRepository? = null

        fun getInstance(context: Context): ChallengesRepository {
            if (instance == null) instance = ChallengesRepository(context)
            return instance!!
        }
    }
}