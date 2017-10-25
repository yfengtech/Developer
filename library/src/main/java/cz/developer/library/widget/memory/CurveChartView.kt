package cz.developer.library.widget.memory

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import cz.developer.library.R


open class CurveChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    protected var config: Config=Config()
    private val dataItem = mutableListOf<Float>()
    private var linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var graduatedLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var linePath= Path()
    private var fillPath= Path()
    private var yMaxValue=0f
    private var yMinValue=0f

    private val yLenPerValue: Float
        get() = (height - paddingBottom - paddingTop) / (this.yMaxValue - this.yMinValue)

    private val xLenPerCount: Float
        get() = (width-paddingTop-paddingBottom) / (this.config.dataSize - 1).toFloat()

    init {
        val typedArray: TypedArray
        val a = context.obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme)
        if (a.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowActionBar)) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CurveChartView, R.attr.curveChartView, R.style.CurveChartViewCompat)
        } else {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CurveChartView, R.attr.curveChartView, R.style.CurveChartView)
        }
        a.recycle()
        setColor(typedArray.getColor(R.styleable.CurveChartView_cv_color,0))
        setFillColor(typedArray.getColor(R.styleable.CurveChartView_cv_fillColor,0))
        setLabelTextSize(typedArray.getDimensionPixelSize(R.styleable.CurveChartView_cv_labelTextSize,0))
        setStrokeWidth(typedArray.getDimension(R.styleable.CurveChartView_cv_strokeWidth,0f))
        setPartCount(typedArray.getInteger(R.styleable.CurveChartView_cv_partCount,0))
        typedArray.recycle()
    }


    private fun setColor(color: Int) {
        val color=Color.argb(0x44,Color.red(color),Color.green(color),Color.blue(color))
        linePaint.color = color
        graduatedLinePaint.color = color
        textPaint.color = color
    }

    private fun setFillColor(color: Int) {
        fillPaint.color = Color.argb(0x22,Color.red(color),Color.green(color),Color.blue(color))
    }

    private fun setLabelTextSize(textSize: Int) {
        textPaint.textSize = textSize*1f
    }

    private fun setStrokeWidth(strokeWidth: Float) {
        this.linePaint.style = Paint.Style.STROKE
        this.linePaint.strokeWidth = strokeWidth
        this.graduatedLinePaint.strokeWidth = strokeWidth
    }

    private fun setPartCount(count: Int) {
        this.config.partCount =count
    }

    fun addData(data: Float) {
        this.dataItem.add(data)
        if (this.dataItem.size > this.config.dataSize) {
            this.dataItem.removeAt(0)
        }
        this.prepareData()
        this.postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.drawXY(canvas)
        this.drawScaleLabel(canvas)
        if(!dataItem.isEmpty()){
            this.drawLine(canvas, dataItem.size)
        }
    }

    private fun drawXY(canvas: Canvas) {
        canvas.drawLine(paddingLeft.toFloat(), paddingTop.toFloat(), paddingLeft.toFloat(), height - paddingBottom*1f, linePaint)
        canvas.drawLine(paddingLeft.toFloat(),
                height - paddingBottom*1f,
                width - paddingRight*1f,
                height - paddingBottom*1f, linePaint)
    }

    private fun drawScaleLabel(canvas: Canvas) {
        val yIntervalLen = (height - paddingBottom - paddingTop) / (this.config.partCount - 1)
        for (i in 0..this.config.partCount - 1) {
            val scaleY = height - paddingBottom*1f - yIntervalLen * i
            canvas.drawLine(paddingLeft.toFloat(), scaleY, (this.width - this.paddingRight).toFloat(), scaleY, graduatedLinePaint)
            val label = String.format("%.1fM",(this.yMaxValue - this.yMinValue) * i*1f / (config.partCount - 1) + this.yMinValue)
            canvas.drawText(label, this.paddingLeft.toFloat(), scaleY, textPaint)
        }

    }

    private fun drawLine(canvas: Canvas, size: Int) {
        this.linePath.reset()
        this.fillPath.reset()
        this.linePath.moveTo(paddingLeft.toFloat(), height - paddingBottom*1f - (this.dataItem[0] - this.yMinValue) * this.yLenPerValue)
        this.fillPath.moveTo(paddingLeft.toFloat(), height - paddingBottom*1f)
        this.fillPath.lineTo(paddingLeft.toFloat(), height - paddingBottom*1f - (this.dataItem[0] - this.yMinValue) * this.yLenPerValue)

        for (i in 1..size - 1) {
            val value = this.dataItem[i]
            this.linePath.lineTo(paddingLeft.toFloat() + i.toFloat() * this.xLenPerCount, height - paddingBottom*1f - (value - this.yMinValue) * this.yLenPerValue)
            this.fillPath.lineTo(paddingLeft.toFloat() + i.toFloat() * this.xLenPerCount, height - paddingBottom*1f - (value - this.yMinValue) * this.yLenPerValue)
        }

        this.fillPath.lineTo(paddingLeft.toFloat() + (size - 1).toFloat() * this.xLenPerCount, height - paddingBottom*1f)
        this.fillPath.close()
        canvas.drawPath(this.linePath, linePaint)
        canvas.drawPath(this.fillPath, fillPaint)
    }

    private fun prepareData() {
        var maxValue = Float.MIN_VALUE
        var minValue = Float.MAX_VALUE
        val size = this.dataItem.size

        for (i in 0..size - 1) {
            val v = this.dataItem[i]
            if (v > maxValue) {
                maxValue = v
            }
            if (v < minValue) {
                minValue = v
            }
        }

        this.yMaxValue = config.maxValueMulti * maxValue
        this.yMinValue = config.minValueMulti * minValue
    }


    inner class Config {
        var maxValueMulti: Float =  1.2f
        var minValueMulti: Float = 0.8f
        var partCount: Int = 5
        var dataSize: Int = 30
    }

}
