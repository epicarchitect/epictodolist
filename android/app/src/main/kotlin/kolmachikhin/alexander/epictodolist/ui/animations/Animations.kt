package kolmachikhin.alexander.epictodolist.ui.animations

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.animations.HeroAnimations.init

object Animations {

    fun init(context: Context) {
        NavigationAnimations.init(context)
        init()
    }


    fun slideView(view: View, currentHeight: Int, newHeight: Int, onEnd: EndAnimationListener? = null) {
        val slideAnimator = ValueAnimator
            .ofInt(currentHeight, newHeight)
            .setDuration(500)

        slideAnimator.addUpdateListener { animator ->
            view.layoutParams.height = (animator.animatedValue as Int)
            view.requestLayout()
        }

        val animationSet = AnimatorSet()
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        animationSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                onEnd?.onEndAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animationSet.play(slideAnimator)
        animationSet.start()
    }

    
    fun showView(v: View, l: EndAnimationListener?) {
        try {
            val animator = AnimatorInflater.loadAnimator(v.context, R.animator.show) as AnimatorSet
            animator.setTarget(v)
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    l?.onEndAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animator.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun longShowView(v: View, l: EndAnimationListener?) {
        try {
            val animator = AnimatorInflater.loadAnimator(v.context, R.animator.long_show) as AnimatorSet
            animator.setTarget(v)
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    l?.onEndAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animator.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    
    fun hideView(v: View, l: EndAnimationListener?) {
        try {
            val animator = AnimatorInflater.loadAnimator(v.context, R.animator.hide) as AnimatorSet
            animator.setTarget(v)
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    l?.onEndAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animator.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun interface EndAnimationListener {
        fun onEndAnimation()
    }
}