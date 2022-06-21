package kolmachikhin.alexander.epictodolist.logic.achievements

import kolmachikhin.alexander.epictodolist.database.achievements.AchievementModel
import kolmachikhin.alexander.epictodolist.database.achievements.AchievementsRepository
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import kolmachikhin.alexander.epictodolist.logic.hero.HeroLogic
import kolmachikhin.alexander.epictodolist.logic.products.ProductsLogic
import kolmachikhin.alexander.epictodolist.logic.skills.SkillsLogic
import kolmachikhin.alexander.epictodolist.logic.status.StatusLogic
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic
import kolmachikhin.alexander.epictodolist.ui.achievements.AchievementDialog

class AchievementsLogic(core: Core) : Logic(core) {

    private var achievements = ArrayList<AchievementModel>()
    private val repository by lazy { AchievementsRepository.getInstance(context) }

    val observer = Observer<AchievementModel>()

    override fun postInit() {
        core.currentTasksLogic
            .observer
            .addListener(CurrentTasksLogic.CREATE) { checkAchievements() }
            .addListener(CurrentTasksLogic.COMPLETE) { checkAchievements() }

        core.heroLogic
            .observer
            .addListener(HeroLogic.UPDATE) { checkAchievements() }

        core.skillsLogic
            .observer
            .addListener(SkillsLogic.IMPROVE) { checkAchievements() }

        core.statusLogic
            .observer
            .addListener(StatusLogic.UPDATE) { checkAchievements() }
        core.productsLogic
            .observer
            .addListener(ProductsLogic.UNLOCK) { checkAchievements() }

        observer.addListener(ACHIEVE) { achievement ->
            AchievementDialog.open(achievement)
        }
    }

    override fun attachRef() {
        repository.liveList.observeForever { list -> setAchievements(list) }
    }

    val countAchievedAchievements: Int
        get() {
            var count = 0
            for (achievement in achievements) if (achievement.isAchieved) count++
            return count
        }

    fun getAchievements(): ArrayList<AchievementModel> {
        return ArrayList(achievements)
    }

    private fun setAchievements(list: ArrayList<AchievementModel>) {
        achievements = list
        sort(achievements, false)
        ready()
    }

    private fun achieveAchievement(achievement: AchievementModel) {
        if (!achievement.isAchieved) {
            achievement.isAchieved = true
            update(achievement)
            observer.notify(ACHIEVE, achievement)
        }
    }

    private fun checkAchievements() {
        try {
            for (a in achievements) if (!a.isAchieved && AchievementModel.isTrue(a)) achieveAchievement(a)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun update(achievement: AchievementModel): AchievementModel {
        for (i in achievements.indices) {
            if (achievement.id == achievements[i].id) {
                achievements[i] = achievement
                break
            }
        }
        repository.save(achievement)
        observer.notify(UPDATE, achievement)
        return achievement
    }

    companion object {
        const val UPDATE = 1
        const val ACHIEVE = 3
    }
}