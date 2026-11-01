package kolmachikhin.alexander.epictodolist.database.creator

import android.content.Context
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class CreatorRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).creatorDao()

    fun save(creator: CreatorModel) {
        creator.id = CreatorModel.CREATOR_ID
        dao.save(creator)
    }

    val creator: CreatorModel
        get() = dao.get() ?: CreatorModel()

    fun clearData() {
        dao.clear()
    }

    companion object {
        var instance: CreatorRepository? = null

        fun getInstance(context: Context): CreatorRepository {
            if (instance == null) instance = CreatorRepository(context.applicationContext)
            return instance!!
        }
    }
}
