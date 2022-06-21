package kolmachikhin.alexander.epictodolist.logic.hero

import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.database.hero.HeroRepository
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic
import kolmachikhin.alexander.epictodolist.logic.achievements.AchievementsLogic

class HeroLogic(core: Core) : Logic(core) {

    private val repository by lazy { HeroRepository.getInstance(context) }
    val observer = Observer<HeroModel>()

    override fun postInit() {
        core.currentTasksLogic
            .observer
            .addListener(CurrentTasksLogic.COMPLETE) { task ->
                improve(task)
            }

        core.achievementsLogic
            .observer
            .addListener(AchievementsLogic.ACHIEVE) { achievement ->
                val hero = hero
                hero.coins += achievement.reward.coins
                hero.crystals += achievement.reward.crystals
                hero.progress += achievement.reward.progress
                update(hero)
            }
    }

    override fun attachRef() {
        ready()
    }

    fun improve(task: CurrentTaskModel): HeroModel {
        val hero = hero
        val reward = task.reward()
        val skill = core.skillsLogic.findById(task.skillId)
        val oldLevel = hero.level()
        hero.coins += reward.coins
        hero.progress += reward.progress
        val newLevel = hero.level()
        val attr = skill.attribute
        val attrPoints = reward.attributePoints

        when (attr) {
            Attribute.STRENGTH -> hero.strength += attrPoints
            Attribute.INTELLECT -> hero.intellect += attrPoints
            Attribute.CREATION -> hero.creation += attrPoints
            Attribute.HEALTH -> hero.health += attrPoints
        }

        observer.notify(IMPROVE, hero)

        if (newLevel > oldLevel) observer.notify(LEVEL_UP, hero)
        return update(hero)
    }

    val hero: HeroModel
        get() = repository.hero ?: HeroModel()

    fun isHaveCoins(coins: Int) = hero.coins >= coins

    fun create(hero: HeroModel): HeroModel {
        hero.id = HeroModel.HERO_ID
        repository.save(hero)
        observer.notify(CREATE, hero)
        return hero
    }

    fun update(hero: HeroModel): HeroModel {
        repository.save(hero)
        observer.notify(UPDATE, hero)
        return hero
    }

    val isHeroCreated: Boolean
        get() = repository.hero != null

    companion object {
        const val IMPROVE = 0
        const val LEVEL_UP = 1
        const val UPDATE = 2
        const val CREATE = 3
    }
}