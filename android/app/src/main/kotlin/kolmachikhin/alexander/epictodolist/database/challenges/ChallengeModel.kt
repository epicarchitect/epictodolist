package kolmachikhin.alexander.epictodolist.database.challenges

import androidx.room.Entity
import androidx.room.TypeConverters
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper
import kolmachikhin.alexander.epictodolist.database.Model

@Entity(tableName = "challenges")
@TypeConverters(ChallengeTaskListConverter::class)
class ChallengeModel : Model {

    var title: String? = ""
    var tasks: ArrayList<ChallengeTaskModel>? = ArrayList()
    var iconId = -1
    var isActive = false
    var currentDay = 0
    var needDays = 5
    var countFails = 0
    var countCompletes = 0

    constructor(
        id: Int,
        title: String,
        tasks: ArrayList<ChallengeTaskModel>,
        iconId: Int,
        isActive: Boolean,
        currentDay: Int,
        needDays: Int,
        countFails: Int,
        countCompletes: Int
    ) : super(id) {
        this.title = title
        this.tasks = tasks
        this.iconId = iconId
        this.isActive = isActive
        this.currentDay = currentDay
        this.needDays = needDays
        this.countFails = countFails
        this.countCompletes = countCompletes
    }

    constructor()

    fun progressPercent() = when (currentDay) {
        0 -> 0
        else -> (currentDay.toFloat() / needDays * 100).toInt()
    }

    fun iconRes() = IconHelper.getIcon(iconId)

}