package kolmachikhin.alexander.epictodolist.ui.graphs.holo.pie

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.graphs.holo.HoloGraphAnimate

class PieGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), HoloGraphAnimate {

    private var mPadding: Int
    private var mInnerCircleRatio: Int
    private var mSlices = ArrayList<PieSlice>()
    private val mPaint = Paint()
    private var mSelectedIndex = -1
    private var mListener: OnSliceClickedListener? = null
    private var mDrawCompleted = false
    private val mRectF = RectF()
    private var mBackgroundImage: Bitmap? = null
    private val mBackgroundImageAnchor = Point(0, 0)
    private var mBackgroundImageCenter = false
    override var duration = 300 //in ms
    override var interpolator: Interpolator? = null
    private var mAnimationListener: Animator.AnimatorListener? = null
    private var mValueAnimator: ValueAnimator? = null

    override val isAnimating: Boolean
        get() = if (mValueAnimator != null) mValueAnimator!!.isRunning else false

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PieGraph, 0, 0)
        mInnerCircleRatio = a.getInt(R.styleable.PieGraph_pieInnerCircleRatio, 0)
        mPadding = a.getDimensionPixelSize(R.styleable.PieGraph_pieSlicePadding, 0)
    }

    public override fun onDraw(canvas: Canvas) {
        var radius: Float
        canvas.drawColor(Color.TRANSPARENT)
        mPaint.reset()
        mPaint.isAntiAlias = true
        if (mBackgroundImage != null) {
            if (mBackgroundImageCenter) mBackgroundImageAnchor[width / 2 - mBackgroundImage!!.width / 2] = height / 2 - mBackgroundImage!!.height / 2
            canvas.drawBitmap(mBackgroundImage!!, mBackgroundImageAnchor.x.toFloat(), mBackgroundImageAnchor.y.toFloat(), mPaint)
        }
        var currentAngle = 270f
        var currentSweep: Float
        var totalValue = 0f
        val midX = (width / 2).toFloat()
        val midY = (height / 2).toFloat()
        radius = if (midX < midY) {
            midX
        } else {
            midY
        }
        radius -= mPadding.toFloat()
        val innerRadius = radius * mInnerCircleRatio / 255
        for (slice in mSlices) {
            totalValue += slice.value
        }
        for ((count, slice) in mSlices.withIndex()) {
            val p = slice.path
            p.reset()
            if (mSelectedIndex == count && mListener != null) {
                mPaint.color = slice.selectedColor
            } else {
                mPaint.color = slice.color
            }
            currentSweep = slice.value / totalValue * 360
            mRectF[midX - radius, midY - radius, midX + radius] = midY + radius
            createArc(
                p, mRectF, currentSweep,
                currentAngle + mPadding, currentSweep - mPadding
            )
            mRectF[midX - innerRadius, midY - innerRadius, midX + innerRadius] = midY + innerRadius
            createArc(
                p, mRectF, currentSweep,
                currentAngle + mPadding + (currentSweep - mPadding),
                -(currentSweep - mPadding)
            )
            p.close()

            // Create selection region
            val r = slice.region
            r[(midX - radius).toInt(), (midY - radius).toInt(), (midX + radius).toInt()] = (midY + radius).toInt()
            canvas.drawPath(p, mPaint)
            currentAngle += currentSweep
        }
        mDrawCompleted = true
    }

    private fun createArc(p: Path, mRectF: RectF, currentSweep: Float, startAngle: Float, sweepAngle: Float) {
        if (currentSweep == 360f) {
            p.addArc(mRectF, startAngle, sweepAngle)
        } else {
            p.arcTo(mRectF, startAngle, sweepAngle)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mDrawCompleted) {
            val point = Point()
            point.x = event.x.toInt()
            point.y = event.y.toInt()
            val r = Region()
            for ((count, slice) in mSlices.withIndex()) {
                r.setPath(slice.path, slice.region)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> if (r.contains(point.x, point.y)) {
                        mSelectedIndex = count
                        postInvalidate()
                    }
                    MotionEvent.ACTION_UP -> if (count == mSelectedIndex && mListener != null && r.contains(point.x, point.y)) {
                        mListener!!.onClick(mSelectedIndex)
                    }
                    else -> {}
                }
            }
        }
        // Case we click somewhere else, also get feedback!
        if (MotionEvent.ACTION_UP == event.action && mSelectedIndex == -1 && mListener != null) {
            mListener!!.onClick(mSelectedIndex)
        }
        // Reset selection
        if (MotionEvent.ACTION_UP == event.action
            || MotionEvent.ACTION_CANCEL == event.action
        ) {
            mSelectedIndex = -1
            postInvalidate()
        }
        return true
    }

    var backgroundBitmap: Bitmap?
        get() = mBackgroundImage
        set(backgroundBitmap) {
            mBackgroundImageCenter = true
            mBackgroundImage = backgroundBitmap
            postInvalidate()
        }

    fun setBackgroundBitmap(backgroundBitmap: Bitmap?, pos_x: Int, pos_y: Int) {
        mBackgroundImage = backgroundBitmap
        mBackgroundImageAnchor[pos_x] = pos_y
        postInvalidate()
    }

    /**
     * sets padding
     * @param padding
     */
    fun setPadding(padding: Int) {
        mPadding = padding
        postInvalidate()
    }

    fun setInnerCircleRatio(innerCircleRatio: Int) {
        mInnerCircleRatio = innerCircleRatio
        postInvalidate()
    }

    var slices: ArrayList<PieSlice>
        get() = mSlices
        set(slices) {
            mSlices = slices
            postInvalidate()
        }

    fun getSlice(index: Int): PieSlice {
        return mSlices[index]
    }

    fun addSlice(slice: PieSlice) {
        mSlices.add(slice)
        postInvalidate()
    }

    fun setOnSliceClickedListener(listener: OnSliceClickedListener?) {
        mListener = listener
    }

    fun removeSlices() {
        mSlices.clear()
        postInvalidate()
    }

    override fun cancelAnimating(): Boolean {
        if (mValueAnimator != null) mValueAnimator!!.cancel()
        return false
    }

    /**
     * Stops running animation and starts a new one, animating each slice from their current to goal value.
     * If removing a slice, consider animating to 0 then removing in onAnimationEnd onDoneListener.
     * Default inerpolator is linear; constant speed.
     */
    override fun animateToGoalValues() {
        if (mValueAnimator != null) mValueAnimator!!.cancel()
        for (s in mSlices) s.oldValue = s.value
        val va = ValueAnimator.ofFloat(0f, 1f)
        mValueAnimator = va
        va.duration = duration.toLong()
        if (interpolator == null) interpolator = LinearInterpolator()
        va.interpolator = interpolator
        if (mAnimationListener != null) va.addListener(mAnimationListener)
        va.addUpdateListener { animation: ValueAnimator ->
            val f = animation.animatedFraction.coerceAtLeast(0.01f) //avoid blank frames; never multiply values by 0
            // Log.d("f", String.valueOf(f));
            for (s in mSlices) {
                val x = s.goalValue - s.oldValue
                s.value = s.oldValue + x * f
            }
            postInvalidate()
        }
        va.start()
    }

    override fun setAnimationListener(animationListener: Animator.AnimatorListener?) {
        mAnimationListener = animationListener
    }

    fun interface OnSliceClickedListener {
        fun onClick(index: Int)
    }
}