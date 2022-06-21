package kolmachikhin.alexander.epictodolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class UIFragment(
    layoutRes: Int,
    
    protected var container: ViewGroup
) {

    
    protected var context = container.context!!

    
    val view = LayoutInflater.from(context).inflate(layoutRes, container, false)!!

    abstract fun findViews()

    abstract fun start()

    open fun add() {
        container.addView(view)
        findViews()
        start()
    }

    fun <T : View?> find(id: Int) = view.findViewById<T>(id)

    open fun replace() {
        container.removeAllViews()
        add()
    }

    open fun remove() {
        MainActivity.ui!!.fragmentStackMaster.fragments.remove(this)
        container.removeView(view)
    }
}