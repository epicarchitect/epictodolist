package kolmachikhin.alexander.epictodolist.appwidget.tasks

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.storage.products.ThemeStorage
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.masters.ThemeMaster

class TaskListWidget : AppWidgetProvider() {

    private lateinit var context: Context
    private lateinit var core: Core

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, manager, appWidgetIds)
        Log.d("test", "onUpdate")
        this.context = context
        Core.with(context) { core ->
            MainActivity.core = core
            this.core = core
            updateAllWidgets(manager, appWidgetIds)
            manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list)
        }
    }

    private fun updateWidget(appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val rv = RemoteViews(context.packageName, R.layout.task_list_widget)
        setList(rv, appWidgetId)
        appWidgetManager.updateAppWidget(appWidgetId, rv)
        Log.d("test", "updateWidget")
    }

    private fun setList(rv: RemoteViews, appWidgetId: Int) {
        if (core.productsLogic.isTaskListWidgetUnlocked) {
            rv.setViewVisibility(R.id.button_unlock, View.GONE)
            if (core.currentTasksLogic.currentTasks.size == 0) {
                rv.setTextViewText(R.id.status, context.getString(R.string.there_are_no_tasks))
                rv.setViewVisibility(R.id.list, View.GONE)
                rv.setViewVisibility(R.id.status, View.VISIBLE)
                Log.d("test", "core.currentTasksLogic.getCurrentTasks().size() == 0")
            } else {
                val adapter = Intent(context, TaskListWidgetRemoteViewsService::class.java)
                adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                rv.setRemoteAdapter(R.id.list, adapter)
                rv.setViewVisibility(R.id.status, View.GONE)
                rv.setViewVisibility(R.id.list, View.VISIBLE)
                Log.d("test", "core.currentTasksLogic.getCurrentTasks().size() != 0")
            }
        } else {
            rv.setViewVisibility(R.id.list, View.GONE)
            rv.setTextViewText(R.id.status, context.getString(R.string.widget_not_unlocked))
            rv.setViewVisibility(R.id.status, View.VISIBLE)
            rv.setViewVisibility(R.id.button_unlock, View.VISIBLE)
            val intent = Intent(context, MainActivity::class.java)
            intent.action = UNLOCK
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            rv.setOnClickPendingIntent(R.id.button_unlock, pendingIntent)
        }
        setThemeColors(rv)
    }

    private fun setThemeColors(rv: RemoteViews) {
        when (ThemeMaster.getTheme(context)) {
            ThemeStorage.IRON_THEME -> {
                setTextColorStatus(rv, R.color.colorPrimaryDarkIronTheme)
                setBackground(rv, R.color.colorPrimaryIronTheme)
                setBorder(rv, R.color.colorPrimaryDarkIronTheme)
                setColorsButtonUnlock(rv, R.color.colorPrimaryIronTheme, R.color.colorPrimaryDarkIronTheme)
            }
            ThemeStorage.PEACH_THEME -> {
                setTextColorStatus(rv, R.color.colorPrimaryDarkPeachTheme)
                setBackground(rv, R.color.colorPrimaryPeachTheme)
                setBorder(rv, R.color.colorPrimaryDarkPeachTheme)
                setColorsButtonUnlock(rv, R.color.colorPrimaryPeachTheme, R.color.colorPrimaryDarkPeachTheme)
            }
            ThemeStorage.WOODY_THEME -> {
                setTextColorStatus(rv, R.color.colorPrimaryDarkWoodyTheme)
                setBackground(rv, R.color.colorPrimaryWoodyTheme)
                setBorder(rv, R.color.colorPrimaryDarkWoodyTheme)
                setColorsButtonUnlock(rv, R.color.colorPrimaryWoodyTheme, R.color.colorPrimaryDarkWoodyTheme)
            }
            ThemeStorage.NIGHT_THEME -> {
                setTextColorStatus(rv, R.color.colorPrimaryDarkNightTheme)
                setBackground(rv, R.color.colorPrimaryNightTheme)
                setBorder(rv, R.color.colorPrimaryDarkNightTheme)
                setColorsButtonUnlock(rv, R.color.colorPrimaryNightTheme, R.color.colorPrimaryDarkNightTheme)
            }
            ThemeStorage.PINK_THEME -> {
                setTextColorStatus(rv, R.color.colorPrimaryDarkPinkTheme)
                setBackground(rv, R.color.colorPrimaryPinkTheme)
                setBorder(rv, R.color.colorPrimaryDarkPinkTheme)
                setColorsButtonUnlock(rv, R.color.colorPrimaryPinkTheme, R.color.colorPrimaryDarkPinkTheme)
            }
            else -> {
                setTextColorStatus(rv, R.color.colorPrimaryDarkDefaultTheme)
                setBackground(rv, R.color.colorPrimaryDefaultTheme)
                setBorder(rv, R.color.colorPrimaryDarkDefaultTheme)
                setColorsButtonUnlock(rv, R.color.colorPrimaryDefaultTheme, R.color.colorPrimaryDarkDefaultTheme)
            }
        }
    }

    private fun setTextColorStatus(rv: RemoteViews, color: Int) {
        rv.setInt(R.id.status, "setTextColor", ContextCompat.getColor(context, color))
    }

    private fun setBackground(rv: RemoteViews, color: Int) {
        rv.setInt(R.id.main_layout, "setBackgroundColor", ContextCompat.getColor(context, color))
    }

    private fun setColorsButtonUnlock(rv: RemoteViews, color: Int, bg: Int) {
        rv.setInt(R.id.button_unlock, "setTextColor", ContextCompat.getColor(context, color))
        rv.setInt(R.id.button_unlock, "setBackgroundColor", ContextCompat.getColor(context, bg))
    }

    private fun setBorder(rv: RemoteViews, color: Int) {
        rv.setInt(R.id.border_layout, "setBackgroundColor", ContextCompat.getColor(context, color))
    }

    private fun updateAllWidgets(appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (i in appWidgetIds) updateWidget(appWidgetManager, i)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        this.context = context
        Core.with(context) { core ->
            MainActivity.core = core
            this.core = core
            val manager = AppWidgetManager.getInstance(context)
            val widget = ComponentName(context, TaskListWidget::class.java)
            updateAllWidgets(manager, manager.getAppWidgetIds(widget))
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(widget), R.id.list)
        }
    }

    companion object {
        const val UNLOCK = "unlock_task_list_widget"

        
        fun update(context: Context) {
            val intent = Intent()
            val widget = ComponentName(context, TaskListWidget::class.java)
            intent.component = widget
            context.sendBroadcast(intent)
        }
    }
}