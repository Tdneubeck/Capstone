package edu.missouri.collegerewards.data

import androidx.lifecycle.MutableLiveData
import edu.missouri.collegerewards.objects.Event
import edu.missouri.collegerewards.objects.Reward
import edu.missouri.collegerewards.objects.User

class SingletonData private constructor() {
    companion object {
        val shared = SingletonData()
    }

    lateinit var currentUser: User

    //livedata for user points
    var userPoints: MutableLiveData<Int> = MutableLiveData()

    var rewardsList: MutableList<Reward> = mutableListOf()
    var eventsList: MutableList<Event> = mutableListOf()

    //updates user points, called on startup and when reward is redeemed
    fun updatePoints(newPoints: Int){
        userPoints.postValue(newPoints)
    }
    fun clear() {

    }
}