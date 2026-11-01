package kolmachikhin.alexander.epictodolist.ui.navigation.icon

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.NavButtonFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.NavButtonFragment.OnSelectListener

class IconNavigationFragment(
    private val orientation: Int,
    container: ViewGroup
) : UIFragment(R.layout.navigation_fragment, container) {

    private lateinit var buttonsContainer: LinearLayout

    var isHandleWindowInsets = false

    @SuppressLint("UseSparseArrays")
    private val buttonsMap = HashMap<Int, NavButtonFragment>()
    override fun findViews() {
        buttonsContainer = find(R.id.buttons_container)
    }

    override fun start() {
        if (orientation == TOP) {
            buttonsContainer.setPadding(0, MainActivity.ui!!.dp(4), 0, 0)
        } else if (orientation == BOTTOM) {
            buttonsContainer.setPadding(0, 0, 0, MainActivity.ui!!.dp(4))
        }

        if (isHandleWindowInsets) {
            ViewCompat.requestApplyInsets(buttonsContainer) // kostil
            ViewCompat.setOnApplyWindowInsetsListener(buttonsContainer) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> { bottomMargin = insets.bottom }
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    fun addButton(id: Int, iconRes: Int, onSelectListener: OnSelectListener) {
        val f = IconNavButtonFragment(iconRes, buttonsContainer)
        f.onSelectListener = OnSelectListener {
            for (key in buttonsMap.keys) {
                val nav = buttonsMap[key]
                nav?.unselect()
            }
            onSelectListener.onSelect()
        }
        f.add()
        buttonsMap[id] = f
    }

    fun select(id: Int) {
        val f = buttonsMap[id]
        f?.select()
    }

    companion object {
        const val TOP = 0
        const val BOTTOM = 1
    }
}