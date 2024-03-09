package edu.missouri.collegerewards.ui.myrewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyRewardsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is My Rewards Fragment"
    }
    val text: LiveData<String> = _text
}