package kolmachikhin.alexander.epictodolist.ui

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

abstract class Dialog<M> : UIFragment {

    protected lateinit var mainLayout: FrameLayout
    protected lateinit var buttonBack: ImageView
    protected var tvTitle: TextView? = null
    
    var model: M? = null

    constructor(layoutRes: Int) : super(layoutRes, MainActivity.dialogContainer!!)

    constructor(layoutRes: Int, container: ViewGroup) : super(layoutRes, container)

    override fun findViews() {
        mainLayout = find(R.id.main_layout)
        tvTitle = find(R.id.title)
        buttonBack = find(R.id.button_back)
    }

    override fun start() {
        set(buttonBack) { closeDialog() }
    }

    open fun openDialog() {
        add()
        Animations.showView(mainLayout, null)
        MainActivity.ui!!.hideKeyboard()
        MainActivity.ui!!.fragmentStackMaster.add(this)
    }

    open fun closeDialog() {
        buttonBack.isClickable = false
        Animations.hideView(mainLayout) { this.remove() }
        MainActivity.ui!!.hideKeyboard()
    }
}