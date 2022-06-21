package kolmachikhin.alexander.epictodolist.database.achievements

import androidx.room.Entity
import kolmachikhin.alexander.epictodolist.database.Model

@Entity(tableName = "incomplete_achievements")
open class IncompleteAchievementModel : Model {


    var isAchieved = false

    constructor()

    constructor(id: Int, isAchieved: Boolean) : super(id) {
        this.isAchieved = isAchieved
    }
}