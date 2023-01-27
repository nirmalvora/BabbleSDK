package com.babble.babblesdk.utils

import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.util.Log
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import com.babble.babblesdk.BabbleSDKController
import com.babble.babblesdk.R
import com.babble.babblesdk.TAG
import java.text.SimpleDateFormat
import java.util.*

internal object BabbleSdkHelper {
    fun initializationFailed() {
        Log.e(TAG, "Initialization failed")
    }

    fun notInitialized() {
        Log.e(TAG, "Babble sdk not Initialized")
    }

    fun surveyNotFoundForTrigger() {
        Log.e(TAG, "Survey not found for specified trigger")
    }

    fun matchingCohortNotFound() {
        Log.e(TAG, "No matching cohorts")
    }

    fun matchingEventsNotFound() {
        Log.e(TAG, "No matching events")
    }


    fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault()).format(
            Date()
        )
    }

    fun convertStringToDate(date:String?): Date? {
        return date?.let {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault()).parse(
                it
            )
        }
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getIdFromStringPath(string: String?): String? {
        return string?.substring(
            string.lastIndexOf('/')
                .plus(1)
        )

    }

    fun manipulateColor(color: Int, factor: Float): Int {
        var factorTemp = factor
        factorTemp = 1.0f - factorTemp
        return ColorUtils.blendARGB(color, Color.WHITE, factorTemp)
    }


    fun submitButtonBeautification(requireActivity: FragmentActivity, nextButton: Button) {
        try {

            val stateListDrawable = StateListDrawable()

            val pressedButtonDrawable = AppCompatResources.getDrawable(
                requireActivity, R.drawable.btn_rounded_rectangle_pressed)
            val pressedButton = DrawableCompat.wrap(pressedButtonDrawable!!)
            DrawableCompat.setTint(pressedButton,
                manipulateColor(Color.parseColor(BabbleSDKController.getInstance(
                    requireActivity)!!.themeColor), 0.5f))
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed),
                pressedButton)


            val focusedButtonDrawable = AppCompatResources.getDrawable(
                requireActivity, R.drawable.btn_rounded_rectangle_focused)
            val focusedButton = DrawableCompat.wrap(focusedButtonDrawable!!)
            DrawableCompat.setTint(focusedButton,
                manipulateColor(Color.parseColor(BabbleSDKController.getInstance(
                    requireActivity)!!.themeColor), 0.5f))
            stateListDrawable.addState(intArrayOf(android.R.attr.state_focused),
                focusedButton)


            val disableButtonDrawable = AppCompatResources.getDrawable(
                requireActivity, R.drawable.btn_rounded_rectangle_disable)
            val disableButton = DrawableCompat.wrap(disableButtonDrawable!!)
            DrawableCompat.setTint(disableButton,
                manipulateColor(Color.parseColor(BabbleSDKController.getInstance(
                    requireActivity)!!.themeColor), 0.5f))
            stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled),
                disableButton)

            val darkButtonDrawable = AppCompatResources.getDrawable(
                requireActivity, R.drawable.btn_rounded_rectangle_normal)
            val darkColor = DrawableCompat.wrap(darkButtonDrawable!!)
            DrawableCompat.setTint(darkColor,
                Color.parseColor(BabbleSDKController.getInstance(
                    requireActivity)!!.themeColor))

            stateListDrawable.addState(intArrayOf(), darkColor)

            nextButton.background = stateListDrawable
        } catch (ex: Exception) {
            Log.e(TAG, "Button color fail" )
        }
    }
}