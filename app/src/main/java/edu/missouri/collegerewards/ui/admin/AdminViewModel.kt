package edu.missouri.collegerewards.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.missouri.collegerewards.data.SingletonData

class AdminViewModel {
    private val user_text = MutableLiveData<String>().apply {
        value = "Hello, ${SingletonData.shared.currentUser.name.split(" ")[0]}"
    }
    val usertext: LiveData<String> = user_text
}
