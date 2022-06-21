package kolmachikhin.alexander.epictodolist.logic.tasks.repeatable

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTasksRepository
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster

class RepeatableTasksLogic(core: Core) : Logic(core) {

    private val repository by lazy { RepeatableTasksRepository.getInstance(context) }
    private var tasks = ArrayList<RepeatableTaskModel>()
    val observer = Observer<RepeatableTaskModel>()

    var repeatableTasks: ArrayList<RepeatableTaskModel>
        get() = ArrayList(tasks)
        set(list) {
            tasks = list
            sort(tasks, true)
            ready()
        }

    override fun postInit() {}

    override fun attachRef() {
        repository.liveList.observeForever { list ->
            repeatableTasks = list
        }
    }

    fun findById(id: Int) = findModel(tasks, id) ?: RepeatableTaskModel()

    fun create(task: RepeatableTaskModel): RepeatableTaskModel {
        task.id = core.statusLogic.nextId
        installRepeatableTask(task)
        tasks.add(0, task)
        repository.save(task)
        observer.notify(CREATE, task)
        return task
    }

    fun update(task: RepeatableTaskModel): RepeatableTaskModel {
        for (i in tasks.indices) {
            if (task.id == tasks[i].id) {
                tasks[i] = task
                break
            }
        }

        installRepeatableTask(task)
        repository.save(task)
        observer.notify(UPDATE, task)
        return task
    }

    fun delete(task: RepeatableTaskModel): RepeatableTaskModel {
        cancelRepeatableTask(task)
        for (i in tasks.indices) {
            if (task.id == tasks[i].id) {
                tasks.removeAt(i)
                break
            }
        }

        repository.delete(task)
        observer.notify(DELETE, task)
        return task
    }

    fun getNextDateCreate(task: RepeatableTaskModel): Long {
        val timeMaster = TimeMaster()
        val today = timeMaster.todayOfWeek
        var nextDay = today + 1
        if (nextDay == 7) nextDay = 0
        val startDay: Int
        var dateCreate = TimeMaster.currentDayWithoutHours + task.copyTime
        if (task.copyDays!![today] && dateCreate > System.currentTimeMillis() + 1000) {
            startDay = today
        } else {
            startDay = nextDay
            dateCreate += TimeMaster.countMillisInDay
        }
        for (day in startDay until 7 + startDay) {
            if (task.copyDays!![day % 7]) {
                return dateCreate
            }
            dateCreate += TimeMaster.countMillisInDay
        }
        return -1
    }

    fun installRepeatableTask(task: RepeatableTaskModel) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dateCreate = getNextDateCreate(task)

        if (dateCreate != -1L) {
            manager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dateCreate,
                PendingIntent.getBroadcast(
                    context,
                    task.id,
                    Intent(context, RepeatableTasksScheduler::class.java)
                        .putExtra(RepeatableTasksScheduler.TASK_ID, task.id),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    private fun cancelRepeatableTask(task: RepeatableTaskModel) {
        PendingIntent.getBroadcast(
            context,
            task.id,
            Intent(context, RepeatableTasksScheduler::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )?.let {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    companion object {
        const val CREATE = 0
        const val UPDATE = 1
        const val DELETE = 2
    }
}