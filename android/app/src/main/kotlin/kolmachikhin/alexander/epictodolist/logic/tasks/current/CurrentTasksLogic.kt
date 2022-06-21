package kolmachikhin.alexander.epictodolist.logic.tasks.current

import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.database.tasks.completed.CompletedTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTasksRepository
import kolmachikhin.alexander.epictodolist.database.tasks.repeatable.RepeatableTaskModel
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer

class CurrentTasksLogic(core: Core) : Logic(core) {

    private val repository by lazy { CurrentTasksRepository.getInstance(context) }
    private var tasks = ArrayList<CurrentTaskModel>()
    val observer = Observer<CurrentTaskModel>()

    var currentTasks: ArrayList<CurrentTaskModel>
        get() = ArrayList(tasks)
        private set(list) {
            tasks = list
            sort(tasks, true)
            ready()
        }

    override fun postInit() {}

    override fun attachRef() {
        repository.liveList.observeForever { list ->
            currentTasks = list
        }
    }

    fun complete(task: CurrentTaskModel): CurrentTaskModel {
        observer.notify(COMPLETE, task)
        return delete(task)
    }

    fun createChallengeTasks(challenge: ChallengeModel) {
        for (task in challenge.tasks!!) {
            var needCreate = true
            for (t in tasks) {
                if (t.id == task.currentTask.id) {
                    needCreate = false
                    break
                }
            }

            if (needCreate) {
                create(task.currentTask, false)
            }
        }
    }

    fun create(task: CompletedTaskModel) = create(CurrentTaskModel(task), true)

    fun create(task: RepeatableTaskModel) = create(CurrentTaskModel(task), true)


    fun create(task: CurrentTaskModel, newId: Boolean = true): CurrentTaskModel {
        if (newId) task.id = core.statusLogic.nextId
        tasks.add(0, task)
        repository.save(task)
        observer.notify(CREATE, task)
        return task
    }

    fun update(task: CurrentTaskModel): CurrentTaskModel {
        for (i in tasks.indices) {
            if (task.id == tasks[i].id) {
                tasks[i] = task
                break
            }
        }

        repository.save(task)
        observer.notify(UPDATE, task)
        return task
    }

    fun delete(task: CurrentTaskModel): CurrentTaskModel {
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

    companion object {
        const val CREATE = 0
        const val UPDATE = 1
        const val DELETE = 2
        const val COMPLETE = 3
    }
}