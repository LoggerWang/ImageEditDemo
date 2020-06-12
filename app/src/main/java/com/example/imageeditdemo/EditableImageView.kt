package com.example.imageeditdemo

import android.content.Context
import android.graphics.*
import android.graphics.Paint.DITHER_FLAG
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.DrawableRes

/**
 * @author wanglezhi
 * @date   2020/6/11 10:43
 * @discription
 */
class EditableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr)  {

    companion object{
        /**涂鸦模式*/
        const val MODE_DOODLE = 0
        /**马赛克*/
        const val MODE_MOSK = 1
    }

    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    private var bitmapPaint:Paint ?=null
    private var mTempPaint:Paint ?=null

    private var mTemplePath:Path ?=null

    /**
     * 画笔的颜色
     */
    private val mPaintColor = Color.RED

    /**
     * 画笔的粗细
     */
    private val mPaintWidth: Int = 33
    /**原始bitmap*/
    var originalBitmap:Bitmap ?=null


    private var noodlePathList: MutableList<PathBean> = mutableListOf<PathBean>()

    /**设置原始bitmap--通过 */
    fun setOriginalViewById(@DrawableRes id:Int){
        this.originalBitmap = BitmapFactory.decodeResource(resources, id)
        initOriginBitmap()
    }
    /**设置原始bitmap--通过Url */
    fun setOriginalViewByUrl(url:String){

    }
    /**设置原始bitmap--通过Bitmap */
    fun setOriginalViewByBitmap(bitmap: Bitmap){
        this.originalBitmap = bitmap
        initOriginBitmap()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            mViewWidth = w
            mViewHeight = h
            initOriginBitmap()
        }
    }

    private fun initOriginBitmap() {
        if (originalBitmap!=null && mViewWidth>0 && mViewHeight>0) {
            originalBitmap = Bitmap.createScaledBitmap(originalBitmap!!,mViewWidth,mViewHeight,true)
            bitmapPaint = Paint(DITHER_FLAG)
            setMode()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mViewHeight==0 || mViewWidth==0) {
            return
        }
        if (originalBitmap!=null) {
            canvas?.drawBitmap(originalBitmap!!,0f,0f,bitmapPaint)
        }

        if (mTemplePath!=null) {
            canvas?.drawPath(mTemplePath!!,mTempPaint!!)
        }
        noodlePathList.forEach {
            canvas?.drawPath(it.path,it.paint)
        }
    }

    fun setMode(){
        mTempPaint = Paint()
        mTempPaint!!.run {
            isAntiAlias = true
            color = mPaintColor
            style = Paint.Style.STROKE
            strokeWidth = mPaintWidth.toFloat()
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

    }

    var startX = 0f
    var startY = 0f
    var moveX = 0f
    var moveY = 0f



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        moveX = event?.x!!
        moveY = event.y!!
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = moveX
                startY = moveY
                mTemplePath = Path()
                mTemplePath!!.moveTo(startX,startY)
                setMode()
            }
            MotionEvent.ACTION_MOVE -> {
                mTemplePath!!.lineTo(moveX,moveY)
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                var pathBean = PathBean(mTemplePath!!, mTempPaint!!, 0)
                noodlePathList.add(pathBean)

                mTemplePath = null
                mTempPaint = null
            }
            else -> {
            }
        }

        return true
    }
}