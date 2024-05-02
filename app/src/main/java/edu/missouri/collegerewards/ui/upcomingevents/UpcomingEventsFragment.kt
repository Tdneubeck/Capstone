package edu.missouri.collegerewards.ui.upcomingevents

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.databinding.FragmentUpcomingEventsBinding
import edu.missouri.collegerewards.objects.Event

class UpcomingEventsFragment : Fragment() {

    private var _binding: FragmentUpcomingEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private var location: Location? = null
    private var client: FusedLocationProviderClient? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.upcomingEventsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            Log.d("NO PERMS", "PERMISSIONS NOT GRANTED!")
        }

        recyclerView.adapter = UpcomingEventItemAdapter(requireContext(), SingletonData.shared.eventsList, object : UpcomingEventItemAdapter.EventInteractionListener {
            override fun onCheckInClicked(event: Event) {
                Log.e("LAT", event.lat.toString())
                Log.e("LNG", event.lng.toString())
                val distance = (GeoFireUtils.getDistanceBetween(
                    GeoLocation(event.lat, event.lng),
                    GeoLocation(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                ) / 1609)

                Log.e("Distance", distance.toString())

                if (SingletonData.shared.currentUser.checkedInEvents.contains(event.id)) {
                    // Yell at em
                    Toast.makeText(context, "You are already checked in to this event.", Toast.LENGTH_LONG).show()
                } else {
                    if (distance < 0.8) {
                        // CHECK EM IN!
                        AlertDialog.Builder(context)
                            .setTitle("Check-In Confirmation")
                            .setMessage("You are close enough to the event. Do you want to check in?")
                            .setPositiveButton("Check In") { dialog, which ->
                                SingletonData.shared.currentUser.checkInToEvent(event)
                                Toast.makeText(context, "Checked in successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    } else {
                        // Alert too far!
                        Toast.makeText(context, "You are too far from this event. Go to the event location and check in!", Toast.LENGTH_LONG).show()
                    }
                }
            }

        })

        recyclerView.setHasFixedSize(true)

        return root
    }

    private fun getLocation() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            val locationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val newLocation = locationResult.lastLocation
                    if (newLocation != null) {
                        this@UpcomingEventsFragment.location = newLocation
                    }
                }
            }

            val locationRequest = LocationRequest.Builder(10000)
                .setMaxUpdates(1)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

            client!!.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            // Location is disabled!
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}