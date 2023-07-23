package roman.senatorov.puzzleprogress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SquareView: View {
    private var squareLength: Int = 100
    private var squareColor: Int = resources.getColor(R.color.grey)
    private lateinit var squarePaint: Paint

    private var isHollowSquare: Boolean = false
    private var strokeWidth : Int = 50

    constructor(context: Context) : super(context) {}

    constructor(context: Context, squareColor: Int, length: Int) : super(context) {
        this.squareColor = squareColor
        this.squareLength = length
        initValues()
    }

    constructor(context: Context,length: Int, squareColor: Int, isHollow: Boolean, strokeWidth : Int) : super(context) {
        this.squareLength = length
        this.squareColor = squareColor
        this.isHollowSquare = isHollow
        this.strokeWidth = strokeWidth
        initValues()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(attrs!!)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs!!)
    }

    fun initAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareView, 0, 0)

        this.squareLength = typedArray.getDimension(R.styleable.SquareView_squareLength,
            100f).toInt()
        this.squareColor = typedArray.getColor(R.styleable.SquareView_squareColor,
            resources.getColor(R.color.grey))
        typedArray.recycle()
        initValues()
    }

    private fun initValues() {
        squarePaint = Paint()
        squarePaint.color = squareColor
        if (isHollowSquare) {
            squarePaint.style = Paint.Style.STROKE
            squarePaint.strokeWidth = strokeWidth.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(squareLength, squareLength)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, squareLength.toFloat(), squareLength.toFloat(), squarePaint)
    }
}