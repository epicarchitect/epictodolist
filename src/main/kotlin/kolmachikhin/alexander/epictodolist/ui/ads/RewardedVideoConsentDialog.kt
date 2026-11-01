package kolmachikhin.alexander.epictodolist.ui.ads

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.confirming.ConfirmDialog
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class RewardedVideoConsentDialog : Dialog<String>(R.layout.rewarded_video_consent_dialog) {

    private lateinit var tvMessage: TextView
    private lateinit var checkPersonalizedAds: CheckBox
    private lateinit var tvAdsConsent: TextView
    private lateinit var buttonWatch: Button
    private lateinit var buttonNo: Button
    private var listener: Listener? = null

    override fun findViews() {
        super.findViews()
        tvMessage = find(R.id.tv_message)
        checkPersonalizedAds = find(R.id.check_personalized_ads)
        tvAdsConsent = find(R.id.tv_ads_consent)
        buttonWatch = find(R.id.button_watch)
        buttonNo = find(R.id.button_no)
    }

    override fun start() {
        super.start()
        tvTitle?.text = MainActivity.ui!!.getString(R.string.watch_video)
        tvMessage.text = model

        val text = MainActivity.ui!!.getString(R.string.personalized_ads_consent_text)
        tvAdsConsent.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        tvAdsConsent.movementMethod = LinkMovementMethod.getInstance()
        tvAdsConsent.setOnClickListener {
            checkPersonalizedAds.isChecked = !checkPersonalizedAds.isChecked
        }

        set(buttonWatch) {
            App.adsConsent.setPersonalizedAdsEnabled(checkPersonalizedAds.isChecked)
            listener?.watch()
            closeDialog()
        }

        set(buttonNo) {
            closeDialog()
        }
    }

    fun interface Listener {
        fun watch()
    }

    companion object {
        fun open(message: String?, listener: Listener?) {
            if (!App.adsConsent.personalizedAdsEnabled) {
                val dialog = RewardedVideoConsentDialog()
                dialog.model = message
                dialog.listener = listener
                dialog.openDialog()
            } else {
                ConfirmDialog.open(message) { yes ->
                    if (yes) listener?.watch()
                }
            }
        }
    }
}
