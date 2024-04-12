package edu.missouri.collegerewards.ui.myrewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.missouri.collegerewards.data.SingletonData

class MyRewardsViewModel : ViewModel() {

    private val point_text = MutableLiveData<String>().apply {
        value = "${SingletonData.shared.currentUser.points} points"
    }
    val _text: LiveData<String> = point_text


}