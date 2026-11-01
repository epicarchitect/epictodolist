package kolmachikhin.alexander.epictodolist.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                ALTER TABLE repeatable_tasks
                ADD COLUMN notCreateIfExists INTEGER DEFAULT 1 NOT NULL
            """.trimIndent()
        )
    }
}
