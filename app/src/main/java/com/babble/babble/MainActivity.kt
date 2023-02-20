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
        val mapValue: HashMap<String, Any?> = HashMap()
        mapValue.put("testKey1","testValue")
        mapValue.put("testKey2","testValue")
        mapValue.put("testKey3","testValue")
        BabbleSDK.setCustomerId(customerId = "cust007", userDetails = mapValue)
        binding.openSurvey.setOnClickListener {
            BabbleSDK.triggerSurvey(trigger = "test3", properties = mapValue)
        }
    }
}