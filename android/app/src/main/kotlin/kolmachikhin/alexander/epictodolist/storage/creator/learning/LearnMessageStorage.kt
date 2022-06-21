package kolmachikhin.alexander.epictodolist.storage.creator.learning

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity

object LearnMessageStorage {
    const val CURRENT_TASKS_NAV = 0
    const val CURRENT_TASK_MAKER = 1
    const val CURRENT_TASK_DIALOG = 2
    const val REPEATABLE_TASKS_NAV = 3
    const val REPEATABLE_TASK_MAKER = 4
    const val REPEATABLE_TASK_DIALOG = 5
    const val COMPLETED_TASKS_NAV = 6
    const val COMPLETED_TASK_DIALOG = 7
    const val SKILLS_NAV = 8
    const val SKILL_MAKER = 9
    const val SKILL_DIALOG = 10
    const val ACHIEVEMENTS_NAV = 11
    const val ACHIEVEMENT_DIALOG = 12
    const val HERO_NAV = 13
    const val HERO_MAKER_EDIT = 14
    const val HERO_MAKER_CREATE = 15
    const val INVENTORY_NAV = 16
    const val PRODUCT_DIALOG = 17
    const val CREATOR_NAV = 18
    const val CREATOR_RATE_APP_OFFER = 19
    const val UNLOCK_NOTIFICATION_FEATURE = 20
    const val UNLOCK_REPEATABLE_TASKS_FEATURE = 21
    const val UNLOCK_TASK_LIST_WIDGET_FEATURE = 22
    const val UNLOCK_ATTRIBUTE_GRAPH_FEATURE = 23
    const val AFTER_CREATE_HERO = 24
    const val CHALLENGES_FRAGMENT = 25
    const val CHALLENGE_MAKER = 26

