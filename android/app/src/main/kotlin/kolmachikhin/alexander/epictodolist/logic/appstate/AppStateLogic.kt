package kolmachikhin.alexander.epictodolist.logic.appstate

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kolmachikhin.alexander.epictodolist.BuildConfig
import kolmachikhin.alexander.epictodolist.database.AppDatabase
import kolmachikhin.alexander.epictodolist.database.Model
import kolmachikhin.alexander.epictodolist.database.achievements.AchievementsRepository
import kolmachikhin.alexander.epictodolist.database.achievements.IncompleteAchievementModel
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeTaskModel
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengesRepository
import kolmachikhin.alexander.epictodolist.database.creator.CreatorModel
import kolmachikhin.alexander.epictodolist.database.creator.CreatorRepository
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.database.hero.HeroRepository
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationModel
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationsRepository
import kolmachikhin.alexander.epictodolist.database.products.IncompleteProductModel
import kolmachikhin.alexander.epictodolist.database.products.ProductsRepository
import kolmachikhin.alexander.epictodolist.database.settings.SettingsModel
import kolmachikhin.alexander.epictodolist.database.settings.SettingsRepository
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.database.skills.SkillsRepository
import kolmachikhin.alexander.epictodolist.database.status.StatusModel
import kolmachikhin.alexander.epictodolist.database.status.StatusRepository
import kolmachikhin.alexander.epictodolist.database.tasks.TaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTasksRepository
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTasksRepository
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTasksRepository
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.ui.drag.ModelOrderManager
import kolmachikhin.alexander.epictodolist.util.json.JsonUtils

class AppStateLogic(core: Core) : Logic(core) {

    private val achievementsRepository by lazy { AchievementsRepository.getInstance(context) }
    private val challengesRepository by lazy { ChallengesRepository.getInstance(context) }
    private val creatorRepository by lazy { CreatorRepository.getInstance(context) }
    private val heroRepository by lazy { HeroRepository.getInstance(context) }
    private val notificationsRepository by lazy { NotificationsRepository.getInstance(context) }
    private val productsRepository by lazy { ProductsRepository.getInstance(context) }
    private val settingsRepository by lazy { SettingsRepository.getInstance(context) }
    private val skillsRepository by lazy { SkillsRepository.getInstance(context) }
    private val statusRepository by lazy { StatusRepository.getInstance(context) }
    private val completedTasksRepository by lazy { CompletedTasksRepository.getInstance(context) }
    private val currentTasksRepository by lazy { CurrentTasksRepository.getInstance(context) }
    private val repeatableTasksRepository by lazy { RepeatableTasksRepository.getInstance(context) }
    private val currentTasksOrderManager by lazy { ModelOrderManager<CurrentTaskModel>(context, "CurrentTasksFragment") }
    private val repeatableTasksOrderManager by lazy { ModelOrderManager<RepeatableTaskModel>(context, "RepeatableTasksFragment") }
    private val challengesOrderManager by lazy { ModelOrderManager<ChallengeModel>(context, "ChallengesFragment") }
    private val skillsOrderManager by lazy { ModelOrderManager<SkillModel>(context, "SkillsFragment") }

    override fun attachRef() {
        ready()
    }

    fun applyAppStateFromJson(json: JsonObject) {
        val state = appStateFromJson(json)
        AppDatabase.getInstance(context).clearAllTables()
        heroRepository.clearData()
        creatorRepository.clearData()
        statusRepository.clearData()
        achievementsRepository.saveList(state.achievements)
        challengesRepository.saveList(state.challenges)
        creatorRepository.save(state.creator)
        heroRepository.save(state.hero)
        notificationsRepository.saveList(state.notifications)
        productsRepository.saveList(state.products)
        settingsRepository.save(state.settings)
        skillsRepository.saveList(state.skills)
        statusRepository.save(state.status)
        completedTasksRepository.saveList(state.completedTasks)
        currentTasksRepository.saveList(state.currentTasks)
        repeatableTasksRepository.saveList(state.repeatableTasks)
        currentTasksOrderManager.save(state.currentTasksOrder)
        repeatableTasksOrderManager.save(state.repeatableTasksOrder)
        challengesOrderManager.save(state.challengesOrder)
        skillsOrderManager.save(state.skillsOrder)
    }

