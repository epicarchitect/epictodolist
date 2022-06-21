package kolmachikhin.alexander.epictodolist.ui.datetime

import android.text.format.DateFormat
import android.widget.Button
import android.widget.TimePicker
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class TimePickerDialog : Dialog<Any?>(R.layout.time_picker_dialog) {

    private lateinit var timePicker: TimePicker
    private lateinit var buttonDone: Button
    private var listener: TimePickerListener? = null
    private var hour = -1
    private var minute = -1

    override fun findViews() {
        super.findViews()
        timePicker = find(R.id.time_picker)
        buttonDone = find(R.id.button_done)
    }

    override fun start() {
        super.start()
        val is24 = DateFormat.is24HourFormat(view.context)
        timePicker.setIs24HourView(is24)

        if (hour != -1) {
            timePicker.hour = hour
        }

        if (minute != -1) {
            timePicker.minute = minute
        }

        set(buttonDone) {
            listener?.onTimeSet(timePicker.hour, timePicker.minute)
            closeDialog()
        }
    }

    fun setTimeSetListener(listener: TimePickerListener) {
        this.listener = listener
    }

    fun interface TimePickerListener {
        fun onTimeSet(hour: Int, minute: Int)
    }

    companion object {

        
        fun open(hour: Int, minute: Int, listener: TimePickerListener) {
            val d = TimePickerDialog()
            d.hour = hour
            d.minute = minute
            d.setTimeSetListener(listener)
            d.openDialog()
        }

        
        fun open(listener: TimePickerListener) {
            open(-1, -1, listener)
        }
    }
}