package com.babble.babblesdk.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.babble.babblesdk.R
import com.babble.babblesdk.TAG
import com.babble.babblesdk.databinding.FragmentSurveyBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SurveyBottomSheetFragment(val babbleUserId: String?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSurveyBottomSheetBinding
    override fun getTheme(): Int = R.style.CustomBottomSheet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSurveyBottomSheetBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener(View.OnClickListener {
            Log.e(TAG, "onViewCreated: $babbleUserId", )
            dismiss()
        })
        binding.question.text="What new features would you like us to introduce?"

    }
}