    private val appState: AppState
        get() = AppState(
            BuildConfig.VERSION_CODE,
            achievementsRepository.incompleteLiveList.value!!,
            challengesRepository.liveList.value!!,
            creatorRepository.creator,
            heroRepository.hero!!,
            notificationsRepository.liveList.value!!,
            productsRepository.incompleteLiveList.value!!,
            settingsRepository.settings,
            skillsRepository.liveList.value!!,
            statusRepository.status,
            completedTasksRepository.liveList.value!!,
            currentTasksRepository.liveList.value!!,
            repeatableTasksRepository.liveList.value!!,
            currentTasksOrderManager.savedOrderIdList,
            repeatableTasksOrderManager.savedOrderIdList,
            challengesOrderManager.savedOrderIdList,
            skillsOrderManager.savedOrderIdList
        )

    override fun postInit() {}

    fun appStateJson() = appStateToJson(appState)

    private fun appStateToJson(state: AppState) = JsonObject().apply {
        addProperty("epicVersion", state.epicVersion)
        add("achievements", achievementsToJson(state.achievements))
        add("challenges", challengesToJson(state.challenges))
        add("creator", creatorToJson(state.creator))
        add("hero", heroToJson(state.hero))
        add("notifications", notificationsToJsonArray(state.notifications))
        add("products", productsToJsonArray(state.products))
        add("settings", settingsToJson(state.settings))
        add("skills", skillsToJsonArray(state.skills))
        add("status", statusToJson(state.status))
        add("completedTasks", completedTasksToJsonArray(state.completedTasks))
        add("currentTasks", currentTasksToJsonArray(state.currentTasks))
        add("repeatableTasks", repeatableTasksToJsonArray(state.repeatableTasks))
        add("currentTasksOrder", JsonUtils.intListToJsonArray(state.currentTasksOrder))
        add("repeatableTasksOrder", JsonUtils.intListToJsonArray(state.repeatableTasksOrder))
        add("challengesOrder", JsonUtils.intListToJsonArray(state.challengesOrder))
        add("skillsOrder", JsonUtils.intListToJsonArray(state.skillsOrder))
    }

    private fun appStateFromJson(json: JsonObject) = AppState(
        json["epicVersion"].asInt,
        achievementsFromJsonArray(json["achievements"].asJsonArray),
        challengesFromJsonArray(json["challenges"].asJsonArray),
        creatorFromJson(json["creator"].asJsonObject),
        heroFromJson(json["hero"].asJsonObject),
        notificationsFromJsonArray(json["notifications"].asJsonArray),
        productsFromJsonArray(json["products"].asJsonArray),
        settingsFromJson(json["settings"].asJsonObject),
        skillsFromJsonArray(json["skills"].asJsonArray),
        statusFromJson(json["status"].asJsonObject),
        completedTasksFromJsonArray(json["completedTasks"].asJsonArray),
        currentTasksFromJsonArray(json["currentTasks"].asJsonArray),
        repeatableTasksFromJsonArray(json["repeatableTasks"].asJsonArray),
        JsonUtils.intListFromJsonArray(json["currentTasksOrder"].asJsonArray),
        JsonUtils.intListFromJsonArray(json["repeatableTasksOrder"].asJsonArray),
        JsonUtils.intListFromJsonArray(json["challengesOrder"].asJsonArray),
        JsonUtils.intListFromJsonArray(json["skillsOrder"].asJsonArray)
    )

    private fun achievementsToJson(models: List<IncompleteAchievementModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(achievementToJson(m))
        return array
    }

