package kolmachikhin.alexander.epictodolist.ui.navigation.text

import android.view.ViewGroup
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.navigation.NavButtonFragment

class TextNavButtonFragment(
    private val text: String,
    container: ViewGroup
) : NavButtonFragment(R.layout.text_nav_button_fragment, container) {

    private lateinit var tv: TextView

    override fun findViews() {
        tv = find(R.id.tv)
    }

    override fun start() {
        super.start()
        tv.text = text
    }

    override fun activate() {
        tv.setTextColor(MainActivity.ui!!.getAttrColor(R.attr.colorPrimaryDark))
        tv.setBackgroundResource(R.drawable.bg_nav_active)
    }

    override fun deactivate() {
        tv.setTextColor(MainActivity.ui!!.getAttrColor(R.attr.colorPrimary))
        tv.setBackgroundResource(R.drawable.bg_nav)
    }
}