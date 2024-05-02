package edu.missouri.collegerewards.ui.upcomingevents

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.LocationServices
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.objects.Event


class UpcomingEventItemAdapter(private val context: Context, private val dataset: List<Event>, private val eventListener: EventInteractionListener)
    : RecyclerView.Adapter<UpcomingEventItemAdapter.UpcomingEventItemViewHolder>() {

        interface EventInteractionListener {
            fun onCheckInClicked(event: Event)
        }

    class UpcomingEventItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val titleTextView: TextView = view.findViewById(R.id.upcoming_event_item_title)
        val locationTextView: TextView = view.findViewById(R.id.upcoming_event_item_location)
        val dateTextView: TextView = view.findViewById(R.id.upcoming_event_item_date)
        val imageView: ImageView = view.findViewById(R.id.upcoming_event_item_image)
        val checkInButton: Button = view.findViewById(R.id.check_in_button)
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
        Glide.with(holder.itemView)
            .load(item.imgUrl)
            .into(holder.imageView)
        holder.titleTextView.text = item.title
        holder.locationTextView.text = item.locationName
        holder.dateTextView.text = item.date
        holder.checkInButton.setOnClickListener {
            eventListener.onCheckInClicked(item)
        }

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

}


