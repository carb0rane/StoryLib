package com.carb0rane.storylib

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import androidx.core.content.IntentCompat
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.carb0rane.storylib.databinding.ActivityStoryPlayerBinding


private const val TAG = "SlideShowActivity"

class SlideShowActivity : Activity(), PlaybackListener {
    private lateinit var binding: ActivityStoryPlayerBinding

    private var imagesList = arrayListOf<Story>()

    //var owner = "Carb0rane"
    private lateinit var linearStoryProgressIndicator: LinearStoryProgressIndicator
    private var primaryColor: Int = 0xFF007A //  bright blue
    private var secondaryColor: Int = 0xFF38A3 //  teal blue
    private var barBackgroundColor: Int = 0xFFFFFF //  white
    private var displayTimeTextColor: Int = 0xFEFEFE
    private var ownerTextColor: Int = 0x433443
    private var duration: Int = 5000
    private var barHeight: Int = 3

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        linearStoryProgressIndicator = binding.customBar

        imagesList =
            IntentCompat.getParcelableArrayListExtra(
                intent,
                R.string.story_url_intent_tag.toString(),
                Story::class.java
            )!!
        val receivedIntent = intent
        val receivedPrimaryColor = receivedIntent.getIntExtra("primaryColor", primaryColor)
        val receivedSecondaryColor = receivedIntent.getIntExtra("secondaryColor", secondaryColor)
     //   val receivedBarBackgroundColor = receivedIntent.getIntExtra("barBackgroundColor", barBackgroundColor)
        val receivedDuration = receivedIntent.getIntExtra("duration", duration)
        val receivedBarHeight = receivedIntent.getIntExtra("barHeight", barHeight)
        val receivedOwnerTextColor = receivedIntent.getIntExtra("ownerTextColor", ownerTextColor)
        val receivedDisplayTimeTextColor =
            receivedIntent.getIntExtra("displayTimeTextColor", displayTimeTextColor)


        linearStoryProgressIndicator.progressBarHeight = receivedBarHeight.toFloat()
        linearStoryProgressIndicator.primaryColor = receivedPrimaryColor
        linearStoryProgressIndicator.secondaryColor = receivedSecondaryColor
        linearStoryProgressIndicator.individualStoryDisplayDuration = receivedDuration.toLong()
        binding.storyImage.setOnTouchListener(OnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_UP -> {

                   linearStoryProgressIndicator.resumeAnimation()
                    true
                }
                MotionEvent.ACTION_DOWN -> {

                    linearStoryProgressIndicator.pauseAnimation()
                    true
                }
                else -> {false}
            }
        })
       // linearStoryProgressIndicator.setBackgroundColor(receivedBarBackgroundColor)
        binding.storyOwnerName.setTextColor(receivedOwnerTextColor)
        binding.storyTimeElapsed.setTextColor(receivedDisplayTimeTextColor)



        linearStoryProgressIndicator.setListener(this)
        linearStoryProgressIndicator.setProgressBarSize(imagesList.size)



    }


    private fun loadImage(index: Int) {
        //  implement caching !!!
        binding.storyOwnerName.text = imagesList[index].storyOwnerName
        binding.storyTimeElapsed.text = imagesList[index].storyTime

        val imgVew = ImageLoader.Builder(this).allowHardware(false).crossfade(true).build()
        val imgReq = ImageRequest.Builder(this).data(imagesList[index].storyImageURL)
            .target(
                onStart = {
                    linearStoryProgressIndicator.pauseAnimation()

                },
                onSuccess = { result ->
                    binding.storyImage.load(result)
                    linearStoryProgressIndicator.resumeAnimation()

                },
                onError = {
                            // display some deault image or something in case an error occurs
                }
            ).build()

        imgVew.enqueue(imgReq)


    }

    override fun onStartedPlaying(index: Int) {
        loadImage(index)

    }

    override fun onFinishedPlaying() {
        finish()
    }

    override fun onPause() {
        super.onPause()
        linearStoryProgressIndicator.cancelAnimation()
    }
}

