package kolmachikhin.alexander.epictodolist.database.skills

import android.content.Context
import androidx.lifecycle.Transformations
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class SkillsRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).skillsDao()
    val liveList = Transformations.map(dao.getLiveList()) { ArrayList(it) }

    fun save(model: SkillModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<SkillModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: SkillModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: SkillsRepository? = null

        
        fun getInstance(context: Context): SkillsRepository {
            if (instance == null) instance = SkillsRepository(context)
            return instance!!
        }
    }
}