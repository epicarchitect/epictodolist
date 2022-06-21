package kolmachikhin.alexander.epictodolist.database.tasks.completed

import android.content.Context
import androidx.lifecycle.Transformations
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class CompletedTasksRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).completedTasksDao()
    val liveList = Transformations.map(dao.getLiveList()) { ArrayList(it) }

    fun save(model: CompletedTaskModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<CompletedTaskModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: CompletedTaskModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: CompletedTasksRepository? = null

        
        fun getInstance(context: Context): CompletedTasksRepository {
            if (instance == null) instance = CompletedTasksRepository(context)
            return instance!!
        }
    }
}