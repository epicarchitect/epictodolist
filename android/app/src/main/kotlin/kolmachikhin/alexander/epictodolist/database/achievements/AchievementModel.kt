package kolmachikhin.alexander.epictodolist.database.achievements

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.achievements.AchievementModel.CurrentCountGetter
import kolmachikhin.alexander.epictodolist.util.rewards.Reward

class AchievementModel : IncompleteAchievementModel {

    var title = ""
    var description = ""
    var iconRes = R.drawable.icon_default
    var reward = Reward()
    var needCount = 1
    var currentCountCalculator = CurrentCountGetter { 0 }

    fun interface CurrentCountGetter {
        fun getCurrentCount(): Int
    }

    constructor(
        id: Int,
        title: String,
        description: String,
        iconRes: Int,
        reward: Reward,
        needCount: Int,
        isAchieved: Boolean,
        currentCountCalculator: CurrentCountGetter
    ) : super(id, isAchieved) {
        this.title = title
        this.description = description
        this.iconRes = iconRes
        this.reward = reward
        this.needCount = needCount
        this.currentCountCalculator = currentCountCalculator
    }

    constructor()

    companion object {
        fun isTrue(a: AchievementModel): Boolean {
            return a.currentCountCalculator.getCurrentCount() >= a.needCount
        }
    }
}