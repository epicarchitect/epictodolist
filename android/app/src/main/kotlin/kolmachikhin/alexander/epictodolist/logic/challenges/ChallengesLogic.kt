package kolmachikhin.alexander.epictodolist.logic.challenges

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.Model.Companion.isVoid
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengeModel
import kolmachikhin.alexander.epictodolist.database.challenges.ChallengesRepository
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationModel
import kolmachikhin.alexander.epictodolist.database.tasks.current.CurrentTaskModel
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import kolmachikhin.alexander.epictodolist.logic.tasks.current.CurrentTasksLogic
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster

class ChallengesLogic(core: Core) : Logic(core) {

    private var challenges = ArrayList<ChallengeModel>()
    private val repository by lazy { ChallengesRepository.getInstance(context) }
    val observer = Observer<ChallengeModel>()

    fun getChallenges() = ArrayList(challenges)

    val activeChallenges: ArrayList<ChallengeModel>
        get() {
            val list = ArrayList<ChallengeModel>()
            for (challenge in challenges) {
                if (challenge.isActive) list.add(challenge)
            }
            return list
        }

    private fun setChallenges(list: ArrayList<ChallengeModel>) {
        challenges = list
        sort(challenges, true)
        ready()
    }

    private fun checkActiveChallengesWhenCompletedTask(task: CurrentTaskModel) {
        for (challenge in activeChallenges) {
            var isNeedUpdate = false
            for (challengeTask in challenge.tasks!!) {
                if (challengeTask.id == task.id) {
                    challengeTask.isCompleted = true
                    isNeedUpdate = true
                    break
                }
            }

            if (isNeedUpdate) {
                update(challenge)
            }
        }
    }

    override fun postInit() {
        core.currentTasksLogic
            .observer
            .addListener(CurrentTasksLogic.COMPLETE) { task ->
                checkActiveChallengesWhenCompletedTask(task)
            }
    }

    fun findById(id: Int) = findModel(challenges, id) ?: ChallengeModel()

    override fun attachRef() {
        repository.liveList.observeForever { list ->
            setChallenges(list)
        }
    }

    fun checkChallengeDay(challenge: ChallengeModel) {
        var isAllCompleted = true

        for (task in challenge.tasks!!) {
            if (!task.isCompleted) {
                isAllCompleted = false
                break
            }
        }

        if (isAllCompleted) {
            challenge.currentDay++
            if (challenge.currentDay == challenge.needDays) {
                complete(challenge)
            } else {
                completeDay(challenge)
            }
        } else {
            fail(challenge)
        }

        observer.notify(CHALLENGE_CHECK_DAY, challenge)
    }

    fun installChallengeChecker(challenge: ChallengeModel) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextCheckTime = TimeMaster.currentDayWithoutHours + TimeMaster.countMillisInDay
        val intent = Intent(context, ChallengeChecker::class.java)
        intent.putExtra(ChallengeChecker.CHALLENGE_ID, challenge.id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            challenge.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextCheckTime, pendingIntent)
        core.currentTasksLogic.createChallengeTasks(challenge)
    }

    fun cancelChallengeChecker(challenge: ChallengeModel) {
        if (isVoid(challenge)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ChallengeChecker::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            challenge.id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun create(challenge: ChallengeModel): ChallengeModel {
        challenge.id = core.statusLogic.nextId
        challenge.isActive = true
        for (task in challenge.tasks!!) {
            task.id = core.statusLogic.nextId
            task.currentTask.id = task.id
        }
        challenges.add(challenge)
        repository.save(challenge)
        installChallengeChecker(challenge)
        return challenge
    }

    fun delete(challenge: ChallengeModel) {
        cancelChallengeChecker(challenge)
        challenges.remove(challenge)
        repository.delete(challenge)
    }

    fun completeDay(challenge: ChallengeModel) {
        for (task in challenge.tasks!!) {
            task.isCompleted = false
        }
        installChallengeChecker(challenge)
        observer.notify(CHALLENGE_DAY_COMPLETED, challenge)
        update(challenge)
        core.notificationsLogic.sendNotification(
            NotificationModel(
                challenge.id,
                challenge.title!!,
                getString(R.string.the_day_is_completed_successfully),
                0
            )
        )
    }

    fun complete(challenge: ChallengeModel) {
        for (task in challenge.tasks!!) {
            task.isCompleted = false
        }
        challenge.isActive = false
        challenge.countCompletes++
        observer.notify(CHALLENGE_COMPLETED, challenge)
        update(challenge)
        core.notificationsLogic.sendNotification(
            NotificationModel(
                challenge.id,
                challenge.title!!,
                getString(R.string.challenge_completed_successfully),
                0
            )
        )
    }

    fun fail(challenge: ChallengeModel) {
        for (task in challenge.tasks!!) {
            task.isCompleted = false
        }
        challenge.isActive = false
        challenge.countFails++
        observer.notify(CHALLENGE_FAILED, challenge)
        update(challenge)
        core.notificationsLogic.sendNotification(
            NotificationModel(
                challenge.id,
                challenge.title!!,
                getString(R.string.the_challenge_is_a_failure),
                0
            )
        )
        cancelChallengeChecker(challenge)
    }

    fun update(challenge: ChallengeModel): ChallengeModel {
        for (i in challenges.indices) {
            if (challenge.id == challenges[i].id) {
                challenges[i] = challenge
                break
            }
        }

        repository.save(challenge)
        return challenge
    }

    fun restart(challenge: ChallengeModel) {
        for (task in challenge.tasks!!) {
            task.isCompleted = false
        }
        challenge.isActive = true
        challenge.currentDay = 0
        installChallengeChecker(challenge)
        update(challenge)
    }

    companion object {
        const val CHALLENGE_CHECK_DAY = 0
        const val CHALLENGE_DAY_COMPLETED = 1
        const val CHALLENGE_COMPLETED = 1
        const val CHALLENGE_FAILED = 2
    }
}