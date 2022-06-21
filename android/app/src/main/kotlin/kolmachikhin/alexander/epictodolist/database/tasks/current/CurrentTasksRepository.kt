package kolmachikhin.alexander.epictodolist.database.tasks.current

import android.content.Context
import androidx.lifecycle.Transformations
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class CurrentTasksRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).currentTasksDao()
    val liveList = Transformations.map(dao.getLiveList()) { ArrayList(it) }

    fun save(model: CurrentTaskModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<CurrentTaskModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: CurrentTaskModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: CurrentTasksRepository? = null

        
        fun getInstance(context: Context): CurrentTasksRepository {
            if (instance == null) instance = CurrentTasksRepository(context)
            return instance!!
        }
    }
}