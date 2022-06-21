package kolmachikhin.alexander.epictodolist.util.time

import android.annotation.SuppressLint
import android.text.format.DateFormat
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class TimeMaster {

    val calendar = Calendar.getInstance()
    val timeDateText: String
        get() = "$timeText, $dateText"

    @get:SuppressLint("SimpleDateFormat")
    val timeText: String
        get() {
            var is24 = true
            if (MainActivity.activity != null) {
                is24 = DateFormat.is24HourFormat(MainActivity.activity)
            }
            val dateFormat = SimpleDateFormat(if (is24) "HH:mm" else "hh:mm a")
            return dateFormat.format(calendar.time).lowercase(Locale.getDefault())
        }

    @get:SuppressLint("SimpleDateFormat")
    val dateText: String
        get() {
            val dateFormat = SimpleDateFormat("dd MMMM yyyy")
            return dateFormat.format(calendar.time)
        }

    val todayOfWeek: Int
        get() {
            val day = calendar[Calendar.DAY_OF_WEEK]
            if (day == Calendar.MONDAY) return 0
            if (day == Calendar.TUESDAY) return 1
            if (day == Calendar.WEDNESDAY) return 2
            if (day == Calendar.THURSDAY) return 3
            if (day == Calendar.FRIDAY) return 4
            if (day == Calendar.SATURDAY) return 5
            return if (day == Calendar.SUNDAY) 6 else 7
        }

    var timeInMinute: Int
        get() = getMillisInMinute(calendar.timeInMillis)
        set(minute) {
            calendar.timeInMillis = getMinuteInMillis(minute)
        }

    var timeInMillis: Long
        get() = calendar.timeInMillis
        set(millis) {
            calendar.timeInMillis = millis
        }

    /**Calendar getters and setters  */
    var millisecond: Int
        get() = calendar[Calendar.MILLISECOND]
        set(mills) {
            calendar[Calendar.MILLISECOND] = mills
        }

    var second: Int
        get() = calendar[Calendar.SECOND]
        set(second) {
            calendar[Calendar.SECOND] = second
        }

    var minute: Int
        get() = calendar[Calendar.MINUTE]
        set(minute) {
            calendar[Calendar.MINUTE] = minute
        }

    var hour: Int
        get() = calendar[Calendar.HOUR_OF_DAY]
        set(hour) {
            calendar[Calendar.HOUR_OF_DAY] = hour
        }

    var day: Int
        get() = calendar[Calendar.DAY_OF_MONTH]
        set(day) {
            calendar[Calendar.DAY_OF_MONTH] = day
        }

    var month: Int
        get() = calendar[Calendar.MONTH]
        set(month) {
            calendar[Calendar.MONTH] = month
        }

    var year: Int
        get() = calendar[Calendar.YEAR]
        set(year) {
            calendar[Calendar.YEAR] = year
        }

    val daysInMonth: Int
        get() = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    companion object {
        val countMillisInDay: Long
            get() = 1000 * 60 * 60 * 24

        val currentTimeInSeconds: Int
            get() = (System.currentTimeMillis() / 1000).toInt()

        fun getMillisInMinute(millis: Long): Int {
            return (millis / 1000 / 60).toInt()
        }

        fun getMinuteInMillis(minutes: Int): Long {
            return minutes.toLong() * 1000 * 60
        }

        val currentTimeInMillis: Long
            get() = System.currentTimeMillis()

        val currentTimeInMinute: Int
            get() = getMillisInMinute(System.currentTimeMillis())

        val currentDayWithoutHours: Long
            get() {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.MILLISECOND] = 0
                return calendar.timeInMillis
            }

        
        fun calculateDaysInMonth(month: Int, year: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar[Calendar.MONTH] = month
            calendar[Calendar.YEAR] = year
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
    }
}