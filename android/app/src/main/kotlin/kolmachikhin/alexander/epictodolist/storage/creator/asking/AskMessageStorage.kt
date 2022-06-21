package kolmachikhin.alexander.epictodolist.storage.creator.asking

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context

object AskMessageStorage {

    
    val list: ArrayList<AskMessage>
        get() {
            val l = ArrayList<AskMessage>()
            for (i in 0 until size()) l.add(AskMessageStorage[i])
            return l
        }

    fun size() = 12

    operator fun get(i: Int) = when (i) {
        0 -> AskMessage(i, s(R.string.question_about_create_task), s(R.string.answer_about_create_task))
        1 -> AskMessage(i, s(R.string.question_about_modify_tasks), s(R.string.answer_about_modify_tasks))
        2 -> AskMessage(i, s(R.string.question_about_modify_skills), s(R.string.answer_about_modify_skills))
        3 -> AskMessage(i, s(R.string.question_about_need_products), s(R.string.answer_about_need_products))
        4 -> AskMessage(i, s(R.string.question_about_getting_features), s(R.string.answer_about_getting_features))
        5 -> AskMessage(i, s(R.string.question_about_need_coins), s(R.string.answer_about_need_coins))
        6 -> AskMessage(i, s(R.string.questions_about_need_crystals), s(R.string.answer_about_need_crystals))
        7 -> AskMessage(i, s(R.string.question_about_where_setting), s(R.string.answer_about_where_setting))
        8 -> AskMessage(i, s(R.string.question_about_earn_coins), s(R.string.answer_about_earn_coins))
        9 -> AskMessage(i, s(R.string.question_about_earn_crystals), s(R.string.answer_about_earn_crystals))
        10 -> AskMessage(i, s(R.string.question_about_improve_skills), s(R.string.answer_about_improve_skills))
        11 -> AskMessage(i, s(R.string.question_about_text_size), s(R.string.answer_about_text_size))
        else -> AskMessage(i, "...", "...")
    }

    private fun s(id: Int): String {
        val c = context
        return c?.getString(id) ?: "Something went wrong :("
    }
}