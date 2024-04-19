package edu.missouri.collegerewards.ui.myrewards

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.data.SingletonData

class RewardAdapter(private val context: Context?, private var rewards: List<RewardTile>) : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reward_tile_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reward = rewards[position]

        // Load image using Glide library (make sure to include Glide in your dependencies)
        Glide.with(holder.itemView)
            .load(reward.imageUrl)
            .into(holder.imageView)

        holder.nameTextView.text = reward.title
        holder.pointCountTextView.text = "${reward.cost} points"
        holder.redeemButton.setOnClickListener {
            // Subtract the cost of the reward from the user's points
            if (SingletonData.shared.currentUser.points >= reward.cost) {
                val newPoints = SingletonData.shared.currentUser.points - reward.cost

// Reference the location where the points are stored in the Firebase database
                val currentUserPointsRef = FirebaseDatabase.getInstance().getReference("Users/${SingletonData.shared.currentUser.uid}/points")

// Update the points value in the database
                currentUserPointsRef.setValue(newPoints)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        // The points value was successfully updated
                        // You can perform any additional actions here, if needed
                    }
                    .addOnFailureListener { e ->
                        // There was an error updating the points value
                        Log.e(TAG, "Error updating points value: ${e.message}")
                    }
            }
            else{
                Toast.makeText(context, "Insufficient points", Toast.LENGTH_SHORT).show()
            }
            // Optionally, you can perform any additional actions here
            // For example, update the UI to reflect the new points value
        }
    }

    override fun getItemCount(): Int {
        return rewards.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val pointCountTextView: TextView = itemView.findViewById(R.id.pointsTextView)
        val redeemButton: Button = itemView.findViewById(R.id.redeemButton)

    }
}