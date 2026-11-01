package kolmachikhin.alexander.epictodolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kolmachikhin.alexander.epictodolist.database.achievements.IncompleteAchievementModel
import kolmachikhin.alexander.epictodolist.database.achievements.IncompleteAchievementsDao
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengesDao
import kolmachikhin.alexander.epictodolist.database.creator.CreatorDao
import kolmachikhin.alexander.epictodolist.database.creator.CreatorModel
import kolmachikhin.alexander.epictodolist.database.hero.HeroDao
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.database.migrations.MIGRATION_3_4
import kolmachikhin.alexander.epictodolist.database.migrations.MIGRATION_4_5
import kolmachikhin.alexander.epictodolist.database.migrations.MIGRATION_5_6
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationModel
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationsDao
import kolmachikhin.alexander.epictodolist.database.products.IncompleteProductModel
import kolmachikhin.alexander.epictodolist.database.products.IncompleteProductsDao
import kolmachikhin.alexander.epictodolist.database.settings.SettingsDao
import kolmachikhin.alexander.epictodolist.database.settings.SettingsModel
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.database.skills.SkillsDao
import kolmachikhin.alexander.epictodolist.database.status.StatusDao
import kolmachikhin.alexander.epictodolist.database.status.StatusModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTasksDao
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTasksDao
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTasksDao

@Database(
    entities = [
        IncompleteAchievementModel::class,
        ChallengeModel::class,
        NotificationModel::class,
        IncompleteProductModel::class,
        SkillModel::class,
        CompletedTaskModel::class,
        CurrentTaskModel::class,
        RepeatableTaskModel::class,
        HeroModel::class,
        CreatorModel::class,
        SettingsModel::class,
        StatusModel::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(
    IntListConverter::class,
    BoolListConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun incompleteAchievementsDao(): IncompleteAchievementsDao
    abstract fun challengesDao(): ChallengesDao
    abstract fun notificationsDao(): NotificationsDao
    abstract fun incompleteProductsDao(): IncompleteProductsDao
    abstract fun skillsDao(): SkillsDao
    abstract fun completedTasksDao(): CompletedTasksDao
    abstract fun currentTasksDao(): CurrentTasksDao
    abstract fun repeatableTasksDao(): RepeatableTasksDao
    abstract fun heroDao(): HeroDao
    abstract fun creatorDao(): CreatorDao
    abstract fun settingsDao(): SettingsDao
    abstract fun statusDao(): StatusDao

    companion object {
        var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "epic_db"
                    )
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                        .build()
            }
            return instance!!
        }
    }
}
