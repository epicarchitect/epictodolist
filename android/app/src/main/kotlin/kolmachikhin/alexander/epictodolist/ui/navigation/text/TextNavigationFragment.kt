package kolmachikhin.alexander.epictodolist.ui.navigation.text

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.NavButtonFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.NavButtonFragment.OnSelectListener

class TextNavigationFragment(
    private val orientation: Int,
    container: ViewGroup
) : UIFragment(R.layout.text_navigation_fragment, container) {

    private lateinit var scrollButtons: HorizontalScrollView
    private lateinit var buttonsContainer: LinearLayout

    @SuppressLint("UseSparseArrays")
    private val buttonsMap = HashMap<Int, NavButtonFragment>()
    var activeId = -1
        private set

    override fun findViews() {
        scrollButtons = find(R.id.scroll_buttons)
        buttonsContainer = find(R.id.buttons_container)
    }

    override fun start() {
        if (orientation == TOP) {
            buttonsContainer.setPadding(0, MainActivity.ui!!.dp(4), 0, 0)
        } else if (orientation == BOTTOM) {
            buttonsContainer.setPadding(0, 0, 0, MainActivity.ui!!.dp(4))
        }
    }

    private fun scrollToButton(v: View) {
        val x = (MainActivity.ui!!.displayMaster.getViewXY(v).x
                + scrollButtons.scrollX
                - MainActivity.ui!!.displayMaster.screenWidth / 2
                + v.width / 2)
        scrollButtons.smoothScrollTo(x, 0)
    }

    fun addButton(id: Int, text: String, onSelectListener: OnSelectListener) {
        val f = TextNavButtonFragment(text, buttonsContainer)
        f.onSelectListener = OnSelectListener {
            scrollToButton(f.view)
            for (key in buttonsMap.keys) {
                val nav = buttonsMap[key]
                nav?.unselect()
            }
            onSelectListener.onSelect()
            activeId = id
        }
        f.add()
        buttonsMap[id] = f
    }

    fun select(id: Int) {
        buttonsMap[id]?.select()
    }

    fun showButton(id: Int) {
        buttonsMap[id]?.view?.visibility = View.VISIBLE
    }

    fun hideButton(id: Int) {
        buttonsMap[id]?.view?.visibility = View.GONE
    }

    companion object {
        const val TOP = 0
        const val BOTTOM = 1
    }
}