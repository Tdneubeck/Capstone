package edu.missouri.collegerewards.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LeaderboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Leaderboard"
    }
    val text: LiveData<String> = _text
}