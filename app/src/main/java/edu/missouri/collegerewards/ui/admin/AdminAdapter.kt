package edu.missouri.collegerewards.ui.admin

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

class AdminAdapter(private var events: List<AdminTile>, requireContext: Context) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_tile_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]

        // Load image using Glide library (make sure to include Glide in your dependencies)
        Glide.with(holder.itemView)
            .load(event.imgUrl)
            .into(holder.imageView)

        holder.nameTextView.text = event.Title
        holder.locationTextView.text = event.Location
        holder.dateTextView.text = event.Date.toString()
        holder.codeTextView.text = event.Rewards_Code
        //holder.redeemButton.setOnClickListener {
        // Handle button click event here
        // You can implement redemption logic or any other action you want
        //}
    }

    override fun getItemCount(): Int {
        return events.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.admin_event_item_image)
        val nameTextView: TextView = itemView.findViewById(R.id.admin_event_item_title)
        val locationTextView: TextView = itemView.findViewById(R.id.admin_event_item_location)
        val dateTextView: TextView = itemView.findViewById(R.id.admin_event_item_date)
        val codeTextView: TextView = itemView.findViewById(R.id.admin_event_item_code)
        val redeemButton: Button = itemView.findViewById(R.id.redeemButton)
    }
}