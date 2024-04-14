package edu.missouri.collegerewards.ui.myrewards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.missouri.collegerewards.R

class RewardAdapter(private var rewards: List<RewardTile>, requireContext: Context) : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {



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
        //holder.redeemButton.setOnClickListener {
            // Handle button click event here
            // You can implement redemption logic or any other action you want
        //}
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