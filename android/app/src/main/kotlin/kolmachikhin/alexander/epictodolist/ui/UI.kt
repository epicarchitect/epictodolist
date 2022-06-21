package kolmachikhin.alexander.epictodolist.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kolmachikhin.alexander.epictodolist.R
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

    
    var fragmentStackMaster: FragmentStackMaster

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        activity = a
        themeMaster = ThemeMaster(a)
        resourceMaster = ResourceMaster(a)
        displayMaster = DisplayMaster(a)
        fontMaster = FontMaster(a)
        soundMaster = SoundMaster(a)
        fragmentStackMaster = FragmentStackMaster()
    }

    fun recreate() = activity.recreate()


    fun restart(action: String? = "") {
        activity.finish()
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = action
        activity.startActivity(intent)
    }

    fun getString(res: Int) = resourceMaster.getString(res)

    fun getStringArray(res: Int) = resourceMaster.getStringArray(res)

    fun getColor(res: Int) = resourceMaster.getColor(res)

    fun getAttrColor(res: Int) = resourceMaster.getAttrColor(res)

    val colorPrimary: Int
        get() = getAttrColor(R.attr.colorPrimary)

    val colorPrimaryDark: Int
        get() = getAttrColor(R.attr.colorPrimaryDark)

    fun hideKeyboard() = displayMaster.hideKeyboard()

    fun dp(dp: Int) = displayMaster.dp(dp)

}