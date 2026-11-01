package kolmachikhin.alexander.epictodolist.database.migrations

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kolmachikhin.alexander.epictodolist.database.AppDatabase
import kolmachikhin.alexander.epictodolist.database.creator.CreatorModel
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.database.settings.SettingsModel
import kolmachikhin.alexander.epictodolist.database.status.StatusModel
import kolmachikhin.alexander.epictodolist.util.json.JsonUtils

/**
 * Одноразовая миграция состояния приложения из старого Gson/SharedPreferences в Room.
 *
 * Добавлена в 2.7.0 (versionCode 100).
 *
 * Не удалять, пока пользователи могут обновляться напрямую с 2.6.x и ниже.
 * Иначе они получат пустую базу Room v5 (MIGRATION_4_5 только создаёт таблицы)
 * и потеряют героя, creator, настройки и status. Задачи уже лежат в Room и не пострадают.
 *
 * Условия удаления (не раньше 2.8.x и через 6–12 месяцев после 2.7.0):
 * 1. Проверить Play Console/аналитику: активных установок ниже 2.7.0 нет
 *    или их доля признана незначительной и потерей данных для них можно пренебречь.
 * 2. Удалить этот файл и вызов в Core.init().
 * 3. Оставить MIGRATION_4_5 и MIGRATION_3_4: они нужны пользователям
 *    со старыми версиями базы.
 * 4. После удаления Gson (в 2.8.0+) убрать строку -applymapping и proguard-mapping.txt,
 *    они нужны только для чтения старого JSON.
 * 5. Старые SharedPreferences и флаг "LegacyStateMigration" при желании удалить позже.
 *
 * Примечания:
 * - Если singleton-строки нет, репозитории возвращают значения по умолчанию,
 *   поэтому удаление этого файла не сломает чистые установки.
 * - Не удалять MIGRATION_4_5 вместе с этим файлом: миграция схемы БД и миграция
 *   старых prefs — независимые вещи.
 */
class LegacyStateMigration private constructor(context: Context) {

    private val appContext = context.applicationContext
    private val database = AppDatabase.getInstance(appContext)
    private val preferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun migrate() {
        if (preferences.getBoolean(KEY_DONE, false)) {
            return
        }

        val hero = readLegacy(HERO_PREFS, HERO_KEY, HeroModel::class.java)
        if (hero != null && HeroModel.isCreated(hero)) {
            database.heroDao().save(hero)
        }

        val creator = readLegacy(CREATOR_PREFS, CREATOR_KEY, CreatorModel::class.java)
        if (creator != null) {
            creator.id = CreatorModel.CREATOR_ID
            database.creatorDao().save(creator)
        } else if (database.creatorDao().get() == null) {
            database.creatorDao().save(CreatorModel())
        }

        val settings = readLegacy(SETTINGS_PREFS, SETTINGS_KEY, SettingsModel::class.java)
            ?: database.settingsDao().get()
            ?: SettingsModel()
        database.settingsDao().save(settings)

        val status = readLegacy(STATUS_PREFS, STATUS_KEY, StatusModel::class.java)
        if (status != null) {
            database.statusDao().save(status)
        } else if (database.statusDao().get() == null) {
            database.statusDao().save(StatusModel())
        }

        preferences.edit { putBoolean(KEY_DONE, true) }
    }

    private fun <M> readLegacy(name: String, key: String, clazz: Class<M>): M? = try {
        val json = legacyPreferences(name).getString(key, "")
        if (json.isNullOrEmpty()) {
            null
        } else {
            JsonUtils.fromJson(json, clazz)
        }
    } catch (_: Throwable) {
        null
    }

    private fun legacyPreferences(name: String): SharedPreferences {
        return appContext.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREFS_NAME = "LegacyStateMigration"
        private const val KEY_DONE = "done_v5"

        private const val HERO_PREFS = "HeroRepository"
        private const val HERO_KEY = "Hero"
        private const val CREATOR_PREFS = "CreatorRepository"
        private const val CREATOR_KEY = "Creator"
        private const val SETTINGS_PREFS = "SettingsRepository"
        private const val SETTINGS_KEY = "Settings"
        private const val STATUS_PREFS = "StatusRepository"
        private const val STATUS_KEY = "Status"

        fun migrate(context: Context) {
            LegacyStateMigration(context.applicationContext).migrate()
        }
    }
}
