package edu.missouri.collegerewards.ui.upcomingevents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.missouri.collegerewards.R

class UpcomingEventItemAdapter(private val context: Context, private val dataset: List<UpcomingEvent>)
    : RecyclerView.Adapter<UpcomingEventItemAdapter.UpcomingEventItemViewHolder>() {

    class UpcomingEventItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.upcoming_event_item_title)
        val imageView: ImageView = view.findViewById(R.id.upcoming_event_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.upcoming_event_item, parent, false)
        return UpcomingEventItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: UpcomingEventItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = context.resources.getString(item.stringResourceID)
        holder.imageView.setImageResource(item.imageResourceID)
    }
}