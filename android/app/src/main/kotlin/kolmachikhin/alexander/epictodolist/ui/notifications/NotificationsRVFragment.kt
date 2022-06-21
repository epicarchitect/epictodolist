package kolmachikhin.alexander.epictodolist.ui.notifications

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.notifications.NotificationModel
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.datetime.DatePickerDialog
import kolmachikhin.alexander.epictodolist.ui.datetime.TimePickerDialog
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVFragment
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class NotificationsRVFragment(
    container: ViewGroup
) : RVFragment<NotificationModel, NotificationsRVFragment.ViewHolder>(
    R.layout.notification_item,
    RecyclerView.VERTICAL,
    container
) {

    init {
        onEmptyMessage = MainActivity.ui!!.getString(R.string.there_are_no_notifications)
    }

    override fun newViewHolder(v: View) = ViewHolder(v)

    inner class ViewHolder(view: View) : RVViewHolder<NotificationModel>(view) {
        private lateinit var notification: NotificationModel
        private var tvTime: TextView = find(R.id.tv_time)
        private var buttonDelete: ImageView = find(R.id.button_delete)
        private val timeMaster = TimeMaster()

        init {
            set(view) {
                openTimeAndDateDialogs()
                notification.time = timeMaster.timeInMillis
            }

            set(buttonDelete) {
                deleteItem(adapterPosition)
            }
        }

        override fun setData(m: NotificationModel) {
            this.notification = m
            timeMaster.timeInMillis = notification.time
            setTvTime()
        }

        private fun setCalendarTime(hour: Int, minute: Int) {
            timeMaster.hour = hour
            timeMaster.minute = minute
            timeMaster.second = 0
            setTvTime()
        }

        private fun setCalendarDate(day: Int, month: Int, year: Int) {
            timeMaster.day = day
            timeMaster.month = month
            timeMaster.year = year
            setTvTime()
        }

        @SuppressLint("SetTextI18n")
        private fun setTvTime() {
            notification.time = timeMaster.timeInMillis
            tvTime.text = timeMaster.timeDateText
        }

        private fun openTimeAndDateDialogs() {
            TimePickerDialog.open(
                timeMaster.hour,
                timeMaster.minute
            ) { hour: Int, minute: Int ->
                DatePickerDialog.open(
                    timeMaster.day,
                    timeMaster.month,
                    timeMaster.year
                ) { day: Int, month: Int, year: Int -> setCalendarDate(day, month, year) }
                setCalendarTime(hour, minute)
            }
        }
    }
}