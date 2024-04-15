package edu.missouri.collegerewards.data

import edu.missouri.collegerewards.objects.Event
import edu.missouri.collegerewards.objects.Reward
import edu.missouri.collegerewards.objects.User

class SingletonData private constructor() {
    companion object {
        val shared = SingletonData()
    }

    lateinit var currentUser: User

    var rewardsList: MutableList<Reward> = mutableListOf()
    var eventsList: MutableList<Event> = mutableListOf()

    fun clear() {

    }
}