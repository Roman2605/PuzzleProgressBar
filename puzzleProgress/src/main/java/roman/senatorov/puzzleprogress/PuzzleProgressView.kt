package roman.senatorov.puzzleprogress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import java.util.*
import kotlin.math.floor

class PuzzleProgressView: View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var minSquaresCount: Int = 10
    private val squarePaint = Paint().apply {
        color = Color.BLUE // Цвет квадратных элементов
        style = Paint.Style.FILL
    }

    private val random = Random()

    private val squaresShownList = mutableListOf<Boolean>()

    private var horizontalSquareCount = 10
    private var verticalSquareCount = 10

    private var squareWidth: Int = 0
    private var squareHeight: Int = 0

    private var isAnimating = false

    private val updateInterval: Long = 100

    private var useFadeInFadeOutAnimation: Boolean = true
    private var isFadingIn = false
    private var isFadingOut = false
    private var fadeInDuration: Long = 200
    private var fadeOutDuration: Long = 200

    init {
        invalidate()
    }


//    override fun onDetachedFromWindow() {
//        stopAnimation()
//        super.onDetachedFromWindow()
//    }

    fun setFadeInDuration(duration: Long) {
        fadeInDuration = duration
    }

    fun setFadeOutDuration(duration: Long) {
        fadeOutDuration = duration
    }
    fun useFadeInFadeOutAnimation(use: Boolean){
        this.useFadeInFadeOutAnimation = use
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            // Перерисовываем только при необходимости
            if (isAnimating) {
                if(updateSquareListNumbers())
                    invalidate()
                postDelayed(this, updateInterval)
            }
        }
    }

    // Добавляем возможность плавного появления (fadeIn)
    private fun fadeIn() {
        if (isFadingIn) return // Уже в процессе появления
        isFadingIn = true

        val alphaAnimation = AlphaAnimation(0f, alpha)
        alphaAnimation.duration = fadeInDuration
        alphaAnimation.interpolator = AccelerateDecelerateInterpolator()
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                visibility = VISIBLE
                isAnimating = true
                invalidate()
            }

            override fun onAnimationEnd(animation: Animation?) {
                isFadingIn = false
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        startAnimation(alphaAnimation)
    }

    // Добавляем возможность плавного завершения (fadeOut)
    private fun fadeOut() {
        if (isFadingOut) return // Уже в процессе исчезновения
        isFadingOut = true

        val alphaAnimation = AlphaAnimation(alpha, 0f)
        alphaAnimation.duration = fadeOutDuration
        alphaAnimation.interpolator = AccelerateDecelerateInterpolator()
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                visibility = INVISIBLE
                isFadingOut = false
                isAnimating = false
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        startAnimation(alphaAnimation)
    }


//    override fun setVisibility(visibility: Int) {
//        val currentVisibility = getVisibility()
//        super.setVisibility(visibility)
//        if (visibility != currentVisibility) {
//            if (visibility == VISIBLE) {
//                startAnimation()
//            } else if (visibility == GONE || visibility == INVISIBLE) {
//                stopAnimation()
//            }
//        }
//    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        automaticUpdateSquareCountByMinCount(minSquaresCount)
    }

    private fun updateSquareSize() {
        squareWidth = measuredWidth / horizontalSquareCount
        squareHeight = measuredHeight / verticalSquareCount
        initSquaresShownList()
    }

    private fun initSquaresShownList() {
        squaresShownList.clear()
        for (i in 0 until (horizontalSquareCount * verticalSquareCount)) {
            squaresShownList.add(false)
        }
    }

    private fun updateSquareListNumbers() : Boolean{
        var changed = false
        squaresShownList.forEachIndexed { index, _ ->
            if (random.nextFloat() < 0.30) {
                changed = true
                squaresShownList[index] = !squaresShownList[index]
            }
        }
        return changed
    }

    fun startAnimation() {
        if (!isAnimating) {
            postDelayed(updateRunnable, updateInterval)

            if(useFadeInFadeOutAnimation) {
                fadeIn()
            } else {
                visibility = VISIBLE
                isAnimating = true
            }
        }
    }

    fun stopAnimation() {
        removeCallbacks(updateRunnable)
        if(useFadeInFadeOutAnimation) {
            fadeOut()
        } else {
            isAnimating = false
            visibility = INVISIBLE
        }
    }

    fun setSquareColor(color: Int) {
        squarePaint.color = color
        invalidate()
    }

    fun setNumberOfSquaresOnMinSide(count: Int) {
        this.minSquaresCount = count
        updateSquareSize()
        invalidate()
    }

    private fun automaticUpdateSquareCountByMinCount(minCount: Int){
        if(height > width) {
            horizontalSquareCount = minCount
            verticalSquareCount = floor(minCount * ((height*1f)/width)).toInt()
        }
        else if(width > height){
            verticalSquareCount = minCount
            horizontalSquareCount = floor(minCount * ((width*1f)/height)).toInt()
        }
        updateSquareSize()
        invalidate()
    }

    fun setHollowStyle(hollow: Boolean) {
        squarePaint.style = if (hollow) Paint.Style.STROKE else Paint.Style.FILL
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isAnimating) return

        squaresShownList.forEachIndexed { index, shown ->
            if (shown) {
                val x = index % horizontalSquareCount
                val y = index / horizontalSquareCount
                val left = x * squareWidth
                val top = y * squareHeight
                val right = left + squareWidth
                val bottom = top + squareHeight
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), squarePaint)
            }
        }
    }
}