package kolmachikhin.alexander.epictodolist.database.creator

import androidx.room.Entity
import androidx.room.Ignore
import kolmachikhin.alexander.epictodolist.database.Model
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage

@Entity(tableName = "creator")
class CreatorModel : Model {

    var learnList = ArrayList<Boolean>()

    @Ignore
    constructor(learnList: ArrayList<Boolean>) : super(CREATOR_ID) {
        this.learnList = learnList
    }

    constructor() : super(CREATOR_ID) {
        for (i in 0 until LearnMessageStorage.size()) {
            learnList.add(false)
        }
    }

    fun learn(i: Int) {
        ensureLearnListSize()
        learnList[i] = true
    }

    fun forgetAllLearnList() {
        ensureLearnListSize()
        for (i in 0 until LearnMessageStorage.size()) {
            learnList[i] = false
        }
    }

    fun learnAll() {
        ensureLearnListSize()
        for (i in learnList.indices) {
            learnList[i] = true
        }
    }

    fun ensureLearnListSize() {
        while (LearnMessageStorage.size() > learnList.size) {
            learnList.add(false)
        }
    }

    companion object {
        const val CREATOR_ID = 0
    }
}
