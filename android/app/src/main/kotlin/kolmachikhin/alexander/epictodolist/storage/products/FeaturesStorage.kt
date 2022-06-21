package kolmachikhin.alexander.epictodolist.storage.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.context
import kolmachikhin.alexander.epictodolist.logic.products.ProductType

object FeaturesStorage {

    const val STEP = 100
    const val TYPE = ProductType.FEATURE
    const val NOTIFICATIONS_ID = 0 + STEP
    const val REPEATABLE_TASKS_ID = 1 + STEP
    const val TASK_LIST_WIDGET_ID = 2 + STEP
    const val ATTRIBUTE_GRAPH_ID = 3 + STEP
    const val CHALLENGES_ID = 4 + STEP

    
    val list: ArrayList<ProductModel>
        get() {
            val l = ArrayList<ProductModel>()
            l.add(FeaturesStorage[NOTIFICATIONS_ID])
            l.add(FeaturesStorage[REPEATABLE_TASKS_ID])
            l.add(FeaturesStorage[TASK_LIST_WIDGET_ID])
            l.add(FeaturesStorage[ATTRIBUTE_GRAPH_ID])
            l.add(FeaturesStorage[CHALLENGES_ID])
            return l
        }

    
    operator fun get(i: Int) = when (i) {
        NOTIFICATIONS_ID -> ProductModel(
            i,
            string(R.string.notifications),
            50,
            5,
            TYPE,
            R.drawable.ic_notification,
            0,
            2
        )
        REPEATABLE_TASKS_ID -> ProductModel(
            i,
            string(R.string.copy_tasks_feature),
            100,
            10,
            TYPE,
            R.drawable.ic_task_scheduler,
            0,
            3
        )
        TASK_LIST_WIDGET_ID -> ProductModel(
            i,
            string(R.string.task_list_widget),
            250,
            5,
            TYPE,
            R.drawable.ic_list,
            0,
            3
        )
        ATTRIBUTE_GRAPH_ID -> ProductModel(
            i,
            string(R.string.attribute_graph),
            150,
            10,
            TYPE,
            R.drawable.ic_pie_graph,
            0,
            4
        )
        CHALLENGES_ID -> ProductModel(
            i,
            string(R.string.challenges),
            250,
            15,
            TYPE,
            R.drawable.icon_22,
            0,
            4
        )
        else -> ProductModel()
    }

    fun string(id: Int) = context?.getString(id) ?: "Something went wrong :("

}