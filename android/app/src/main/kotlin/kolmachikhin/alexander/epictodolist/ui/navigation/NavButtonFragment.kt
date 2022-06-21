package kolmachikhin.alexander.epictodolist.ui.navigation

import android.view.ViewGroup
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

abstract class NavButtonFragment(layoutRes: Int, container: ViewGroup) : UIFragment(layoutRes, container) {

    var onSelectListener: OnSelectListener? = null

    override fun start() {
        set(view) { select() }
    }

    fun select() {
        onSelectListener?.onSelect()
        activate()
    }

    fun unselect() {
        deactivate()
    }

    protected abstract fun activate()

    protected abstract fun deactivate()

    fun interface OnSelectListener {
        fun onSelect()
    }
}