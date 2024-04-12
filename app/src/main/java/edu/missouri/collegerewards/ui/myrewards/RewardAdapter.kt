package edu.missouri.collegerewards.ui.myrewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.missouri.collegerewards.R
import com.squareup.picasso.Picasso


class RewardAdapter(private var rewards: List<RewardTile>) : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val costTextView: TextView = itemView.findViewById(R.id.pointsTextView)
        val redeemButton: Button = itemView.findViewById(R.id.redeemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reward_tile_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reward = rewards[position]
        // Bind data to views
        holder.titleTextView.text = reward.title
        holder.costTextView.text = "Points: ${reward.cost}"
        // Load image using Picasso, Glide, or any other image loading library
        Picasso.get().load(reward.imageUrl).into(holder.imageView)
        // Set click listener for redeem button
        holder.redeemButton.setOnClickListener {
            // Handle button click event
            // You can implement your logic here
        }
    }

    override fun getItemCount(): Int {
        return rewards.size
    }

    fun setData(newRewards: List<RewardTile>) {
        rewards = newRewards
    }
}
