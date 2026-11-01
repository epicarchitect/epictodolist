package kolmachikhin.alexander.epictodolist.database.hero

import android.content.Context
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class HeroRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).heroDao()

    fun save(hero: HeroModel) {
        dao.save(hero)
    }

    val hero: HeroModel?
        get() = dao.get()

    fun clearData() {
        dao.clear()
    }

    companion object {
        var instance: HeroRepository? = null

        fun getInstance(context: Context): HeroRepository {
            if (instance == null) instance = HeroRepository(context)
            return instance!!
        }
    }
}