    private fun achievementsFromJsonArray(array: JsonArray): ArrayList<IncompleteAchievementModel> {
        val list = ArrayList<IncompleteAchievementModel>()
        for (i in 0 until array.size()) list.add(achievementFromJson(array[i].asJsonObject))
        return list
    }

    private fun challengesToJson(models: List<ChallengeModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(challengeToJson(m))
        return array
    }

    private fun challengesFromJsonArray(array: JsonArray): ArrayList<ChallengeModel> {
        val list = ArrayList<ChallengeModel>()
        for (i in 0 until array.size()) list.add(challengeFromJson(array[i].asJsonObject))
        return list
    }

    private fun challengeTasksToJsonArray(models: List<ChallengeTaskModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(challengeTaskToJson(m))
        return array
    }

    private fun challengeTasksFromJsonArray(array: JsonArray): ArrayList<ChallengeTaskModel> {
        val list = ArrayList<ChallengeTaskModel>()
        for (i in 0 until array.size()) list.add(challengeTaskFromJson(array[i].asJsonObject))
        return list
    }

    private fun notificationsToJsonArray(models: List<NotificationModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(notificationToJson(m))
        return array
    }

    private fun notificationsFromJsonArray(array: JsonArray): ArrayList<NotificationModel> {
        val list = ArrayList<NotificationModel>()
        for (i in 0 until array.size()) list.add(notificationFromJson(array[i].asJsonObject))
        return list
    }

    private fun productsToJsonArray(models: List<IncompleteProductModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(productToJson(m))
        return array
    }

    private fun productsFromJsonArray(array: JsonArray): ArrayList<IncompleteProductModel> {
        val list = ArrayList<IncompleteProductModel>()
        for (i in 0 until array.size()) list.add(productFromJson(array[i].asJsonObject))
        return list
    }

    private fun skillsToJsonArray(models: List<SkillModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(skillToJson(m))
        return array
    }

    private fun skillsFromJsonArray(array: JsonArray): ArrayList<SkillModel> {
        val list = ArrayList<SkillModel>()
        for (i in 0 until array.size()) list.add(skillFromJson(array[i].asJsonObject))
        return list
    }

    private fun completedTasksToJsonArray(models: List<CompletedTaskModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(completedTaskToJson(m))
        return array
    }

    private fun completedTasksFromJsonArray(array: JsonArray): ArrayList<CompletedTaskModel> {
        val list = ArrayList<CompletedTaskModel>()
        for (i in 0 until array.size()) list.add(completedTaskFromJson(array[i].asJsonObject))
        return list
    }

    private fun currentTasksToJsonArray(models: List<CurrentTaskModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(currentTaskToJson(m))
        return array
    }

    private fun currentTasksFromJsonArray(array: JsonArray): ArrayList<CurrentTaskModel> {
        val list = ArrayList<CurrentTaskModel>()
        for (i in 0 until array.size()) list.add(currentTaskFromJson(array[i].asJsonObject))
        return list
    }

    private fun repeatableTasksToJsonArray(models: List<RepeatableTaskModel>?): JsonArray {
        val array = JsonArray()
        if (models == null) return array
        for (m in models) array.add(repeatableTaskToJson(m))
        return array
    }

    private fun repeatableTasksFromJsonArray(array: JsonArray): ArrayList<RepeatableTaskModel> {
        val list = ArrayList<RepeatableTaskModel>()
        for (i in 0 until array.size()) list.add(repeatableTaskFromJson(array[i].asJsonObject))
        return list
    }

    private fun statusToJson(model: StatusModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("idCounter", model.idCounter)
        json.addProperty("wastedCoins", model.wastedCoins)
        json.addProperty("earnedCoins", model.earnedCoins)
        json.addProperty("earnedCrystals", model.earnedCrystals)
        json.addProperty("wastedCrystals", model.wastedCrystals)
        json.addProperty("timeInApp", model.timeInApp)
        return json
    }

