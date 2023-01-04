package com.babble.babble

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.babble.babble.databinding.ActivityMainBinding
import com.babble.babblesdk.BabbleSDK
import com.babble.babblesdk.BabbleSDKController


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        BabbleSDK.init(this, "xyz123")

        binding.openSurvey.setOnClickListener(View.OnClickListener {
            BabbleSDK.triggerSurvey("new_test_trigger");
        })
    }
}