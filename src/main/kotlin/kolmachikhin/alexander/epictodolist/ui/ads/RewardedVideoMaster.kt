package kolmachikhin.alexander.epictodolist.ui.ads

import androidx.lifecycle.LiveData

interface RewardedVideoMaster {
    val adStatus: LiveData<AdStatus>

    fun showRewardedVideo(callback: Callback)

    interface Callback {
        fun onReward()
        fun onFailed(code: Int, message: String?)
    }

    enum class AdStatus {
        READY,
        LOADING,
        NOT_READY
    }
}