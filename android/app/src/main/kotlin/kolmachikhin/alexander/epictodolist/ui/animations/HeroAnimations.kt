package kolmachikhin.alexander.epictodolist.ui.animations

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import kolmachikhin.alexander.epictodolist.ui.MainActivity

object HeroAnimations {
    private const val BREATH_DURATION = 1700
    lateinit var breath: Animation

    
    fun init() {
        breath = TranslateAnimation(0f, 0f, 0f, MainActivity.ui!!.dp(4).toFloat())
        breath.duration = BREATH_DURATION.toLong()
        breath.repeatCount = Animation.INFINITE
        breath.repeatMode = Animation.REVERSE
        breath.interpolator = AccelerateDecelerateInterpolator()
    }
}