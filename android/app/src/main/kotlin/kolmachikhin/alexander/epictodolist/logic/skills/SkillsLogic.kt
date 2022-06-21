package kolmachikhin.alexander.epictodolist.logic.skills

import kolmachikhin.alexander.epictodolist.database.Model.Companion.isVoid
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.database.skills.SkillsRepository
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic
import kolmachikhin.alexander.epictodolist.util.rewards.Reward

class SkillsLogic(core: Core) : Logic(core) {

    private val repository by lazy { SkillsRepository.getInstance(context) }
    private var skills = ArrayList<SkillModel>()

    val maxLevel: Int
        get() {
            var max = 1
            for (skill in skills) {
                val level = skill.level()
                if (level > max) max = level
            }
            return max
        }

    override fun postInit() {
        core.currentTasksLogic
            .observer
            .addListener(CurrentTasksLogic.COMPLETE, this@SkillsLogic::improve)
    }

    override fun attachRef() {
        repository.liveList.observeForever { list ->
            setSkills(list)
        }
    }

    val observer = Observer<SkillModel>()

    fun getSkills() = ArrayList(skills)

    private fun setSkills(list: ArrayList<SkillModel>) {
        skills = list
        sort(skills, true)
        ready()
    }

    fun findById(id: Int) = findModel(skills, id) ?: SkillModel()

    private fun improve(skill: SkillModel, reward: Reward): SkillModel {
        if (isVoid(skill)) return skill

        val oldLevel = skill.level()
        skill.progress += reward.progress
        val newLevel = skill.level()
        observer.notify(IMPROVE, skill)
        if (newLevel > oldLevel) observer.notify(LEVEL_UP, skill)
        return update(skill)
    }

    private fun improve(task: CurrentTaskModel) = improve(
        findById(task.skillId),
        task.reward()
    )

    fun create(skill: SkillModel): SkillModel {
        skill.id = core.statusLogic.nextId
        skills.add(0, skill)
        repository.save(skill)
        observer.notify(CREATE, skill)
        return skill
    }

    fun update(skill: SkillModel): SkillModel {
        for (i in skills.indices) {
            if (skill.id == skills[i].id) {
                skills[i] = skill
                break
            }
        }

        repository.save(skill)
        observer.notify(UPDATE, skill)
        return skill
    }

    fun delete(skill: SkillModel): SkillModel {
        for (i in skills.indices) {
            if (skill.id == skills[i].id) {
                skills.removeAt(i)
                break
            }
        }

        repository.delete(skill)
        observer.notify(DELETE, skill)
        return skill
    }

    companion object {
        const val IMPROVE = 0
        const val LEVEL_UP = 1
        const val CREATE = 2
        const val UPDATE = 3
        const val DELETE = 4
    }
}