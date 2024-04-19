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

class AdminAdapter(private var adminEvents: List<AdminTile>, requireContext: Context) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_tile_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = adminEvents[position]

        // Load image using Glide library (make sure to include Glide in your dependencies)
        Glide.with(holder.itemView)
            .load(event.imgUrl)
            .into(holder.imageView)

        holder.title.text = event.title
        holder.location.text = event.location
        holder.date.text = event.date
        holder.code.text = event.rewardcode
        //holder.redeemButton.setOnClickListener {
        // Handle button click event here
        // You can implement redemption logic or any other action you want
        //}
    }

    override fun getItemCount(): Int {
        return adminEvents.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.admin_event_item_image)
        val title: TextView = itemView.findViewById(R.id.admin_event_item_title)
        val location: TextView = itemView.findViewById(R.id.admin_event_item_location)
        val date: Button = itemView.findViewById(R.id.admin_event_item_date)
        val code: TextView = itemView.findViewById(R.id.admin_event_item_code)
    }
}