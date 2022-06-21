package kolmachikhin.alexander.epictodolist.storage.achievements

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.achievements.AchievementModel
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.with
import kolmachikhin.alexander.epictodolist.logic.hero.Attribute
import kolmachikhin.alexander.epictodolist.logic.products.ProductType
import kolmachikhin.alexander.epictodolist.util.rewards.Difficulty
import kolmachikhin.alexander.epictodolist.util.rewards.Reward

object AchievementsStorage {

    val list: ArrayList<AchievementModel>
        get() {
            val l = ArrayList<AchievementModel>()
            for (i in 0 until size()) l.add(get(i))
            return l
        }

    
    fun size() = 22



    operator fun get(i: Int, achieved: Boolean = false): AchievementModel {
        val core = with(context!!)

        return when (i) {
            0 -> AchievementModel(
                i,
                s(R.string.the_beginning_of_the_way),
                s(R.string.complete_one_task),
                R.drawable.icon_12,
                Reward(100, 10, 5),
                1,
                achieved
            ) { core.completedTasksLogic.notDeletedCompletedTasks.size }
            1 -> AchievementModel(
                i,
                s(R.string.progressive),
                s(R.string.accumulate_500_total_hero_progress),
                R.drawable.icon_11,
                Reward(500, 40, 20),
                500,
                achieved
            ) { core.heroLogic.hero.progress }
            2 -> AchievementModel(
                i,
                s(R.string.sportsman),
                s(R.string.accumulate_100_attribute_strength_points),
                R.drawable.icon_16,
                Reward(300, 30, 20),
                100,
                achieved
            ) { core.heroLogic.hero.strength }
            3 -> AchievementModel(
                i,
                s(R.string.scientist),
                s(R.string.accumulate_100_attribute_intellect_points),
                R.drawable.icon_17,
                Reward(300, 30, 20),
                100,
                achieved
            ) { core.heroLogic.hero.intellect }
            4 -> AchievementModel(
                i,
                s(R.string.creative_person),
                s(R.string.accumulate_100_attribute_creation_points),
                R.drawable.icon_10,
                Reward(300, 30, 20),
                100,
                achieved
            ) { core.heroLogic.hero.creation }
            5 -> AchievementModel(
                i,
                s(R.string.healthy),
                s(R.string.accumulate_100_attribute_health_points),
                R.drawable.icon_5,
                Reward(300, 30, 20),
                100,
                achieved
            ) { core.heroLogic.hero.health }
            6 -> AchievementModel(
                i,
                s(R.string.spender),
                s(R.string.spend_1000_coins),
                R.drawable.icon_7,
                Reward(300, 20, 30),
                1000,
                achieved
            ) { core.statusLogic.status.wastedCoins }
            7 -> AchievementModel(
                i,
                s(R.string.banker),
                s(R.string.accumulate_1000_coins),
                R.drawable.icon_7,
                Reward(300, 20, 30),
                1000,
                achieved
            ) { core.heroLogic.hero.coins }
            8 -> AchievementModel(
                i,
                s(R.string.im_not_small_anymore),
                s(R.string.reach_hero_level_5),
                R.drawable.icon_15,
                Reward(300, 50, 50),
                5,
                achieved
            ) { core.heroLogic.hero.level() }
            9 -> AchievementModel(
                i,
                s(R.string.gladiator),
                s(R.string.complete_20_tasks_with_the_strength_attribute_skill),
                R.drawable.icon_5,
                Reward(250, 20, 50),
                20,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByAttribute(Attribute.STRENGTH) }
            10 -> AchievementModel(
                i,
                s(R.string.thinker),
                s(R.string.complete_20_tasks_with_the_intellect_attribute_skill),
                R.drawable.icon_8,
                Reward(250, 20, 50),
                20,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByAttribute(Attribute.INTELLECT) }
            11 -> AchievementModel(
                i,
                s(R.string.wizard),
                s(R.string.complete_20_tasks_with_the_creation_attribute_skill),
                R.drawable.icon_10,
                Reward(250, 20, 50),
                20,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByAttribute(Attribute.CREATION) }
            12 -> AchievementModel(
                i,
                s(R.string.healer),
                s(R.string.complete_20_tasks_with_the_health_attribute_skill),
                R.drawable.ic_woody_theme,
                Reward(250, 20, 50),
                20,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByAttribute(Attribute.HEALTH) }
            13 -> AchievementModel(
                i,
                s(R.string.easy_peasy),
                s(R.string.complete_10_easy_tasks),
                R.drawable.icon_1,
                Reward(50, 20, 10),
                10,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByDifficulty(Difficulty.EASY) }
            14 -> AchievementModel(
                i,
                s(R.string.not_difficult),
                s(R.string.complete_10_middle_tasks),
                R.drawable.icon_1,
                Reward(100, 30, 20),
                10,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByDifficulty(Difficulty.MIDDLE) }
            15 -> AchievementModel(
                i,
                s(R.string.difficulties_cannot_be_avoided),
                s(R.string.complete_10_hard_tasks),
                R.drawable.icon_15,
                Reward(200, 40, 40),
                10,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByDifficulty(Difficulty.HARD) }
            16 -> AchievementModel(
                i,
                s(R.string.legend),
                s(R.string.complete_10_legendary_tasks),
                R.drawable.icon_26,
                Reward(300, 50, 50),
                10,
                achieved
            ) { core.completedTasksLogic.getCountCompletedTasksByDifficulty(Difficulty.LEGENDARY) }
            17 -> AchievementModel(
                i,
                s(R.string.themes_never_happen_much),
                s(R.string.unlock_5_themes),
                R.drawable.icon_4,
                Reward(200, 20, 30),
                5,
                achieved
            ) { core.productsLogic.getCountUnlockedProducts(ProductType.THEME) }
            18 -> AchievementModel(
                i,
                s(R.string.armed_to_the_teeth),
                s(R.string.unlock_5_weapons),
                R.drawable.icon_29,
                Reward(200, 20, 30),
                5,
                achieved
            ) { core.productsLogic.getCountUnlockedProducts(ProductType.WEAPON) }
            19 -> AchievementModel(
                i,
                s(R.string.elegant),
                s(R.string.unlock_5_costumes),
                R.drawable.icon_3,
                Reward(200, 20, 30),
                5,
                achieved
            ) { core.productsLogic.getCountUnlockedProducts(ProductType.COSTUME) }
            20 -> AchievementModel(
                i,
                s(R.string.traveler),
                s(R.string.unlock_5_locations),
                R.drawable.icon_21,
                Reward(200, 20, 30),
                5,
                achieved
            ) { core.productsLogic.getCountUnlockedProducts(ProductType.LOCATION) }
            21 -> AchievementModel(
                i,
                s(R.string.functional),
                s(R.string.unlock_5_features),
                R.drawable.icon_18,
                Reward(200, 20, 30),
                5,
                achieved
            ) { core.productsLogic.getCountUnlockedProducts(ProductType.FEATURE) }
            else -> AchievementModel()
        }
    }

    private fun s(id: Int): String {
        val c = context
        return c?.getString(id) ?: "Something went wrong :("
    }
}