    fun size() = 27

    
    operator fun get(i: Int) = when (i) {
        CURRENT_TASKS_NAV -> LearnMessage(
            i,
            string(R.string.learn_current_tasks_nav_message),
            array(R.array.learn_current_tasks_nav_questions),
            array(R.array.learn_current_tasks_nav_answers)
        )
        CURRENT_TASK_MAKER -> LearnMessage(
            i,
            string(R.string.learn_current_task_maker_message),
            array(R.array.learn_current_task_maker_questions),
            array(R.array.learn_current_task_maker_answers)
        )
        CURRENT_TASK_DIALOG -> LearnMessage(
            i,
            string(R.string.learn_current_task_dialog_message),
            array(R.array.learn_current_task_dialog_questions),
            array(R.array.learn_current_task_dialog_answers)
        )
        REPEATABLE_TASKS_NAV -> LearnMessage(
            i,
            string(R.string.learn_copy_tasks_nav_message),
            array(R.array.learn_copy_tasks_nav_questions),
            array(R.array.learn_copy_tasks_nav_answers)
        )
        REPEATABLE_TASK_MAKER -> LearnMessage(
            i,
            string(R.string.learn_copy_task_maker_message),
            array(R.array.learn_copy_task_maker_questions),
            array(R.array.learn_copy_task_maker_answers)
        )
        REPEATABLE_TASK_DIALOG -> LearnMessage(
            i,
            string(R.string.learn_copy_task_dialog_message),
            array(R.array.learn_copy_task_dialog_questions),
            array(R.array.learn_copy_task_dialog_answers)
        )
        COMPLETED_TASKS_NAV -> LearnMessage(
            i,
            string(R.string.learn_completed_tasks_nav_message),
            array(R.array.learn_completed_tasks_nav_questions),
            array(R.array.learn_completed_tasks_nav_answers)
        )
        COMPLETED_TASK_DIALOG -> LearnMessage(
            i,
            string(R.string.learn_completed_task_dialog_message),
            array(R.array.learn_completed_task_dialog_questions),
            array(R.array.learn_completed_task_dialog_answers)
        )
        SKILLS_NAV -> LearnMessage(
            i,
            string(R.string.learn_skills_nav_message),
            array(R.array.learn_skills_nav_questions),
            array(R.array.learn_skills_nav_answers)
        )
        SKILL_MAKER -> LearnMessage(
            i,
            string(R.string.learn_skill_maker_message),
            array(R.array.learn_skill_maker_questions),
            array(R.array.learn_skill_maker_answers)
        )
        SKILL_DIALOG -> LearnMessage(
            i,
            string(R.string.learn_skill_dialog_message),
            array(R.array.learn_skill_dialog_questions),
            array(R.array.learn_skill_dialog_answers)
        )
        ACHIEVEMENTS_NAV -> LearnMessage(
            i,
            string(R.string.learn_achievements_nav_message),
            array(R.array.learn_achievement_nav_questions),
            array(R.array.learn_achievement_nav_answers)
        )
        ACHIEVEMENT_DIALOG -> LearnMessage(
            i,
            string(R.string.learn_achievement_dialog_message),
            array(R.array.learn_achievement_dialog_questions),
            array(R.array.learn_achievement_dialog_answers)
        )
        HERO_NAV -> LearnMessage(
            i,
            string(R.string.learn_hero_nav_message),
            array(R.array.learn_hero_nav_questions),
            array(R.array.learn_hero_nav_answers)
        )
        HERO_MAKER_EDIT -> LearnMessage(
            i,
            string(R.string.learn_hero_maker_edit_message),
            array(R.array.learn_hero_maker_edit_questions),
            array(R.array.learn_hero_maker_edit_answers)
        )
        HERO_MAKER_CREATE -> LearnMessage(
            i,
            string(R.string.learn_hero_maker_create_message),
            array(R.array.learn_hero_maker_create_questions),
            array(R.array.learn_hero_maker_create_answers)
        )
        INVENTORY_NAV -> LearnMessage(
            i,
            string(R.string.learn_inventory_nav_message),
            array(R.array.learn_inventory_nav_questions),
            array(R.array.learn_inventory_nav_answers)
        )
        PRODUCT_DIALOG -> LearnMessage(
            i,
            string(R.string.learn_product_dialog_message),
            array(R.array.learn_product_dialog_questions),
            array(R.array.learn_product_dialog_answers)
        )
        CREATOR_NAV -> LearnMessage(
            i,
            string(R.string.learn_creator_nav_message),
            array(R.array.learn_creator_nav_questions),
            array(R.array.learn_creator_nav_answers)
        )
        UNLOCK_ATTRIBUTE_GRAPH_FEATURE -> LearnMessage(
            i,
            string(R.string.learn_unlock_attribute_graph_message),
            array(R.array.learn_unlock_attribute_graph_questions),
            array(R.array.learn_unlock_attribute_graph_answers)
        )
        UNLOCK_NOTIFICATION_FEATURE -> LearnMessage(
            i,
            string(R.string.learn_unlock_notifications_message),
            array(R.array.learn_unlock_notifications_questions),
            array(R.array.learn_unlock_notifications_answers)
        )
        UNLOCK_TASK_LIST_WIDGET_FEATURE -> LearnMessage(
            i,
            string(R.string.learn_unlock_task_list_widget_message),
            array(R.array.learn_unlock_task_list_widget_questions),
            array(R.array.learn_unlock_task_list_widget_answers)
        )
        UNLOCK_REPEATABLE_TASKS_FEATURE -> LearnMessage(
            i,
            string(R.string.learn_unlock_copy_tasks_message),
            array(R.array.learn_unlock_copy_tasks_questions),
            array(R.array.learn_unlock_copy_tasks_answers)
        )
        AFTER_CREATE_HERO -> LearnMessage(
            i,
            string(R.string.learn_after_create_hero_message),
            array(R.array.learn_after_create_hero_questions),
            array(R.array.learn_after_create_hero_answers)
        )
        CHALLENGES_FRAGMENT -> LearnMessage(
            i,
            string(R.string.learn_challenges_fragment_message),
            array(R.array.learn_challenges_fragment_questions),
            array(R.array.learn_challenges_fragment_answers)
        )
        CHALLENGE_MAKER -> LearnMessage(
            i,
            string(R.string.learn_challenge_maker_message),
            array(R.array.learn_challenge_maker_questions),
            array(R.array.learn_challenge_maker_answers)
        )
        else -> LearnMessage(i, "...", arrayOf(), arrayOf())
    }

    private fun string(id: Int) = MainActivity.ui!!.getString(id)

    private fun array(id: Int) = MainActivity.ui!!.getStringArray(id)

}