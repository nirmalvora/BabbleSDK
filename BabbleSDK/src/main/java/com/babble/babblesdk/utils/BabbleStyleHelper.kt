package com.babble.babblesdk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.babble.babblesdk.BabbleSDKController
import com.babble.babblesdk.R
import com.babble.babblesdk.TAG

class BabbleStyleHelper(activity: Context) {
    private var activity: Context? = activity

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: BabbleStyleHelper
        fun init(activity: Context) {
            instance = BabbleStyleHelper(activity)
        }

        fun setTextStyle(textView: TextView, isSelected: Boolean = false) {
            val textColor: Int =
                if (isSelected) Color.parseColor("#ffffff") else BabbleSDKController.getInstance(
                    instance.activity!!
                )!!.textColor
            if (isSelected) {
                textView.setTypeface(null, Typeface.BOLD)
            } else {
                textView.setTypeface(null, Typeface.NORMAL)
            }
            textView.setTextColor(
                textColor
            )
        }

        fun setLightTextColor(textView: TextView) {
            textView.setTextColor(
                BabbleSDKController.getInstance(
                    instance.activity!!
                )!!.textColorLight
            )
        }  fun setEditTextColor(textView: EditText) {
            textView.setTextColor(
                BabbleSDKController.getInstance(
                    instance.activity!!
                )!!.textColor
            )
        }

        fun setOptionSelected(gd: GradientDrawable, textView: TextView, isSelected: Boolean = false,selectedBackgroundColor: Int? = null) {
            val textColor: Int =
                if (isSelected) Color.parseColor("#ffffff") else BabbleSDKController.getInstance(
                    instance.activity!!
                )!!.textColor

            val backgroundColor: Int =
                selectedBackgroundColor
                    ?: if (isSelected) {
                        BabbleSDKController.getInstance(
                            instance.activity!!
                        )!!.themeColor
                    }else {
                        BabbleSDKController.getInstance(
                            instance.activity!!
                        )!!.optionBackgroundColor
                    }
            if (isSelected) {
                textView.setTypeface(null, Typeface.BOLD)
            } else {
                textView.setTypeface(null, Typeface.NORMAL)
            }

            gd.setColor(
                backgroundColor
            )
            textView.setTextColor(
                textColor
            )
        }

        fun submitButtonBeautification(nextButton: Button) {
            try {

                val stateListDrawable = StateListDrawable()

                val pressedButtonDrawable = AppCompatResources.getDrawable(
                    instance.activity!!, R.drawable.btn_rounded_rectangle_pressed
                )
                val pressedButton = DrawableCompat.wrap(pressedButtonDrawable!!)
                DrawableCompat.setTint(
                    pressedButton, BabbleSdkHelper.manipulateColor(
                        BabbleSDKController.getInstance(
                            instance.activity!!
                        )!!.themeColor, 0.5f
                    )
                )
                stateListDrawable.addState(
                    intArrayOf(android.R.attr.state_pressed), pressedButton
                )


                val focusedButtonDrawable = AppCompatResources.getDrawable(
                    instance.activity!!, R.drawable.btn_rounded_rectangle_focused
                )
                val focusedButton = DrawableCompat.wrap(focusedButtonDrawable!!)
                DrawableCompat.setTint(
                    focusedButton, BabbleSdkHelper.manipulateColor(
                        BabbleSDKController.getInstance(
                            instance.activity!!
                        )!!.themeColor, 0.5f
                    )
                )
                stateListDrawable.addState(
                    intArrayOf(android.R.attr.state_focused), focusedButton
                )


                val disableButtonDrawable = AppCompatResources.getDrawable(
                    instance.activity!!, R.drawable.btn_rounded_rectangle_disable
                )
                val disableButton = DrawableCompat.wrap(disableButtonDrawable!!)
                DrawableCompat.setTint(
                    disableButton, BabbleSdkHelper.manipulateColor(
                        BabbleSDKController.getInstance(
                            instance.activity!!
                        )!!.themeColor, 0.5f
                    )
                )
                stateListDrawable.addState(
                    intArrayOf(-android.R.attr.state_enabled), disableButton
                )

                val darkButtonDrawable = AppCompatResources.getDrawable(
                    instance.activity!!, R.drawable.btn_rounded_rectangle_normal
                )
                val darkColor = DrawableCompat.wrap(darkButtonDrawable!!)
                DrawableCompat.setTint(
                    darkColor, BabbleSDKController.getInstance(
                        instance.activity!!
                    )!!.themeColor
                )

                stateListDrawable.addState(intArrayOf(), darkColor)

                nextButton.background = stateListDrawable
            } catch (ex: Exception) {
                Log.e(TAG, "Button color fail")
            }
        }
    }


}