    private fun statusFromJson(json: JsonObject) = StatusModel(
        json["idCounter"].asInt,
        json["earnedCoins"].asInt,
        json["wastedCoins"].asInt,
        json["earnedCrystals"].asInt,
        json["wastedCrystals"].asInt,
        json["timeInApp"].asLong
    )

    private fun skillToJson(model: SkillModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("title", model.title)
        json.addProperty("iconId", model.iconId)
        json.addProperty("attribute", model.attribute)
        json.addProperty("progress", model.progress)
        return json
    }

    private fun skillFromJson(json: JsonObject) = SkillModel(
        json["id"].asInt,
        json["title"].asString,
        json["attribute"].asInt,
        json["iconId"].asInt,
        json["progress"].asInt
    )

    private fun settingsToJson(model: SettingsModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("showTitleNav", model.showTitleNav)
        json.addProperty("showNewTaskNotification", model.showNewTaskNotification)
        json.addProperty("showHeaddress", model.showHeaddress)
        return json
    }

    private fun settingsFromJson(json: JsonObject) = SettingsModel(
        json["showTitleNav"].asBoolean,
        json["showNewTaskNotification"].asBoolean,
        "-1",
        json["showHeaddress"].asBoolean
    )

    private fun productToJson(model: IncompleteProductModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("type", model.type)
        json.addProperty("countParts", model.countParts)
        return json
    }

    private fun productFromJson(json: JsonObject) = IncompleteProductModel(
        json["id"].asInt,
        json["type"].asInt,
        json["countParts"].asInt
    )

    private fun notificationToJson(model: NotificationModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("title", model.title)
        json.addProperty("content", model.content)
        json.addProperty("time", model.time)
        return json
    }

    private fun notificationFromJson(json: JsonObject) = NotificationModel(
        json["id"].asInt,
        json["title"].asString,
        json["content"].asString,
        json["time"].asLong
    )

    private fun heroToJson(model: HeroModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("name", model.name)
        json.addProperty("progress", model.progress)
        json.addProperty("gender", model.gender)
        json.addProperty("coins", model.coins)
        json.addProperty("crystals", model.crystals)
        json.addProperty("strength", model.strength)
        json.addProperty("intellect", model.intellect)
        json.addProperty("creation", model.creation)
        json.addProperty("health", model.health)
        json.addProperty("location", model.location)
        json.addProperty("backWeapon", model.backWeapon)
        json.addProperty("ears", model.ears)
        json.addProperty("body", model.body)
        json.addProperty("extras", model.extras)
        json.addProperty("beard", model.beard)
        json.addProperty("eyes", model.eyes)
        json.addProperty("brows", model.brows)
        json.addProperty("hair", model.hair)
        json.addProperty("colorBody", model.colorBody)
        json.addProperty("colorHair", model.colorHair)
        return json
    }

    private fun heroFromJson(json: JsonObject) = HeroModel(
        json["name"].asString,
        json["progress"].asInt,
        json["gender"].asInt,
        json["coins"].asInt,
        json["crystals"].asInt,
        json["strength"].asInt,
        json["intellect"].asInt,
        json["creation"].asInt,
        json["health"].asInt,
        json["location"].asInt,
        json["backWeapon"].asInt,
        json["ears"].asInt,
        json["body"].asInt,
        json["extras"].asInt,
        json["beard"].asInt,
        json["eyes"].asInt,
        json["brows"].asInt,
        json["hair"].asInt,
        json["colorBody"].asInt,
        json["colorHair"].asInt
    )

    private fun creatorToJson(model: CreatorModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.add("learnList", JsonUtils.boolListToJsonArray(model.learnList))
        return json
    }

    private fun creatorFromJson(json: JsonObject) = CreatorModel(
        JsonUtils.boolListFromJsonArray(json["learnList"].asJsonArray)
    )

