package kolmachikhin.alexander.epictodolist.logic.appstate

import kolmachikhin.alexander.epictodolist.database.achievements.IncompleteAchievementModel
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.database.creator.CreatorModel
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationModel
import kolmachikhin.alexander.epictodolist.database.products.IncompleteProductModel
import kolmachikhin.alexander.epictodolist.database.settings.SettingsModel
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.database.status.StatusModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel

class AppState(
    val epicVersion: Int,
    val achievements: List<IncompleteAchievementModel>,
    val challenges: List<ChallengeModel>,
    val creator: CreatorModel,
    val hero: HeroModel,
    val notifications: List<NotificationModel>,
    val products: List<IncompleteProductModel>,
    val settings: SettingsModel,
    val skills: List<SkillModel>,
    val status: StatusModel,
    val completedTasks: List<CompletedTaskModel>,
    val currentTasks: List<CurrentTaskModel>,
    val repeatableTasks: List<RepeatableTaskModel>,
    val currentTasksOrder: List<Int>,
    val repeatableTasksOrder: List<Int>,
    val challengesOrder: List<Int>,
    val skillsOrder: List<Int>
)