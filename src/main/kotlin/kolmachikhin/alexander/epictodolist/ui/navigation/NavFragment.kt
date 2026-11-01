package kolmachikhin.alexander.epictodolist.ui.navigation

import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment

abstract class NavFragment(layoutRes: Int) : UIFragment(layoutRes, MainActivity.navContainer!!) {

    fun showTitle(title: String) {
        if (MainActivity.core!!.settingsLogic.getSettings().showTitleNav) {
            MessageFragment.show(title, 1)
        }
    }
}