package kolmachikhin.alexander.epictodolist.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.R as AppCompatR
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.ads.RewardedVideoMaster
import kolmachikhin.alexander.epictodolist.ui.ads.YandexRewardedVideoMaster
import kolmachikhin.alexander.epictodolist.ui.masters.DisplayMaster
import kolmachikhin.alexander.epictodolist.ui.masters.FontMaster
import kolmachikhin.alexander.epictodolist.ui.masters.FragmentStackMaster
import kolmachikhin.alexander.epictodolist.ui.masters.ResourceMaster
import kolmachikhin.alexander.epictodolist.ui.masters.SoundMaster
import kolmachikhin.alexander.epictodolist.ui.masters.ThemeMaster

@SuppressLint("StaticFieldLeak")
class UI(a: AppCompatActivity) {
    var activity: AppCompatActivity
    var resourceMaster: ResourceMaster
    var displayMaster: DisplayMaster
    var themeMaster: ThemeMaster
    var fontMaster: FontMaster
    var soundMaster: SoundMaster
    var rewardedVideoMaster: RewardedVideoMaster
    var fragmentStackMaster: FragmentStackMaster

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        activity = a
        rewardedVideoMaster = YandexRewardedVideoMaster(a)
        themeMaster = ThemeMaster(a)
        resourceMaster = ResourceMaster(a)
        displayMaster = DisplayMaster(a)
        fontMaster = FontMaster(a)
        soundMaster = SoundMaster(a)
        fragmentStackMaster = FragmentStackMaster()
    }

    fun restart(action: String? = "") {
        activity.finish()
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = action
        activity.startActivity(intent)
    }

    fun goToBreakBadHabits() {
        val name = "kolmachikhin.alexander.breakbadhabits"
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$name".toUri()
                )
            )
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$name".toUri()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun goToAppInStore() {
        val name = activity.packageName
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$name".toUri()
                )
            )
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$name".toUri()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun goToPrivacyPolicy() {
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    activity.getString(R.string.privacy_policy_url).toUri()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun goToGithub() {
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/epicarchitect/epictodolist".toUri()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getString(res: Int) = resourceMaster.getString(res)

    fun getStringArray(res: Int) = resourceMaster.getStringArray(res)

    fun getColor(res: Int) = resourceMaster.getColor(res)

    fun getAttrColor(res: Int) = resourceMaster.getAttrColor(res)

    val colorPrimary: Int
        get() = getAttrColor(AppCompatR.attr.colorPrimary)

    val colorPrimaryDark: Int
        get() = getAttrColor(AppCompatR.attr.colorPrimaryDark)

    fun hideKeyboard() = displayMaster.hideKeyboard()

    fun dp(dp: Int) = displayMaster.dp(dp)
}