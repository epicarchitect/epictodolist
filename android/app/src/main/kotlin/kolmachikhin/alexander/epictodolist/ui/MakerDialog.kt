package kolmachikhin.alexander.epictodolist.ui

import android.view.ViewGroup
import android.widget.ImageView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

abstract class MakerDialog<M> : Dialog<M> {

    
    var modeCreate = false

    
    var modeEdit = false

    protected lateinit var buttonDone: ImageView

    
    var onDoneListener: OnDoneListener<M>? = null

    constructor(layoutRes: Int) : super(layoutRes)
    constructor(layoutRes: Int, container: ViewGroup) : super(layoutRes, container)

    override fun findViews() {
        super.findViews()
        buttonDone = find(R.id.button_done)
    }

    override fun start() {
        super.start()
        set(buttonDone) {
            buttonDone.isClickable = false
            onDoneListener?.onDone(done())
            closeDialog()
        }

        if (modeEdit) tvTitle?.text = MainActivity.ui!!.getString(R.string.editing)
        else tvTitle?.text = MainActivity.ui!!.getString(R.string.creating)
    }

    protected abstract fun done(): M

    fun setModeCreate() {
        modeCreate = true
        modeEdit = false
    }

    fun setModeEdit() {
        modeCreate = false
        modeEdit = true
    }

    fun interface OnDoneListener<M> {
        fun onDone(m: M)
    }
}