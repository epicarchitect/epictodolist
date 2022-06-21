package kolmachikhin.alexander.epictodolist.database.creator

import kolmachikhin.alexander.epictodolist.database.Model
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage

class CreatorModel : Model {

    var learnList = ArrayList<Boolean>()

    constructor(learnList: ArrayList<Boolean>) : super(CREATOR_ID) {
        this.learnList = learnList
    }

    constructor() : super(CREATOR_ID) {
        for (i in 0 until LearnMessageStorage.size()) {
            learnList.add(false)
        }
    }

    fun learn(i: Int) {
        learnList[i] = true
    }

    fun forgetAllLearnList() {
        addNewLearns()
        for (i in 0 until LearnMessageStorage.size()) {
            learnList[i] = false
        }
    }

    fun learnAll() {
        addNewLearns()
        for (i in learnList.indices) {
            learnList[i] = true
        }
    }

    private fun addNewLearns() {
        while (LearnMessageStorage.size() > learnList.size) {
            learnList.add(false)
        }
    }

    companion object {
        const val CREATOR_ID = -3
    }
}