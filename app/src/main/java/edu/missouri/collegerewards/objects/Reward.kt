package edu.missouri.collegerewards.objects

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class Reward {

    var title: String = ""
    var image: StorageReference? = null
    var cost: Int = 0

    companion object {
        fun loadRewards(completion: (MutableList<Reward>) -> Unit) {
            val database = Firebase.firestore.collection("Rewards")
            val rewardsList = mutableListOf<Reward>()

            database.get().addOnSuccessListener {
                if (!it.isEmpty) {
                    it.documents.forEach { documentSnapshot ->
                        val rewardObject = Reward(documentSnapshot.data!!)
                        rewardsList.add(rewardObject)
                    }
                }
                completion(rewardsList)
            }.addOnFailureListener {
                completion(rewardsList)
            }
        }
    }

    constructor(data: Map<String, Any>) {
        this.title = data["title"] as? String ?: ""
        this.cost = (data["cost"] as? Long)?.toInt() ?: 0
        this.loadImage()
    }

    fun loadImage() {

    }

}