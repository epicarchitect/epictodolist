package kolmachikhin.alexander.epictodolist.database.status

import androidx.room.Entity
import androidx.room.Ignore
import kolmachikhin.alexander.epictodolist.database.Model

@Entity(tableName = "status")
class StatusModel : Model {

    var idCounter = 0

    var wastedCoins = 0

    var wastedCrystals = 0

    var earnedCoins = 0

    var earnedCrystals = 0

    var timeInApp: Long = 0

    @Ignore
    constructor(
        idCounter: Int,
        wastedCoins: Int,
        earnedCoins: Int,
        wastedCrystals: Int,
        earnedCrystals: Int,
        timeInApp: Long
    ) : super(STATUS_ID) {
        this.idCounter = idCounter
        this.wastedCoins = wastedCoins
        this.earnedCoins = earnedCoins
        this.earnedCrystals = earnedCrystals
        this.wastedCrystals = wastedCrystals
        this.timeInApp = timeInApp
    }

    constructor() : super(STATUS_ID)

    companion object {
        const val STATUS_ID = 0
    }
}
