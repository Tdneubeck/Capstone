package edu.missouri.collegerewards.ui.upcomingevents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpcomingEventsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Upcoming Events Fragment"
    }
    val text: LiveData<String> = _text
}