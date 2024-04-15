package edu.missouri.collegerewards.objects

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class Event {

    var title: String = ""
    var imgUrl: String = ""

    companion object {
        fun loadEvents(completion: (MutableList<Event>) -> Unit) {
            val database = Firebase.firestore.collection("Events")
            val eventsList = mutableListOf<Event>()

            database.get().addOnSuccessListener {
                if (!it.isEmpty) {
                    it.documents.forEach { documentSnapshot ->
                        val eventObject = Event(documentSnapshot.data!!)
                        eventsList.add(eventObject)
                    }
                }
                completion(eventsList)
            }.addOnFailureListener {
                completion(eventsList)
            }
        }
    }

    constructor(data: Map<String, Any>) {
        this.title = data["title"] as? String ?: ""
        this.imgUrl = data["imgUrl"] as? String ?: ""
    }


}