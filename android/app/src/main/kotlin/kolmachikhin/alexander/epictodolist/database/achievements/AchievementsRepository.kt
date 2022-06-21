package kolmachikhin.alexander.epictodolist.database.achievements

import android.content.Context
import androidx.lifecycle.Transformations
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.storage.achievements.AchievementsStorage
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class AchievementsRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).incompleteAchievementsDao()
    val incompleteLiveList = Transformations.map(dao.getLiveList()) { ArrayList(it) }
    val liveList = Transformations.map(incompleteLiveList) { models ->
        val list = AchievementsStorage.list
        for (achievement in list) {
            for (model in models) {
                if (achievement.id == model.id) {
                    achievement.isAchieved = model.isAchieved
                    break
                }
            }
        }
        list
    }

    fun save(model: IncompleteAchievementModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<IncompleteAchievementModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: IncompleteAchievementModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: AchievementsRepository? = null

        
        fun getInstance(context: Context): AchievementsRepository {
            if (instance == null) instance = AchievementsRepository(context)
            return instance!!
        }
    }
}