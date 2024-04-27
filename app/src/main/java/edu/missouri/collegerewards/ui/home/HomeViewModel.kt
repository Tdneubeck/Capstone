package edu.missouri.collegerewards.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.datatransport.Event
import edu.missouri.collegerewards.data.SingletonData

class HomeViewModel : ViewModel() {

    private val point_text = MutableLiveData<String>().apply {
        value = "${SingletonData.shared.currentUser.points} points"
    }
    val pointtext: LiveData<String> = point_text
    private val nextreward_text = MutableLiveData<String>().apply {
        value = "5 points till Next reward"
    }
    val nextrewardtext: LiveData<String> = nextreward_text

    private val user_text = MutableLiveData<String>().apply {
        value = "Hello, ${SingletonData.shared.currentUser.name.split(" ")[0]}"
    }
    val usertext: LiveData<String> = user_text


}