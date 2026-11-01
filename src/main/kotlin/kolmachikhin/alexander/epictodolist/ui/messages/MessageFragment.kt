package kolmachikhin.alexander.epictodolist.ui.messages

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.animations.Animations

class MessageFragment : UIFragment(R.layout.message_fragment, MainActivity.messageContainer!!) {

    private lateinit var tvMessage: TextView
    var message: String? = null
    var seconds = 0

    override fun findViews() {
        tvMessage = find(R.id.tv_message)
    }

    @SuppressLint("StaticFieldLeak")
    override fun start() {
        tvMessage.text = message

        object : CountDownTimer((seconds * 1000).toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                seconds = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                remove()
            }
        }.start()

        ViewCompat.requestApplyInsets(view) // kostil
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun add() {
        container.addView(view)
        findViews()
        start()
        Animations.showView(view, null)
    }

    override fun remove() {
        Animations.hideView(view) { super.remove() }
    }

    companion object {

        fun show(message: String?, seconds: Int = 4) {
            val f = MessageFragment()
            f.message = message
            f.seconds = seconds
            f.add()
        }
    }
}