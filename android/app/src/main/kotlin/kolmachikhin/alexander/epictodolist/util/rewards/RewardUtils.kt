package kolmachikhin.alexander.epictodolist.util.rewards

object RewardUtils {

    private fun getCoins(difficulty: Int) = when (difficulty) {
        Difficulty.EASY -> 10
        Difficulty.MIDDLE -> 25
        Difficulty.HARD -> 60
        Difficulty.LEGENDARY -> 100
        else -> error("RewardUtils::getCoins - difficulty: $difficulty")
    }

    private fun getProgress(difficulty: Int) = when (difficulty) {
        Difficulty.EASY -> 5
        Difficulty.MIDDLE -> 15
        Difficulty.HARD -> 30
        Difficulty.LEGENDARY -> 50
        else -> error("RewardUtils::getProgress - difficulty: $difficulty")
    }

    private fun getAttributePoints(difficulty: Int) = when (difficulty) {
        Difficulty.EASY -> 2
        Difficulty.MIDDLE -> 3
        Difficulty.HARD -> 4
        Difficulty.LEGENDARY -> 5
        else -> error("RewardUtils::getAttributePoints - difficulty: $difficulty")
    }

    fun getReward(difficulty: Int) = Reward(
        coins = getCoins(difficulty),
        attributePoints = getAttributePoints(difficulty),
        progress = getProgress(difficulty)
    )
}