package kolmachikhin.alexander.epictodolist.logic.ads

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.ads.RewardedVideoMaster
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment

class AdLogic(core: Core) : Logic(core) {

    init {
        ready()
    }

    override fun postInit() {}
    override fun attachRef() {}

    fun showRewardedVideoAdForCrystals() {
        showRewardedVideoAd {
            val hero = core.heroLogic.hero
            hero.crystals += REWARDED_VIDEO_CRYSTALS_REWARD
            core.heroLogic.update(hero)
        }
    }

    fun showRewardedVideoAdForCoins() {
        showRewardedVideoAd {
            val hero = core.heroLogic.hero
            hero.coins += REWARDED_VIDEO_COINS_REWARD
            core.heroLogic.update(hero)
        }
    }

    fun showRewardedVideoAd(completeListener: AdCompleteListener) {
        if (core.isOnline) {
            MessageFragment.show(MainActivity.ui!!.getString(R.string.please_wait))

            MainActivity.ui!!.rewardedVideoMaster.showRewardedVideo(object :
                RewardedVideoMaster.Callback {
                override fun onReward() {
                    completeListener.onComplete()
                }

                override fun onFailed(code: Int, message: String?) {
                    if (message == null) {
                        MessageFragment.show("Sorry, ads currently unavailable.")
                    } else {
                        MessageFragment.show(message)
                    }
                }
            })
        } else {
            MessageFragment.show(getString(R.string.no_internet_connection))
        }
    }

    fun interface AdCompleteListener {
        fun onComplete()
    }

    companion object {
        const val REWARDED_VIDEO_CRYSTALS_REWARD = 5
        const val REWARDED_VIDEO_COINS_REWARD = 100
    }
}