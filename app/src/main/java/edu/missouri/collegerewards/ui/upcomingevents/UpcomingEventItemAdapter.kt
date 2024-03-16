package edu.missouri.collegerewards.ui.upcomingevents

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import edu.missouri.collegerewards.R


class UpcomingEventItemAdapter(private val context: Context, private val dataset: List<UpcomingEvent>)
    : RecyclerView.Adapter<UpcomingEventItemAdapter.UpcomingEventItemViewHolder>() {

    class UpcomingEventItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val titleTextView: TextView = view.findViewById(R.id.upcoming_event_item_title)
        val locationTextView: TextView = view.findViewById(R.id.upcoming_event_item_location)
        val dateTextView: TextView = view.findViewById(R.id.upcoming_event_item_date)
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
        holder.titleTextView.text = context.resources.getString(item.titleStringResourceID)
        holder.locationTextView.text = context.resources.getString(item.locationStringResourceID)
        holder.dateTextView.text = context.resources.getString(item.eventDateStringResourceID)
        holder.imageView.setImageResource(item.imageResourceID)

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            // Handle item click event
            // check permissions, Retrieve device location and display it
            if (hasLocationPermission(context)) {
                Toast.makeText(context, "Location Permission ON", Toast.LENGTH_SHORT).show()
                fetchDeviceLocation(context)
            } else {
                requestLocationPermission(context)
                Toast.makeText(context, "Location Permission OFF", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    //check location permissions, check location
    private fun fetchDeviceLocation(context: Context) {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle the case where location permissions are not granted
            // You can request permissions here
            return
        }
        // Create a FusedLocationProviderClient
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Get the last known location of the device
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    // Display the device's current location
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val locationText = "Latitude: $latitude, Longitude: $longitude"
                    Toast.makeText(context, locationText, Toast.LENGTH_LONG).show()
                } else {
                    // Handle the case where the last location is null
                }
            }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

}


