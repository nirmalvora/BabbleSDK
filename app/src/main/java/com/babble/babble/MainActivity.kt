package com.babble.babble

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.babble.babble.databinding.ActivityMainBinding
import com.babble.babblesdk.BabbleSDK

internal class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        BabbleSDK.init(this, "xyz123")
        binding.openSurvey.setOnClickListener {
            BabbleSDK.triggerSurvey("1mrazt7auzo")
        }
    }
}