package kolmachikhin.alexander.epictodolist.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `hero` (
                    `name` TEXT NOT NULL,
                    `gender` INTEGER NOT NULL,
                    `coins` INTEGER NOT NULL,
                    `crystals` INTEGER NOT NULL,
                    `strength` INTEGER NOT NULL,
                    `intellect` INTEGER NOT NULL,
                    `creation` INTEGER NOT NULL,
                    `health` INTEGER NOT NULL,
                    `location` INTEGER NOT NULL,
                    `backWeapon` INTEGER NOT NULL,
                    `ears` INTEGER NOT NULL,
                    `body` INTEGER NOT NULL,
                    `extras` INTEGER NOT NULL,
                    `beard` INTEGER NOT NULL,
                    `eyes` INTEGER NOT NULL,
                    `brows` INTEGER NOT NULL,
                    `hair` INTEGER NOT NULL,
                    `colorBody` INTEGER NOT NULL,
                    `colorHair` INTEGER NOT NULL,
                    `progress` INTEGER NOT NULL,
                    `id` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent()
        )
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `creator` (
                    `learnList` TEXT NOT NULL,
                    `id` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent()
        )
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `settings` (
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
                CREATE TABLE IF NOT EXISTS `status` (
                    `idCounter` INTEGER NOT NULL,
                    `wastedCoins` INTEGER NOT NULL,
                    `wastedCrystals` INTEGER NOT NULL,
                    `earnedCoins` INTEGER NOT NULL,
                    `earnedCrystals` INTEGER NOT NULL,
                    `timeInApp` INTEGER NOT NULL,
                    `id` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent()
        )
    }
}
