package kolmachikhin.alexander.epictodolist.database.tasks.repeatable

import android.content.Context
import androidx.lifecycle.Transformations
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class RepeatableTasksRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).repeatableTasksDao()
    val liveList = Transformations.map(dao.getLiveList()) {  ArrayList(it) }

    fun save(model: RepeatableTaskModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<RepeatableTaskModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: RepeatableTaskModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: RepeatableTasksRepository? = null

        
        fun getInstance(context: Context): RepeatableTasksRepository {
            if (instance == null) instance = RepeatableTasksRepository(context)
            return instance!!
        }
    }
}