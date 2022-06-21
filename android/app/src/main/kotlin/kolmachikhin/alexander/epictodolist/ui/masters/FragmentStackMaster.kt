package kolmachikhin.alexander.epictodolist.ui.masters

import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.UIFragment

class FragmentStackMaster {

    var fragments = ArrayList<UIFragment>()

    fun add(f: UIFragment) {
        fragments.add(f)
    }

    fun remove() = try {
        val f = fragments[fragments.size - 1]
        if (f is Dialog<*>) {
            f.closeDialog()
        } else {
            f.remove()
        }
        true
    } catch (e: Exception) {
        fragments.clear()
        false
    }
}