package com.babble.babblesdk.customWidgets

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

internal class BabbleDynamicSquare : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    // here we are returning the width in place of height, so width = height
    // you may modify further to create any proportion you like ie. height = 2*width etc
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)

        //int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }
}