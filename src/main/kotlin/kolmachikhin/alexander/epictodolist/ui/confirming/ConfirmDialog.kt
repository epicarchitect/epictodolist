package kolmachikhin.alexander.epictodolist.ui.confirming

import android.widget.Button
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class ConfirmDialog : Dialog<Any?>(R.layout.confidence_check_dialog) {

    private lateinit var tvMessage: TextView
    private lateinit var buttonYes: Button
    private lateinit var buttonNo: Button
    private var message: String? = null
    private var listener: Listener? = null

    override fun findViews() {
        super.findViews()
        tvMessage = find(R.id.tv_message)
        buttonYes = find(R.id.button_yes)
        buttonNo = find(R.id.button_no)
    }

    override fun start() {
        super.start()
        tvTitle?.text = MainActivity.ui!!.getString(R.string.confirm_action)
        tvMessage.text = message

        set(buttonYes) {
            listener?.result(true)
            closeDialog()
        }

        set(buttonNo) {
            listener?.result(false)
            closeDialog()
        }
    }

    fun interface Listener {
        fun result(yes: Boolean)
    }

    companion object {
        fun open(listener: Listener?) {
            open(MainActivity.ui!!.getString(R.string.are_you_sure_you_want_to_do_this), listener)
        }

        fun open(message: String?, listener: Listener?) {
            val dialog = ConfirmDialog()
            dialog.listener = listener
            dialog.message = message
            dialog.openDialog()
        }
    }
}