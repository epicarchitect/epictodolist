package kolmachikhin.alexander.epictodolist.database.tasks.current

import androidx.room.Entity
import androidx.room.Ignore
import kolmachikhin.alexander.epictodolist.database.tasks.TaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel

@Entity(tableName = "current_tasks")
class CurrentTaskModel : TaskModel {

    var notificationIds: ArrayList<Int>? = ArrayList()

    @Ignore
    constructor(
        id: Int,
        content: String,
        difficulty: Int,
        skillId: Int,
        notificationIds: ArrayList<Int>
    ) : super(id, content, difficulty, skillId) {
        this.notificationIds = notificationIds
    }

    @Ignore
    constructor(task: RepeatableTaskModel) : super(task)

    @Ignore
    constructor(task: CompletedTaskModel) : super(task)

    constructor()
}
