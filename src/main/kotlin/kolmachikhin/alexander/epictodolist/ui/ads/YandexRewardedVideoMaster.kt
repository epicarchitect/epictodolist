package kolmachikhin.alexander.epictodolist.ui.ads

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kolmachikhin.alexander.epictodolist.BuildConfig
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.YandexAds
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import kolmachikhin.alexander.epictodolist.App

class YandexRewardedVideoMaster(
    private val activity: Activity
) : RewardedVideoMaster {
    private var currentCallback: RewardedVideoMaster.Callback? = null
    private var isVideoRunning = false
    override val adStatus = MutableLiveData(RewardedVideoMaster.AdStatus.NOT_READY)

    private var currentAd: RewardedAd? = null
    private var loadedWithPersonalizedAds = false
    private val loader by lazy { RewardedAdLoader(activity) }
    private val adLoadListener = object : RewardedAdLoadListener {
        override fun onAdLoaded(rewarded: RewardedAd) {
            handler.removeCallbacks(retryRunnable)
            retryDelay = INITIAL_RETRY_DELAY
            retryAttempts = 0
            if (loadedWithPersonalizedAds != App.adsConsent.personalizedAdsEnabled) {
                currentAd = null
                adStatus.value = RewardedVideoMaster.AdStatus.NOT_READY
                load()
                return
            }
            currentAd = rewarded
            currentAd!!.setAdEventListener(adListener)
            adStatus.value = RewardedVideoMaster.AdStatus.READY

            if (currentCallback != null && !isVideoRunning) {
                isVideoRunning = true
                currentAd!!.show(activity)
            }
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            currentAd = null
            adStatus.value = RewardedVideoMaster.AdStatus.NOT_READY
            if (currentCallback != null) {
                currentCallback!!.onFailed(error.code, error.description)
            }
            isVideoRunning = false
            currentCallback = null
            currentAd = null
            schedulePreloadRetry()
        }
    }
    private val adListener = object : RewardedAdEventListener {
        override fun onAdShown() {
            Log.d(TAG, "onAdShown")
        }

        override fun onAdFailedToShow(adError: AdError) {
            adStatus.value = RewardedVideoMaster.AdStatus.NOT_READY
            currentAd = null
            isVideoRunning = false
            currentCallback?.onFailed(0, adError.description)
            currentCallback = null
            preload()
        }

        override fun onAdDismissed() {
            adStatus.value = RewardedVideoMaster.AdStatus.NOT_READY
            currentAd = null
            isVideoRunning = false
            currentCallback = null
            preload()
            Log.d(TAG, "onAdDismissed")
        }

        override fun onAdClicked() {
            Log.d(TAG, "onAdClicked")
        }

        override fun onAdImpression(impressionData: ImpressionData?) {
            Log.d(TAG, "onAdImpression $impressionData")
        }

        override fun onRewarded(reward: Reward) {
            if (currentCallback != null) {
                currentCallback!!.onReward()
            }
            adStatus.value = RewardedVideoMaster.AdStatus.NOT_READY
            currentCallback = null
            currentAd = null
            isVideoRunning = false
            Log.d(TAG, "onRewarded $reward")
        }
    }

    private var initialized = false
    private var initializing = false
    private var pendingCallback: RewardedVideoMaster.Callback? = null
    private val handler = Handler(Looper.getMainLooper())
    private var retryDelay = INITIAL_RETRY_DELAY
    private var retryAttempts = 0
    private val retryRunnable = Runnable { preload() }
    private val adUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_AD_ID else AD_ID

    init {
        initialize()
    }

    private fun initialize() {
        if (initialized || initializing) return
        initializing = true
        YandexAds.setUserConsent(App.adsConsent.personalizedAdsEnabled)
        YandexAds.setAgeRestricted(false)
        YandexAds.setAppAdAnalyticsReporting(App.analytics.analyticsEnabled)
        YandexAds.initialize(activity.applicationContext) {
            initialized = true
            initializing = false
            val callback = pendingCallback
            pendingCallback = null
            if (callback != null) showOrLoad(callback) else preload()
        }
    }

    override fun showRewardedVideo(callback: RewardedVideoMaster.Callback) {
        Log.d(TAG, "call showRewardedVideo")
        if (initialized) {
            showOrLoad(callback)
            return
        }
        pendingCallback = callback
        initialize()
    }

    private fun showOrLoad(callback: RewardedVideoMaster.Callback) {
        YandexAds.setUserConsent(App.adsConsent.personalizedAdsEnabled)
        YandexAds.setAppAdAnalyticsReporting(App.analytics.analyticsEnabled)
        currentCallback = callback
        if (currentAd != null && loadedWithPersonalizedAds != App.adsConsent.personalizedAdsEnabled) {
            currentAd = null
            adStatus.value = RewardedVideoMaster.AdStatus.NOT_READY
        }
        if (currentAd != null && !isVideoRunning) {
            currentAd!!.setAdEventListener(adListener)
            currentAd!!.show(activity)
            isVideoRunning = true
        } else {
            load()
        }
    }

    private fun preload() {
        if (currentAd == null && !isVideoRunning && adStatus.value != RewardedVideoMaster.AdStatus.LOADING) {
            load()
        }
    }

    private fun schedulePreloadRetry() {
        if (currentAd != null || isVideoRunning || retryAttempts >= MAX_RETRY_ATTEMPTS) return
        retryAttempts++
        handler.removeCallbacks(retryRunnable)
        handler.postDelayed(retryRunnable, retryDelay)
        retryDelay = (retryDelay * 2).coerceAtMost(MAX_RETRY_DELAY)
    }

    private fun load() {
        if (adStatus.value != RewardedVideoMaster.AdStatus.LOADING) {
            if (adUnitId.isBlank()) {
                adStatus.value = RewardedVideoMaster.AdStatus.NOT_READY
                return
            }
            handler.removeCallbacks(retryRunnable)
            Log.d(TAG, "load")
            YandexAds.setUserConsent(App.adsConsent.personalizedAdsEnabled)
            YandexAds.setAppAdAnalyticsReporting(App.analytics.analyticsEnabled)
            loadedWithPersonalizedAds = App.adsConsent.personalizedAdsEnabled
            adStatus.value = RewardedVideoMaster.AdStatus.LOADING
            loader.loadAd(AdRequest.Builder(adUnitId).build(), adLoadListener)
        }
    }

    companion object {
        private const val TAG = "YandexRewardedVideoMaster"
        private const val AD_ID = "R-M-20214884-1"
        private const val TEST_AD_ID = "demo-rewarded-yandex"
        private const val INITIAL_RETRY_DELAY = 10_000L
        private const val MAX_RETRY_DELAY = 60_000L
        private const val MAX_RETRY_ATTEMPTS = 3
    }
}
