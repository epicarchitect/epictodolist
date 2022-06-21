package kolmachikhin.alexander.epictodolist.database.challenges

import kolmachikhin.alexander.epictodolist.database.Model
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel

class ChallengeTaskModel(
    id: Int,

    var currentTask: CurrentTaskModel,

    var isCompleted: Boolean
) : Model(id)