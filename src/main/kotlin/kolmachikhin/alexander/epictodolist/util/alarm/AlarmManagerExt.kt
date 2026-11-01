package kolmachikhin.alexander.epictodolist.util.alarm

import android.app.AlarmManager
import android.app.PendingIntent

fun AlarmManager.setExactOrInexact(
    type: Int,
    triggerAtMillis: Long,
    operation: PendingIntent
) {
    if (!canScheduleExactAlarms()) {
        setAndAllowWhileIdle(type, triggerAtMillis, operation)
    } else {
        setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
    }
}
