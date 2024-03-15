package edu.missouri.collegerewards.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val point_text = MutableLiveData<String>().apply {
        value = "55 points"
    }
    val pointtext: LiveData<String> = point_text
    private val nextreward_text = MutableLiveData<String>().apply {
        value = "5 points till Next reward"
    }
    val nextrewardtext: LiveData<String> = nextreward_text
    private val user_text = MutableLiveData<String>().apply {
        value = "Hello, John"
    }
    val usertext: LiveData<String> = user_text
}