package kolmachikhin.alexander.epictodolist.appwidget.tasks

import android.content.Intent
import android.widget.RemoteViewsService

class TaskListWidgetRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent) = TaskListWidgetRemoteViewsFactory(applicationContext)

}