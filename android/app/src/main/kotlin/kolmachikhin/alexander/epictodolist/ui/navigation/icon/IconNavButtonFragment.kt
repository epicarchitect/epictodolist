package kolmachikhin.alexander.epictodolist.ui.navigation.icon

import android.view.ViewGroup
import android.widget.ImageView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.navigation.NavButtonFragment

class IconNavButtonFragment(
    private val iconRes: Int,
    container: ViewGroup
) : NavButtonFragment(R.layout.icon_nav_button_fragment, container) {

    private lateinit var icon: ImageView

    override fun findViews() {
        icon = find(R.id.icon)
    }

    override fun start() {
        super.start()
        icon.setImageResource(iconRes)
    }

    override fun activate() {
        icon.setBackgroundResource(R.drawable.bg_nav_active)
    }

    override fun deactivate() {
        icon.setBackgroundResource(R.drawable.bg_nav)
    }
}