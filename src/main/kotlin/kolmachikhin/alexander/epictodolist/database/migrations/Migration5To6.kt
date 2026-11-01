package kolmachikhin.alexander.epictodolist.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `settings_new` (
                    `showTitleNav` INTEGER NOT NULL,
                    `showNewTaskNotification` INTEGER NOT NULL,
                    `lang` TEXT NOT NULL,
                    `showHeaddress` INTEGER NOT NULL,
                    `id` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent()
        )
        db.execSQL(
            """
                INSERT INTO `settings_new` (`id`, `showTitleNav`, `showNewTaskNotification`, `lang`, `showHeaddress`)
                SELECT `id`, `showTitleNav`, `showNewTaskNotification`, `lang`, `showHeaddress`
                FROM `settings`
            """.trimIndent()
        )
        db.execSQL("DROP TABLE `settings`")
        db.execSQL("ALTER TABLE `settings_new` RENAME TO `settings`")
    }
}
