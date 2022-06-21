package kolmachikhin.alexander.epictodolist.util.progress

object ProgressUtils {

    private val levelUpPoint = intArrayOf(
        0, 100, 200, 300, 500, 700,
        1000, 1300, 1600, 1900, 2400
    )

    fun getLevel(progress: Int): Int {
        if (progress == 0) return 1
        var stackLevels = 0
        var level = 0
        var i = 1
        while (progress >= stackLevels && level < levelUpPoint.size - 1) {
            stackLevels += levelUpPoint[i]
            level++
            i++
        }
        return level
    }

    fun getProgressOfLevel(progress: Int): Int {
        val level = getLevel(progress)
        var stackLevels = 0
        var i = 1
        while (progress > stackLevels && level < levelUpPoint.size - 1) {
            stackLevels += levelUpPoint[i]
            i++
        }
        val result = levelUpPoint[level] + progress - stackLevels
        return if (result == levelUpPoint[level]) 0 else result
    }

    fun getPointLevelUp(level: Int) = levelUpPoint[level]


    fun getProgressPercent(progress: Int) = getProgressOfLevel(progress) * 100 / levelUpPoint[getLevel(progress)]

    val wholeNeedProgress: Int
        get() {
            var p = 0
            for (i in levelUpPoint) p += i
            return p
        }
}