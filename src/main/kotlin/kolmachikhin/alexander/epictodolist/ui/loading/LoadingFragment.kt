package kolmachikhin.alexander.epictodolist.ui.loading

import android.view.ViewGroup
import android.widget.FrameLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.animations.Animations

class LoadingFragment(container: ViewGroup) : UIFragment(R.layout.loading_fragment, container) {

    private lateinit var mainLayout: FrameLayout

    override fun findViews() {
        mainLayout = find(R.id.main_layout)
    }

    override fun start() {}

    fun open() {
        add()
        Animations.showView(mainLayout, null)
    }

    fun close() {
        Animations.hideView(mainLayout) { this.remove() }
    }
}