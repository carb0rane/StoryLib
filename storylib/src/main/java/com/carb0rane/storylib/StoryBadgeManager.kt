package com.carb0rane.storylib

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.request.ImageRequest
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "StoryBadgeManager"

class StoryBadgeManager(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {


    private var rectSideInPx:Int? = 153
    private var arcWidthPx:Int? = 3
    private var seenArcColor:Int? = 0
    private var unseenArcColor:Int? = 0
    private lateinit var badgeImageRect: Rect
    private var imageBitmap: Bitmap? = null
    private val imagesList = arrayListOf<Story>()
    private var owner = "Carb0rane"
    private lateinit var storyPaintObj: Paint
    private var primaryColor: Int? = 0xFF007A //  bright blue
    private var secondaryColor: Int? = 0xFF38A3 //  teal blue
    private  var barBackgroundColor :Drawable?
    private var displayTimeTextColor: Int? = 0xFEFEFE
    private var ownerTextColor: Int? = 0x433443
    private var duration: Int? = 5000
    private var barHeight: Int? = 10
    private var radius = 2.24f



     init {
        val ta = context?.obtainStyledAttributes(
            attrs,
            R.styleable.StoryBadgeManager,
            0,
            0
        )
        try {
            arcWidthPx = ta?.getInt(
                R.styleable.StoryBadgeManager_bWidth,
                3
            )?.dpToPx(context)
            rectSideInPx = ta?.getInt(
                R.styleable.StoryBadgeManager_iRadius,
               150
            )?.dpToPx(context)
            unseenArcColor = ta?.getColor(
                R.styleable.StoryBadgeManager_storyUnVisitedColor,
                ContextCompat.getColor(context, R.color.storyUnVisitedColor)
            )
            seenArcColor = ta?.getColor(
                R.styleable.StoryBadgeManager_storyVisitedColor,
                ContextCompat.getColor(context, R.color.storyVisitedColor)
            )
            primaryColor = ta?.getColor(
                R.styleable.StoryBadgeManager_barPrimaryColor,
                ContextCompat.getColor(context, R.color.progressBarPrimaryColor)
            )
            secondaryColor = ta?.getColor(
                R.styleable.StoryBadgeManager_barSecondaryColor,
                ContextCompat.getColor(context, R.color.progressBarSecondaryColor)

            )
            barBackgroundColor = ta?.getDrawable(
                R.styleable.StoryBadgeManager_barBackgroundColor
            )


            duration = ta?.getInt(
                R.styleable.StoryBadgeManager_barStoryPlaybackDuration,
                3000
            )

            barHeight = ta?.getInt(
                R.styleable.StoryBadgeManager_barHeight,
                15
            )
            ownerTextColor = ta?.getColor(
                R.styleable.StoryBadgeManager_barOwnerTextColor,
                ContextCompat.getColor(context, R.color.progressBarSecondaryColor)

            )
            displayTimeTextColor = ta?.getColor(
                R.styleable.StoryBadgeManager_barDisplayTimeTextColor,
                ContextCompat.getColor(context, R.color.progressBarSecondaryColor)

            )


        } finally {
            ta?.recycle()
            setValues()
        }
    }

    private fun storyPlayer() {
        val intnt = Intent(context, SlideShowActivity::class.java)
        intnt.putExtra("duration", duration)
        intnt.putExtra("barHeight", barHeight)
        intnt.putExtra("primaryColor", primaryColor)
        intnt.putExtra("secondaryColor", secondaryColor)
       // intnt.putExtra("barBackgroundColor", barBackgroundColor)
        intnt.putExtra("ownerTextColor", ownerTextColor)
        intnt.putExtra("displayTimeTextColor", displayTimeTextColor)
        intnt.putParcelableArrayListExtra(R.string.story_url_intent_tag.toString(), imagesList)
        context.startActivity(intnt)

    }



    private fun setValues() {
        if (context != null) {


            storyPaintObj = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND

            }
            // storyURLs = mutableListOf()
            badgeImageRect = Rect(
                0,
                0,
                rectSideInPx!!,
                rectSideInPx!!
            )

        }
    }

    fun setStoryOwner(owner: String) {
        this.owner = owner
    }
    fun setRadius(rad:Float){
        this.radius = rad
    }

    fun setStoriesList(vararg url: String) {
        for (u in url) {
            imagesList.add(Story(u, owner, SimpleDateFormat("hh:mm a").format(Date())))
        }
        if (imagesList.size == url.size) {
            loadTheIconForStory()
        } else {
            invalidate()
        }

    }

    private fun loadTheIconForStory() {
        val imgLoader = ImageLoader.Builder(context).allowHardware(false).build()
        val req = ImageRequest.Builder(context)
            .data(imagesList[0].storyImageURL).target {

                    result ->
                imageBitmap = (result as BitmapDrawable?)?.bitmap

                invalidate()
            }
            .build()


        imgLoader.enqueue(req)


    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                storyPlayer()
            }
        }
        return true

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        storyPaintObj.color = unseenArcColor!!
        storyPaintObj.strokeWidth = arcWidthPx!!.toFloat()
        var startAngle = 1.0
        val sweepAngle = (360 - (imagesList.size * 6)) / imagesList.size.toFloat()
        for (i in 0 until imagesList.size) {

            canvas.drawArc(
                arcWidthPx!!.toFloat(),
                arcWidthPx!!.toFloat(),
                rectSideInPx!!.toFloat()-arcWidthPx!!.toFloat(),
                rectSideInPx!!.toFloat()-arcWidthPx!!.toFloat(),
                startAngle.toFloat(),
                sweepAngle,
                false,
                storyPaintObj
            )
            startAngle += sweepAngle + 6.0
        }





        if (imageBitmap != null) {
            val path = Path()
            path.addCircle(
                badgeImageRect.centerX().toFloat(),
                badgeImageRect.centerY().toFloat(),
                badgeImageRect.width() / radius, // ye 2.24 achha laga to rehne diye
                Path.Direction.CW
            )
            canvas.clipPath(path)
            canvas.drawBitmap(imageBitmap!!, null, badgeImageRect, null)


        }


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = paddingStart + paddingEnd + rectSideInPx!!
        val height = paddingTop + paddingBottom + rectSideInPx!!
        val w = resolveSizeAndState(width, widthMeasureSpec, 0)
        val h = resolveSizeAndState(height, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)


    }

    private fun Int.dpToPx(context: Context?): Int {
        val metrics = context?.resources?.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
            .toInt()
    }

}