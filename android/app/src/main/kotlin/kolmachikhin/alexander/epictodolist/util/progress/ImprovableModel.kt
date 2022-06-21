package kolmachikhin.alexander.epictodolist.util.progress

import kolmachikhin.alexander.epictodolist.database.Model

abstract class ImprovableModel : Model {
    
    var progress = 0

    constructor(id: Int, progress: Int) : super(id) {
        this.progress = progress
    }

    constructor()

    fun level() = ProgressUtils.getLevel(progress)

    fun progressOfLevel() = ProgressUtils.getProgressOfLevel(progress)

    fun pointOfLevelUp() = ProgressUtils.getPointLevelUp(level())

    fun progressPercent() = ProgressUtils.getProgressPercent(progress)

    fun wholeNeedProgress() = ProgressUtils.wholeNeedProgress

}