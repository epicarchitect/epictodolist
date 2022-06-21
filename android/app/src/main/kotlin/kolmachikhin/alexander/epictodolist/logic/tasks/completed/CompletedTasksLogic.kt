package kolmachikhin.alexander.epictodolist.logic.tasks.completed

import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTasksRepository
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic
import kolmachikhin.alexander.epictodolist.ui.graphs.progress.Interval
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster

class CompletedTasksLogic(core: Core) : Logic(core) {

    private val repository by lazy { CompletedTasksRepository.getInstance(context) }
    private var tasks = ArrayList<CompletedTaskModel>()
    val observer = Observer<CompletedTaskModel>()

    val notDeletedCompletedTasks: ArrayList<CompletedTaskModel>
        get() {
            val notDeletedTasks = ArrayList<CompletedTaskModel>()
            for (task in tasks) {
                if (!task.isDeleted) {
                    notDeletedTasks.add(task)
                }
            }
            return notDeletedTasks
        }

    var completedTasks: ArrayList<CompletedTaskModel>
        get() = ArrayList(tasks)
        set(list) {
            tasks = list
            sort(tasks, true)
            ready()
        }

    val completedTasksToday: ArrayList<CompletedTaskModel>
        get() {
            val list = ArrayList<CompletedTaskModel>()
            val startDay = TimeMaster.currentDayWithoutHours
            val endDay = startDay + TimeMaster.countMillisInDay
            for (task in tasks) {
                if (task.completionDate in (startDay + 1) until endDay) {
                    list.add(task)
                }
            }
            return list
        }

    override fun postInit() {
        core.currentTasksLogic
            .observer
            .addListener(CurrentTasksLogic.COMPLETE) { task ->
                create(task)
            }
    }

    override fun attachRef() {
        repository.liveList.observeForever { list ->
            completedTasks = list
        }
    }

    fun getCountCompletedTasksBySkillId(id: Int): Int {
        var count = 0
        for (i in tasks.indices) {
            if (tasks[i].skillId == id) {
                count++
            }
        }
        return count
    }

    fun create(task: CurrentTaskModel?): CompletedTaskModel {
        return create(CompletedTaskModel(task!!))
    }

    fun create(task: CompletedTaskModel): CompletedTaskModel {
        tasks.add(0, task)
        repository.save(task)
        observer.notify(CREATE, task)
        return task
    }

    fun delete(task: CompletedTaskModel): CompletedTaskModel {
        task.isDeleted = true
        repository.save(task)
        observer.notify(DELETE, task)
        return task
    }

    fun getCountCompletedTasksByAttribute(attr: Int): Int {
        var count = 0
        for (task in tasks) {
            if (core.skillsLogic.findById(task.skillId).attribute == attr) {
                count++
            }
        }
        return count
    }

    fun getCountCompletedTasksByDifficulty(d: Int): Int {
        var count = 0
        for (task in tasks) {
            if (task.difficulty == d) {
                count++
            }
        }
        return count
    }

    fun getEarnedProgressOnInterval(interval: Interval): ArrayList<Int> {
        val list = ArrayList<Int>()
        val day1 = interval.startDate.day
        val day2 = interval.endDate.day
        val month1 = interval.startDate.month
        val month2 = interval.endDate.month
        val year1 = interval.startDate.year
        val year2 = interval.endDate.year
        if (year1 == year2) {
            if (month1 == month2) {

                //берем дни между day1 и day2 включая day2
                for (d in day1..day2) {
                    list.add(getEarnedProgressByDay(d, month1, year1))
                }
            } else {

                //берем дни с day1 включая последний день в этом месяце этого года
                for (d in day1..TimeMaster.calculateDaysInMonth(month1, year1)) {
                    list.add(getEarnedProgressByDay(d, month1, year1))
                }

                //берем все дни между месяцами этого года
                for (m in 0 until month2 - month1 - 1) {
                    for (d in 1..TimeMaster.calculateDaysInMonth(m + month1, year1)) {
                        list.add(getEarnedProgressByDay(d, m + month1, year1))
                    }
                }

                //берем дни начиная с первого и заканчивая day2 в конечном месяце
                for (d in 1..day2) {
                    list.add(getEarnedProgressByDay(d, month2, year1))
                }
            }
        } else {

            //берем дни с day1 включая последний день в этом месяце этого года
            for (d in day1..TimeMaster.calculateDaysInMonth(month1, year1)) {
                list.add(getEarnedProgressByDay(d, month1, year1))
            }

            //берем все дни между месяцами первого года
            for (m in month1 + 1..11) {
                for (d in 1..TimeMaster.calculateDaysInMonth(m, year1)) {
                    list.add(getEarnedProgressByDay(d, m, year1))
                }
            }

            //берем все дни между этими годами
            for (y in 1 until year2 - year1) {
                //берем все дни между месяцами обоих центральных годов
                for (m in 0..11) {
                    for (d in 1..TimeMaster.calculateDaysInMonth(m, y + year1)) {
                        list.add(getEarnedProgressByDay(d, m, y + year1))
                    }
                }
            }

            //берем все дни между месяцами второго года
            for (m in 0 until month2) {
                for (d in 1..TimeMaster.calculateDaysInMonth(m, year2)) {
                    list.add(getEarnedProgressByDay(d, m, year2))
                }
            }

            //берем дни начиная с первого и заканчивая day2 в конечном месяце
            for (d in 1..day2) {
                list.add(getEarnedProgressByDay(d, month2, year2))
            }
        }
        return list
    }

    private fun getEarnedProgressByDay(day: Int, month: Int, year: Int): Int {
        var progress = 0
        for (task in tasks) {
            if (task.completionYear() == year && task.completionMonth() == month && task.completionDay() == day) {
                progress += task.reward().progress
            }
        }
        return progress
    }

    companion object {
        const val CREATE = 0
        const val DELETE = 1
    }
}