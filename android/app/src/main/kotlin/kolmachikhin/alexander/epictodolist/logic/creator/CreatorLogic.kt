package kolmachikhin.alexander.epictodolist.logic.creator

import kolmachikhin.alexander.epictodolist.database.creator.CreatorModel
import kolmachikhin.alexander.epictodolist.database.creator.CreatorRepository
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic

class CreatorLogic(core: Core) : Logic(core) {

    private val repository by lazy { CreatorRepository.getInstance(context) }

    override fun postInit() {}

    override fun attachRef() {
        ready()
    }

    val creator: CreatorModel
        get() = repository.creator

    fun learn(i: Int) {
        val creator = creator
        creator.learn(i)
        repository.save(creator)
    }

    fun isLearned(i: Int): Boolean {
        return creator.learnList[i]
    }

    fun forgetAllLearned() {
        val creator = creator
        creator.forgetAllLearnList()
        repository.save(creator)
    }

    fun skipAllLearned() {
        val creator = creator
        creator.learnAll()
        repository.save(creator)
    }
}