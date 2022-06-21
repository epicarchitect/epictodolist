package kolmachikhin.alexander.epictodolist.database.tasks.repeatable

import androidx.room.Entity
import kolmachikhin.alexander.epictodolist.database.tasks.TaskModel

@Entity(tableName = "repeatable_tasks")
class RepeatableTaskModel : TaskModel {

    var copyDays: ArrayList<Boolean>? = ArrayList()
    var copyTime: Long = 0
    var notCreateIfExists = true

    constructor(
        id: Int,
        content: String,
        difficulty: Int,
        skillId: Int,
        copyDays: ArrayList<Boolean>,
        copyTime: Long,
        notCreateIfExists: Boolean
    ) : super(id, content, difficulty, skillId) {
        this.copyTime = copyTime
        this.copyDays = copyDays
        this.notCreateIfExists = notCreateIfExists
        while (copyDays.size < 7) copyDays.add(false)
    }

    constructor(task: TaskModel) : super(task) {
        while (copyDays!!.size < 7) copyDays!!.add(false)
    }

    constructor() {
        while (copyDays!!.size < 7) copyDays!!.add(false)
    }

    fun copyHour() = (copyTime / 1000 / 60 / 60).toInt()

    fun copyMinute() = (copyTime / 1000 / 60 % 60).toInt()

}