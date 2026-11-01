package kolmachikhin.alexander.epictodolist.database.status

import android.content.Context
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class StatusRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).statusDao()

    fun save(statusModel: StatusModel) {
        dao.save(statusModel)
    }

    val status: StatusModel
        get() = dao.get() ?: StatusModel()

    fun clearData() {
        dao.clear()
    }

    companion object {
        var instance: StatusRepository? = null

        fun getInstance(context: Context): StatusRepository {
            if (instance == null) instance = StatusRepository(context.applicationContext)
            return instance!!
        }
    }
}
