package kolmachikhin.alexander.epictodolist.ui.hero

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.MainNavigationFragment
import kolmachikhin.alexander.epictodolist.ui.MainNavigationFragment.Companion.select
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.graphs.progress.ProgressGraphFragment
import kolmachikhin.alexander.epictodolist.ui.hero.maker.HeroMakerDialog
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.text.TextNavigationFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class HeroFragment(container: ViewGroup) : UIFragment(R.layout.hero_fragment, container) {

    private lateinit var buttonEdit: ImageView
    private lateinit var buttonSettings: ImageView
    private lateinit var buttonHeaddress: ImageView
    private lateinit var buttonMusic: ImageView
    private lateinit var tvName: TextView
    private lateinit var heroBody: HeroBodyFragment
    private lateinit var heroBodyContainer: FrameLayout
    private lateinit var heroFrame: FrameLayout
    private lateinit var navigationContainer: FrameLayout
    private lateinit var fragmentsContainer: FrameLayout
    private lateinit var mainContainer: FrameLayout
    private lateinit var scrollView: ScrollView
    private lateinit var navigation: TextNavigationFragment
    private var needScroll = false

    override fun start() {
        MainActivity.core!!.statusLogic.updateTimeUsing()
        heroBody = HeroBodyFragment(heroBodyContainer)
        heroBody.add()
        val params = heroFrame.layoutParams
        params.height = HeroBodyFragment.needHeight
        heroFrame.layoutParams = params
        set(buttonHeaddress, 100) { changeHeaddressVisibility() }
        set(buttonMusic) { MusicSettingDialog.open() }
        set(buttonEdit) { HeroMakerDialog.open(mainContainer) { select(MainNavigationFragment.HERO) } }
        set(buttonSettings) { SettingsFragment.open(mainContainer) }
        showHero()
        if (MainActivity.core!!.settingsLogic.getSettings().showHeaddress) {
            buttonHeaddress.setImageResource(R.drawable.ic_headdress_hide)
        } else {
            buttonHeaddress.setImageResource(R.drawable.ic_headdress_show)
        }

        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.HERO_NAV)
        setNavigation()
        navigation.select(ATTRIBUTES)
    }

    private fun setNavigation() {
        navigation = TextNavigationFragment(TextNavigationFragment.TOP, navigationContainer)
        navigation.replace()
        navigation.addButton(ATTRIBUTES, MainActivity.ui!!.getString(R.string.attributes)) { showAttributes() }
        navigation.addButton(PROGRESS_GRAPH, MainActivity.ui!!.getString(R.string.hero_progress_graph)) { showProgressGraph() }
        navigation.addButton(STATISTICS, MainActivity.ui!!.getString(R.string.statistics)) { showStatistics() }
    }

    override fun findViews() {
        mainContainer = find(R.id.main_container)
        scrollView = find(R.id.scroll_view)
        heroFrame = find(R.id.hero_frame)
        buttonHeaddress = find(R.id.button_headdress)
        buttonMusic = find(R.id.button_music)
        tvName = find(R.id.name)
        buttonEdit = find(R.id.button_edit)
        buttonSettings = find(R.id.button_settings)
        heroBodyContainer = find(R.id.hero_body_container)
        navigationContainer = find(R.id.navigation_container)
        fragmentsContainer = find(R.id.fragments_container)
    }

    private fun changeHeaddressVisibility() {
        if (heroBody.isHaveHeaddress) {
            val settings = MainActivity.core!!.settingsLogic.getSettings()
            if (settings.showHeaddress) {
                settings.showHeaddress = false
                buttonHeaddress.setImageResource(R.drawable.ic_headdress_show)
                heroBody.hideHeaddress()
            } else {
                settings.showHeaddress = true
                buttonHeaddress.setImageResource(R.drawable.ic_headdress_hide)
                heroBody.showHeaddress()
            }
            MainActivity.core!!.settingsLogic.update(settings)
        } else {
            MessageFragment.show(MainActivity.ui!!.getString(R.string.headgear_is_missing))
        }
    }

    private fun scrollTo(y: Int) {
        scrollView.smoothScrollTo(0, y)
    }

    private fun showAttributes() {
        if (navigation.activeId != ATTRIBUTES) {
            if (needScroll) {
                Animations.hideView(fragmentsContainer) {
                    AttributesFragment.open(fragmentsContainer)
                    Animations.longShowView(fragmentsContainer) { scrollTo(getViewY(fragmentsContainer)) }
                }
            } else {
                AttributesFragment.open(fragmentsContainer)
                needScroll = true
            }
        }
    }

    private fun showProgressGraph() {
        if (navigation.activeId != PROGRESS_GRAPH) {
            Animations.hideView(fragmentsContainer) {
                ProgressGraphFragment.open(fragmentsContainer) { scrollTo(getViewY(fragmentsContainer)) }
                Animations.longShowView(fragmentsContainer) { scrollTo(getViewY(fragmentsContainer)) }
            }
        }
    }

    private fun showStatistics() {
        if (navigation.activeId != STATISTICS) {
            Animations.hideView(fragmentsContainer) {
                StatisticsFragment.open(fragmentsContainer)
                Animations.longShowView(fragmentsContainer) { scrollTo(getViewY(fragmentsContainer)) }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showHero() {
        val hero = MainActivity.core!!.heroLogic.hero
        tvName.text = hero.name
    }

    private fun getViewY(v: View) =
        MainActivity.ui!!.displayMaster.getViewXY(v).y +
                scrollView.scrollY - MainActivity.ui!!.dp(94) // top_bar + nav

    override fun remove() {
        Animations.hideView(container) { super.remove() }
    }

    override fun replace() {
        Animations.hideView(container) { super.replace() }
    }

    override fun add() {
        super.add()
        Animations.showView(container, null)
    }

    companion object {
        const val ATTRIBUTES = 0
        const val PROGRESS_GRAPH = 1
        const val STATISTICS = 2
    }
}