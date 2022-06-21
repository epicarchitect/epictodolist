package kolmachikhin.alexander.epictodolist.ui.icons

import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class IconDialog : Dialog<Any?>(R.layout.icons_dialog) {

    private var listener: IconDialogListener? = null
    private var iconsLayout: LinearLayout? = null

    override fun findViews() {
        super.findViews()
        iconsLayout = find<LinearLayout>(R.id.icons_layout)
    }

    override fun start() {
        super.start()
        tvTitle?.text = MainActivity.ui!!.getString(R.string.select_icon)
        buildIcons()
    }

    private fun buildLinearLayout(): LinearLayout {
        val params: ViewGroup.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.gravity = Gravity.CENTER
        layout.layoutParams = params
        return layout
    }

    private fun buildIcons() {
        iconsLayout!!.removeAllViews()
        val icons = IconHelper.ICONS
        var row = buildLinearLayout()
        for (i in icons.indices) {
            if (i % 5 == 0) {
                iconsLayout!!.addView(row)
                row = buildLinearLayout()
            }
            row.addView(buildIcon(i, icons[i]))
        }
        iconsLayout!!.addView(row)
    }

    private fun buildIcon(id: Int, iconRes: Int): ImageView {
        val params = LinearLayout.LayoutParams(MainActivity.ui!!.dp(36), MainActivity.ui!!.dp(36))
        params.setMargins(MainActivity.ui!!.dp(4), MainActivity.ui!!.dp(4), MainActivity.ui!!.dp(4), MainActivity.ui!!.dp(4))
        val ic = ImageView(context)
        ic.id = id
        ic.setImageResource(iconRes)
        ic.setPadding(MainActivity.ui!!.dp(4), MainActivity.ui!!.dp(4), MainActivity.ui!!.dp(4), MainActivity.ui!!.dp(4))
        ic.layoutParams = params
        set(ic) {
            listener?.onIconSelected(id)
            closeDialog()
        }
        return ic
    }

    fun interface IconDialogListener {
        fun onIconSelected(iconId: Int)
    }

    companion object {
        
        fun open(listener: IconDialogListener) {
            val iconDialog = IconDialog()
            iconDialog.listener = listener
            iconDialog.openDialog()
        }
    }
}