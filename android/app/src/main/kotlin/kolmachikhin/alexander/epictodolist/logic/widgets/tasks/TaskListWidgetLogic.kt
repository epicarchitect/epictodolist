package kolmachikhin.alexander.epictodolist.logic.widgets.tasks

import kolmachikhin.alexander.epictodolist.appwidget.tasks.TaskListWidget
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.products.ProductsLogic
import kolmachikhin.alexander.epictodolist.logic.settings.SettingsLogic
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic

class TaskListWidgetLogic(core: Core) : Logic(core) {

    override fun postInit() {
        core.currentTasksLogic
            .observer
            .addListener(CurrentTasksLogic.CREATE) { update() }
            .addListener(CurrentTasksLogic.UPDATE) { update() }
            .addListener(CurrentTasksLogic.DELETE) { update() }

        core.productsLogic
            .observer
            .addListener(ProductsLogic.UNLOCK) { update() }

        core.settingsLogic
            .observer
            .addListener(SettingsLogic.UPDATE) { update() }

        ready()
    }

    override fun attachRef() {}

    fun update() {
        TaskListWidget.update(context)
    }
}