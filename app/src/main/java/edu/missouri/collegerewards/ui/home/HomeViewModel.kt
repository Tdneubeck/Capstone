package edu.missouri.collegerewards.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val pointText = MutableLiveData<String>().apply {
        value = "55 points"
    }
    val pointtext: LiveData<String> = pointText
    private val nextrewardText = MutableLiveData<String>().apply {
        value = "5 points till Next reward"
    }
    val nextrewardtext: LiveData<String> = nextrewardText
    private val userText = MutableLiveData<String>().apply {
        value = "Hello, John"
    }
    val usertext: LiveData<String> = userText
}