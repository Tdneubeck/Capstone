package edu.missouri.collegerewards.objects

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Event {

    var id: String = ""
    var date: String =""
    var code: String= ""
    var title: String = ""
    var imgUrl: String = ""
    var locationName: String= ""
    var lat: Double = 0.0
    var lng: Double = 0.0
    var points: Int = 0

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
        this.id = data["id"] as? String ?: ""
        this.title = data["title"] as? String ?: ""
        this.imgUrl = data["imgUrl"] as? String ?: ""
        this.locationName= data["locationName"] as? String ?: ""
        this.date = data["date"] as? String ?: ""
        this.code =data["code"] as? String ?: ""
        this.lat = (data["lat"] as? Long)?.toDouble() ?: data["lat"] as? Double ?: 0.0
        this.lng = (data["lng"] as? Long)?.toDouble() ?: data["lng"] as? Double ?: 0.0
        this.points = (data["points"] as? Long)?.toInt() ?: 0
    }


}