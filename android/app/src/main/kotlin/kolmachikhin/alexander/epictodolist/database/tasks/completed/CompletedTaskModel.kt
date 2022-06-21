package kolmachikhin.alexander.epictodolist.database.tasks.completed

import androidx.room.Entity
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster
import kolmachikhin.alexander.epictodolist.database.tasks.TaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel

@Entity(tableName = "completed_tasks")
class CompletedTaskModel : TaskModel {

    var completionDate: Long = -1
    var isDeleted = false

    constructor(id: Int, content: String, difficulty: Int, skillId: Int, completionDate: Long, isDeleted: Boolean) : super(
        id,
        content,
        difficulty,
        skillId
    ) {
        this.completionDate = completionDate
        this.isDeleted = isDeleted
    }

    constructor(id: Int, content: String, difficulty: Int, skillId: Int, completionDate: Long) : super(id, content, difficulty, skillId) {
        this.completionDate = completionDate
    }

    constructor(task: CurrentTaskModel) : super(task) {
        completionDate = TimeMaster.currentTimeInMillis
    }

    constructor()

    fun completionYear(): Int {
        val timeMaster = TimeMaster()
        timeMaster.timeInMillis = completionDate
        return timeMaster.year
    }

    fun completionMonth(): Int {
        val timeMaster = TimeMaster()
        timeMaster.timeInMillis = completionDate
        return timeMaster.month
    }

    fun completionDay(): Int {
        val timeMaster = TimeMaster()
        timeMaster.timeInMillis = completionDate
        return timeMaster.day
    }

    fun completionDateInString(): String {
        val timeMaster = TimeMaster()
        timeMaster.timeInMillis = completionDate
        return timeMaster.timeDateText
    }
}