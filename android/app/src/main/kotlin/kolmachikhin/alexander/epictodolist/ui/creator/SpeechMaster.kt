package kolmachikhin.alexander.epictodolist.ui.creator

import android.widget.TextView
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import java.util.*

class SpeechMaster(private val tv: TextView) {

    private var timer: Timer
    private var isRunning = false
    private var onEndListener: OnEndListener? = null
    private var message = ""

    fun start(message: String, onEndListener: OnEndListener?) {
        this.message = message
        this.onEndListener = onEndListener

        tv.text = ""

        if (isRunning) {
            timer.cancel()
            timer = Timer()
        }

        timer.scheduleAtFixedRate(object : TimerTask() {
            var result = ""
            val time = (message.length * PERIOD).toLong()
            var cursor = 0
            override fun run() {
                isRunning = true
                try {
                    result += message[cursor]
                    cursor++
                    MainActivity.activity!!.runOnUiThread {
                        tv.text = result
                        if (time == (PERIOD * cursor).toLong()) {
                            stop()
                        }
                    }
                } catch (e: Exception) {
                    MainActivity.activity!!.runOnUiThread { stop() }
                }
            }
        }, 0, PERIOD.toLong())
    }

    fun stop() {
        if (isRunning) {
            timer.cancel()
            timer = Timer()
        }
        if (onEndListener != null) {
            onEndListener!!.onEnd()
            onEndListener = null
        }
        isRunning = false
        tv.text = message
    }

    fun interface OnEndListener {
        fun onEnd()
    }

    companion object {
        const val PERIOD = 70
    }

    init {
        timer = Timer()
    }
}