package kolmachikhin.alexander.epictodolist.ui.hero.maker

import android.view.View
import android.widget.Button
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.ads.RewardedVideoMaster.AdStatus
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class HeroEditPaymentDialog : Dialog<HeroModel>(R.layout.hero_edit_payment_dialog) {

    private lateinit var buttonSpendCoins: Button
    private lateinit var buttonWatchVideo: Button
    private lateinit var tvOr: TextView
    private var listener: Listener? = null

    override fun findViews() {
        super.findViews()
        buttonSpendCoins = find(R.id.button_spend_coins)
        buttonWatchVideo = find(R.id.button_watch_video)
        tvOr = find(R.id.tv_or)
    }

    override fun start() {
        super.start()
        tvTitle?.text = MainActivity.ui!!.getString(R.string.to_change_hero)
        set(buttonSpendCoins) {
            if (model!!.coins >= HERO_EDIT_PRICE) {
                model!!.coins -= HERO_EDIT_PRICE
                // Core.heroLogic.update(model); Not update because update in heroMaker
                listener!!.onDone()
            } else {
                MessageFragment.show(MainActivity.ui!!.getString(R.string.not_enough_coins))
            }
            closeDialog()
        }

        MainActivity.ui!!.rewardedVideoMaster.adStatus.observe(MainActivity.activity!!) { status ->
            if (status == AdStatus.READY) {
                tvOr.visibility = View.VISIBLE
                buttonWatchVideo.visibility = View.VISIBLE
            } else {
                tvOr.visibility = View.GONE
                buttonWatchVideo.visibility = View.GONE
            }
        }

        set(buttonWatchVideo) {
            MainActivity.core!!.adLogic.showRewardedVideoAd {
                listener?.onDone()
            }
            closeDialog()
        }
    }

    fun interface Listener {
        fun onDone()
    }

    companion object {
        const val HERO_EDIT_PRICE = 200
        fun open(hero: HeroModel, l: Listener) {
            val dialog = HeroEditPaymentDialog()
            dialog.listener = l
            dialog.model = hero
            dialog.openDialog()
        }
    }
}