package edu.missouri.collegerewards.data

import edu.missouri.collegerewards.objects.User

class SingletonData private constructor() {
    companion object {
        val shared = SingletonData()
    }

    lateinit var currentUser: User

    fun clear() {

    }
}