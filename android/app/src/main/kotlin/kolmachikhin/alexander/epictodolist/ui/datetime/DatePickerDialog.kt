package kolmachikhin.alexander.epictodolist.ui.datetime

import android.widget.Button
import android.widget.DatePicker
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class DatePickerDialog : Dialog<Any?>(R.layout.date_picker_dialog) {

    private lateinit var datePicker: DatePicker
    private lateinit var buttonDone: Button
    private var listener: DatePickerListener? = null
    private var day = -1
    private var month = -1
    private var year = -1

    override fun findViews() {
        super.findViews()
        datePicker = find(R.id.date_picker)
        buttonDone = find(R.id.button_done)
    }

    override fun start() {
        super.start()
        if (day != -1 && month != -1 && year != -1) {
            datePicker.updateDate(year, month, day)
        }

        set(buttonDone) {
            listener?.onDateSet(datePicker.dayOfMonth, datePicker.month, datePicker.year)
            closeDialog()
        }
    }

    fun setDateSetListener(listener: DatePickerListener) {
        this.listener = listener
    }

    fun interface DatePickerListener {
        fun onDateSet(day: Int, month: Int, year: Int)
    }

    companion object {

        
        fun open(listener: DatePickerListener) {
            open(-1, -1, -1, listener)
        }

        
        fun open(day: Int, month: Int, year: Int, listener: DatePickerListener) {
            val d = DatePickerDialog()
            d.day = day
            d.month = month
            d.year = year
            d.setDateSetListener(listener)
            d.openDialog()
        }
    }
}