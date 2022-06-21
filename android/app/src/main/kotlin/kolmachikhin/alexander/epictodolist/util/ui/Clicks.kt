package kolmachikhin.alexander.epictodolist.util.ui

import android.view.View

object Clicks {

    
    fun set(view: View, listener: View.OnClickListener) {
        set(view, 1000, listener)
    }

    
    fun set(view: View, delay: Long, listener: View.OnClickListener) {
        view.setOnClickListener(object : View.OnClickListener {
            var lastTimeClicked: Long = 0
            override fun onClick(v: View) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTimeClicked < delay) return
                lastTimeClicked = currentTime
                listener.onClick(v)
            }
        })
    }
}