    private fun achievementToJson(model: IncompleteAchievementModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("isAchieved", model.isAchieved)
        return json
    }

    private fun achievementFromJson(json: JsonObject) = IncompleteAchievementModel(
        json["id"].asInt,
        json["isAchieved"].asBoolean
    )

    private fun challengeToJson(model: ChallengeModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("title", model.title)
        json.addProperty("iconId", model.iconId)
        json.addProperty("isActive", model.isActive)
        json.addProperty("currentDay", model.currentDay)
        json.addProperty("needDays", model.needDays)
        json.addProperty("countFails", model.countFails)
        json.addProperty("countCompletes", model.countCompletes)
        json.add("tasks", challengeTasksToJsonArray(model.tasks))
        return json
    }

    private fun challengeFromJson(json: JsonObject) = ChallengeModel(
        json["id"].asInt,
        json["title"].asString,
        challengeTasksFromJsonArray(json["tasks"].asJsonArray),
        json["iconId"].asInt,
        json["isActive"].asBoolean,
        json["currentDay"].asInt,
        json["needDays"].asInt,
        json["countFails"].asInt,
        json["countCompletes"].asInt
    )

    private fun challengeTaskToJson(model: ChallengeTaskModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("isCompleted", model.isCompleted)
        json.add("currentTask", currentTaskToJson(model.currentTask))
        return json
    }

    private fun challengeTaskFromJson(json: JsonObject) = ChallengeTaskModel(
        json["id"].asInt,
        currentTaskFromJson(json["currentTask"].asJsonObject),
        json["isCompleted"].asBoolean
    )

    private fun repeatableTaskToJson(model: RepeatableTaskModel?): JsonObject {
        val json = taskToJson(model)
        if (model == null) return json
        json.addProperty("copyTime", model.copyTime)
        json.addProperty("notCreateIfExists", model.notCreateIfExists)
        json.add("copyDays", JsonUtils.boolListToJsonArray(model.copyDays!!))
        return json
    }

    private fun repeatableTaskFromJson(json: JsonObject) = RepeatableTaskModel(
        json["id"].asInt,
        json["content"].asString,
        json["difficulty"].asInt,
        json["skillId"].asInt,
        JsonUtils.boolListFromJsonArray(json["copyDays"].asJsonArray),
        json["copyTime"].asLong,
        json["notCreateIfExists"].asBoolean
    )

    private fun completedTaskToJson(model: CompletedTaskModel?): JsonObject {
        val json = taskToJson(model)
        if (model == null) return json
        json.addProperty("completionDate", model.completionDate)
        json.addProperty("isDeleted", model.isDeleted)
        return json
    }

    private fun completedTaskFromJson(json: JsonObject) = CompletedTaskModel(
        json["id"].asInt,
        json["content"].asString,
        json["difficulty"].asInt,
        json["skillId"].asInt,
        json["completionDate"].asLong,
        json["isDeleted"].asBoolean
    )

    private fun currentTaskToJson(model: CurrentTaskModel?): JsonObject {
        val json = taskToJson(model)
        if (model == null) return json
        json.add("notificationIds", JsonUtils.intListToJsonArray(model.notificationIds!!))
        return json
    }

    private fun currentTaskFromJson(json: JsonObject) = CurrentTaskModel(
        json["id"].asInt,
        json["content"].asString,
        json["difficulty"].asInt,
        json["skillId"].asInt,
        JsonUtils.intListFromJsonArray(json["notificationIds"].asJsonArray)
    )

    private fun taskToJson(model: TaskModel?): JsonObject {
        val json = modelToJson(model)
        if (model == null) return json
        json.addProperty("content", model.content)
        json.addProperty("difficulty", model.difficulty)
        json.addProperty("skillId", model.skillId)
        return json
    }

    private fun modelToJson(model: Model?): JsonObject {
        val json = JsonObject()
        if (model == null) return json
        json.addProperty("id", model.id)
        return json
    }
}