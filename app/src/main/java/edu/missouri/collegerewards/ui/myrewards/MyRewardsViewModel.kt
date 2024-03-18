package edu.missouri.collegerewards.ui.myrewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyRewardsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "55 Points"
    }
    val text: LiveData<String> = _text
}