package com.carb0rane.storylib

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat

private const val TAG = "LinearStoryProgressIndicator"
class LinearStoryProgressIndicator(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {





    private var customIndex = 0
    private var screenWidth = 0
     var progressBarHeight = 0f
    private var horizontalGapBetweenProgressBars = 3f
    private var progressBarPaint: Paint
    private var singleProgressBarWidth = 7f
    private var countOfProgressBars = 4
    private lateinit var progressBarRightEdge: FloatArray
    private var progressBar: RectF? = null
    private var progressBarTop = 0
    private var progressBarBottom = 0
     var primaryColor = 0
     var secondaryColor = 0
    private var currentProgressIndex = 0
      var individualStoryDisplayDuration = 2000L //this is in ms
    private var progressBarAnimation: ValueAnimator? = null
    private var isAnimationCancelled = false
    private var playbackListener: PlaybackListener? = null


    init {


        val ta = context?.obtainStyledAttributes(
            attrs,
            R.styleable.LinearStoryProgressIndicator,
            0,
            0
        )


        try {

            progressBarHeight = ta?.getDimension(
                R.styleable.LinearStoryProgressIndicator_progressBarHeight,
                15f
            )!!.dpToPx(context)
            horizontalGapBetweenProgressBars = ta.getDimension(
                R.styleable.LinearStoryProgressIndicator_gapBetweenProgressBar,
               3f
            ).dpToPx(context)
            individualStoryDisplayDuration = ta.getInt(
                R.styleable.LinearStoryProgressIndicator_storyPlaybackDuration,
                3000
            ).toLong()
            primaryColor = ta.getColor(
                R.styleable.LinearStoryProgressIndicator_primaryColor,
                ContextCompat.getColor(context, R.color.progressBarPrimaryColor)

            )
            secondaryColor = ta.getColor(
                R.styleable.LinearStoryProgressIndicator_secondaryColor,
                ContextCompat.getColor(context, R.color.progressBarSecondaryColor)
            )

        } catch (e: Exception) {
            Log.d(TAG, ": exception occured : $e  ")

        } finally {
            ta?.recycle()
        }
        screenWidth = context?.resources?.displayMetrics?.widthPixels!!
        progressBarPaint = Paint().apply {
            isAntiAlias = true
            color = secondaryColor
            style = Paint.Style.FILL
            strokeCap = Paint.Cap.ROUND
        }
        progressBarTop = 0
        progressBarBottom = 10
        progressBar = RectF(
            0f,
            progressBarTop.toFloat(),
            30f,
            progressBarBottom.toFloat()
        ) // these values don't really matter as they are changes later on in onDraw


        test()


    }

    private fun test() {
        Log.d(TAG, "test: I am in test fun ")
    }

    fun setListener(playbackListener: PlaybackListener) {
        this.playbackListener = playbackListener
    }

    fun setProgressBarSize(size: Int) {
        this.countOfProgressBars = size
        progressBarRightEdge = FloatArray(countOfProgressBars)
        singleProgressBarWidth =
            (screenWidth - (countOfProgressBars + 1) * horizontalGapBetweenProgressBars) / countOfProgressBars


        startStoryPlayer(0)
    }

    private fun startStoryPlayer(index: Int) {
        customIndex = index
        if (customIndex >= countOfProgressBars) {
            Log.d(TAG, "startStoryPlayer: in the if")
            playbackListener?.onFinishedPlaying()
        } else {

            Log.d(
                TAG,
                "startStoryPlayer: i am animating with $customIndex and the count is $countOfProgressBars "
            )
            progressBarAnimation = ValueAnimator.ofFloat(0f, singleProgressBarWidth)
            progressBarAnimation?.setDuration(individualStoryDisplayDuration)
            progressBarAnimation?.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                progressBarRightEdge[customIndex] =
                    (customIndex + 1) * horizontalGapBetweenProgressBars + customIndex * singleProgressBarWidth + value
                invalidate()
            }
            progressBarAnimation?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {
                    if (!isAnimationCancelled) startStoryPlayer(customIndex + 1)
                }

                override fun onAnimationCancel(animator: Animator) {
                    isAnimationCancelled = true
                }

                override fun onAnimationRepeat(animation: Animator) {

                }


            })

            progressBarAnimation?.start()
            Log.d(TAG, "startStoryPlayer: I am in between start on loading image")
            playbackListener?.onStartedPlaying(customIndex)
        }


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until countOfProgressBars) {
            // This draws the secondary unfilled rounded rectangle which denotes the remaining part
            // left is the left coordinate for the rect and this is whatever index we are at and then mul by
            // the net gap of each bar (viz the gap and the size of prog bar itself ) and added another gap

            val left =
                (i * (horizontalGapBetweenProgressBars + singleProgressBarWidth) + horizontalGapBetweenProgressBars).toInt()
            var right =
                ((i + 1) * (horizontalGapBetweenProgressBars + singleProgressBarWidth)).toInt()
            progressBarPaint.color = secondaryColor
            progressBar!!.set(
                left.toFloat(),
                progressBarTop.toFloat(),
                right.toFloat(),
                progressBarBottom.toFloat()
            )
            canvas.drawRoundRect(
                progressBar!!,
                progressBarHeight, progressBarHeight, progressBarPaint
            )

            // this now draws the primary filled part
            right = progressBarRightEdge[i].toInt() // this array consists of the coordinates up until which each rect is to be drawn
           // Log.d(TAG, "Drawing for $i and pre is ${progressBarRightEdge.joinToString(", ")} duration is: $individualStoryDisplayDuration ")
            if (right > 0) {
                progressBarPaint.color = primaryColor
                progressBar!!.set(
                    left.toFloat(),
                    progressBarTop.toFloat(),
                    right.toFloat(),
                    progressBarBottom.toFloat()
                )
                canvas.drawRoundRect(
                    progressBar!!,
                    progressBarHeight, progressBarHeight, progressBarPaint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = screenWidth - paddingStart + paddingEnd
        val height =
            (paddingTop + paddingBottom + 2 * horizontalGapBetweenProgressBars + progressBarHeight).toInt()
        val w = resolveSizeAndState(width, widthMeasureSpec, 0)
        val h = resolveSizeAndState(height, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    fun pauseAnimation() {
        progressBarAnimation?.pause()
    }

    fun resumeAnimation() {
        progressBarAnimation?.resume()
    }

    fun cancelAnimation() {
        progressBarAnimation?.cancel()
    }

    private fun Float.dpToPx(context: Context?): Float {
        val metrics = context?.resources?.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
    }


}