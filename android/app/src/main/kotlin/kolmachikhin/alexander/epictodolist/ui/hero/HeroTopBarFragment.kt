package kolmachikhin.alexander.epictodolist.ui.hero

import android.annotation.SuppressLint
import android.widget.ProgressBar
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.logic.hero.HeroLogic
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment

class HeroTopBarFragment : UIFragment(R.layout.hero_top_bar_fragment, MainActivity.heroTopBarContainer!!) {

    private lateinit var tvLevel: TextView
    private lateinit var tvCoins: TextView
    private lateinit var tvCrystals: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar

    override fun start() {}

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