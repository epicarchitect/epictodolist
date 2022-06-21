package kolmachikhin.alexander.epictodolist.ui.graphs.holo

import android.animation.Animator
import android.view.animation.Interpolator

interface HoloGraphAnimate {

    var duration: Int
    var interpolator: Interpolator?
    val isAnimating: Boolean
    fun cancelAnimating(): Boolean
    fun animateToGoalValues()
    fun setAnimationListener(animationListener: Animator.AnimatorListener?)

    companion object {
        const val ANIMATE_NORMAL = 0
        const val ANIMATE_INSERT = 1
        const val ANIMATE_DELETE = 2
    }
}