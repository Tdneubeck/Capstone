package edu.missouri.collegerewards.ui.myrewards

import android.content.Context
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.objects.Reward
import edu.missouri.collegerewards.objects.User

class RewardAdapter(private var rewards: List<RewardTile>, private val context: Context) : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    val currentUser = SingletonData.shared.currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reward_tile_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reward = rewards[position]

        // Load image using Glide library
        Glide.with(holder.itemView)
            .load(reward.imageUrl)
            .into(holder.imageView)

        holder.nameTextView.text = reward.title
        holder.pointCountTextView.text = "${reward.cost} points"

        holder.redeemButton.setOnClickListener {
            //val points = reward.cost
            redeemReward(currentUser, reward)
        }
    }

    //redeem rewards
    private fun redeemReward(user: User, reward: RewardTile){
        if (user.points >= reward.cost) {
            val newPoints = user.points - reward.cost

            // Update points in Firestore
            val userRef = Firebase.firestore.collection("Users").document(user.uid)
            userRef.update("points", newPoints)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully claimed ${reward.title}", Toast.LENGTH_LONG).show()
                    user.points = newPoints  // Update the user object in SingletonData
                    SingletonData.shared.currentUser = user
                    SingletonData.shared.updatePoints(newPoints)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to update points: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Not enough points to claim this reward", Toast.LENGTH_SHORT).show()
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