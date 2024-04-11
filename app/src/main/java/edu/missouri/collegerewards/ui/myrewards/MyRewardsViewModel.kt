package edu.missouri.collegerewards.ui.myrewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.missouri.collegerewards.data.SingletonData

class MyRewardsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "${SingletonData.shared.currentUser.points} points"
    }
    val text: LiveData<String> = _text
}