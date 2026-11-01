package kolmachikhin.alexander.epictodolist.ui.hero

import android.annotation.SuppressLint
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.logic.hero.HeroLogic
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.ads.RewardedVideoConsentDialog
import kolmachikhin.alexander.epictodolist.ui.ads.RewardedVideoMaster
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class HeroTopBarFragment : UIFragment(
    layoutRes = R.layout.hero_top_bar_fragment,
    container = MainActivity.heroTopBarContainer!!
) {
    private lateinit var tvLevel: TextView
    private lateinit var tvCoins: TextView
    private lateinit var tvCrystals: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonAddCrystals: ImageView

    override fun start() {
        MainActivity.ui!!.rewardedVideoMaster.adStatus.observe(MainActivity.activity!!) { status ->
            buttonAddCrystals.isVisible = status == RewardedVideoMaster.AdStatus.READY
            TransitionManager.beginDelayedTransition(container)
        }

        set(buttonAddCrystals) {
            RewardedVideoConsentDialog.open(
                MainActivity.ui!!.getString(R.string.watch_a_video_for_5_crystals)
            ) {
                MainActivity.core!!.adLogic.showRewardedVideoAdForCrystals()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> { topMargin = insets.top }
            WindowInsetsCompat.CONSUMED
        }
    }

    @SuppressLint("SetTextI18n")
    fun setHeroTopBar(hero: HeroModel) {
        tvLevel.text = hero.level().toString()
        tvProgress.text = hero.progressOfLevel().toString() + "/" + hero.pointOfLevelUp()
        progressBar.progress = hero.progressPercent()
        tvCoins.text = hero.coins.toString()
        tvCrystals.text = hero.crystals.toString()
    }

    override fun findViews() {
        tvLevel = find(R.id.level)
        tvProgress = find(R.id.progress_of_level)
        tvCoins = find(R.id.coins)
        tvCrystals = find(R.id.crystals)
        progressBar = find(R.id.progress_bar)
        buttonAddCrystals = find(R.id.button_add_crystals)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: HeroTopBarFragment? = null
            private set

        fun init() {
            instance = HeroTopBarFragment()
            instance!!.replace()
        }

        fun setAfterCoreInit() {
            MainActivity.core!!.heroLogic.observer
                .addListener(HeroLogic.UPDATE) { hero -> instance!!.setHeroTopBar(hero) }
                .addListener(HeroLogic.CREATE) { hero -> instance!!.setHeroTopBar(hero) }

            instance!!.setHeroTopBar(MainActivity.core!!.heroLogic.hero)
        }

        fun update(hero: HeroModel) {
            instance?.setHeroTopBar(hero)
        }
    }
}