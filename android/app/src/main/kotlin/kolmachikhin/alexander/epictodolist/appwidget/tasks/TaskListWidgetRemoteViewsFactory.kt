package kolmachikhin.alexander.epictodolist.appwidget.tasks

import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import androidx.core.content.ContextCompat
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.appwidget.tasks.TaskListWidget.Companion.update
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.storage.products.ThemeStorage
import kolmachikhin.alexander.epictodolist.ui.drag.ModelOrderManager
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.masters.ThemeMaster

class TaskListWidgetRemoteViewsFactory(private val context: Context) : RemoteViewsFactory {
    private var list = ArrayList<CurrentTaskModel>()
    private val orderManager = ModelOrderManager<CurrentTaskModel>(context, "CurrentTasksFragment")

    override fun onCreate() {
        Log.d("test", "onCreate")
    }

    override fun onDataSetChanged() {
        Log.d("test", "onDataSetChanged")

        if (MainActivity.core == null) {
            update(context)
        } else {
            list = orderManager.sorted(MainActivity.core!!.currentTasksLogic.currentTasks)
        }
    }

    override fun onDestroy() = Unit

    override fun getCount() = list.size

    override fun getViewAt(position: Int) = RemoteViews(
        context.packageName,
        R.layout.task_list_widget_item
    ).apply {
        try {
            val task = list[position]
            setTextViewText(R.id.content, task.content)
            when (ThemeMaster.getTheme(context)) {
                ThemeStorage.IRON_THEME -> {
                    setInt(R.id.content, "setTextColor", ContextCompat.getColor(context, R.color.colorPrimaryDarkIronTheme))
                }
                ThemeStorage.PEACH_THEME -> {
                    setInt(R.id.content, "setTextColor", ContextCompat.getColor(context, R.color.colorPrimaryDarkPeachTheme))
                }
                ThemeStorage.WOODY_THEME -> {
                    setInt(R.id.content, "setTextColor", ContextCompat.getColor(context, R.color.colorPrimaryDarkWoodyTheme))
                }
                ThemeStorage.NIGHT_THEME -> {
                    setInt(R.id.content, "setTextColor", ContextCompat.getColor(context, R.color.colorPrimaryDarkNightTheme))
                }
                ThemeStorage.PINK_THEME -> {
                    setInt(R.id.content, "setTextColor", ContextCompat.getColor(context, R.color.colorPrimaryDarkPinkTheme))
                }
                else -> {
                    setInt(R.id.content, "setTextColor", ContextCompat.getColor(context, R.color.colorPrimaryDarkDefaultTheme))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = position.toLong()

    override fun hasStableIds() = true

}