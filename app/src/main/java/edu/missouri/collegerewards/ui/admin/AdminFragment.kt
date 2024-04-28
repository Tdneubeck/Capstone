package edu.missouri.collegerewards.ui.admin

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.databinding.FragmentAdminBinding
import edu.missouri.collegerewards.objects.Event
import android.location.Address
import android.location.Geocoder


class AdminFragment : Fragment() {
    private var _binding: FragmentAdminBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminAdapter: AdminAdapter
    private lateinit var AddEventButton:Button
    private lateinit var editEventButton: Button
    private lateinit var title: EditText
    private lateinit var location: EditText
    private lateinit var date: EditText
    private lateinit var imgUrl: EditText
    private lateinit var points: EditText



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adminViewModel =
            ViewModelProvider(this)[AdminViewModel::class.java]

        _binding = FragmentAdminBinding.inflate(inflater, container, false)

    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AddEventButton = binding.AddEventButton
        title = binding.eventName
        location=binding.eventLocation
        date = binding.eventDate
        imgUrl = binding.eventImage
        points =binding.eventPoints




        recyclerView = binding.AdminEventsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adminAdapter = AdminAdapter(emptyList(), requireContext())
        recyclerView.adapter = adminAdapter

        val adminTiles = SingletonData.shared.eventsList.sortedBy { it.title }.map { event ->
            val title = event.title
            val location = event.location
            val imgUrl = event.imgUrl
            val date = event.date
            val code = event.code
            AdminTile(imgUrl, title, location,date,code)

        }

        recyclerView.adapter = AdminAdapter(adminTiles, requireContext())

        AddEventButton.setOnClickListener {
            val titleText = title.text.toString()
            val locationText = location.text.toString()
            val dateText = date.text.toString()
            val imgUrlText = imgUrl.text.toString()
            val points = points.text.toString().toInt()


            if (titleText.isNotBlank() && locationText.isNotBlank() && dateText.isNotBlank() && imgUrlText.isNotBlank() && points.toString().isNotBlank()) {

                val eventRef = Firebase.firestore.collection("Events").document()

                val eventData = hashMapOf(
                    "title" to titleText,
                    "location" to locationText,
                    "date" to dateText,
                    "imgUrl" to imgUrlText,
                    "points" to points
                    // Add any additional fields here
                )
                val newEvent= Event(eventData)

                eventRef.set(eventData)
                    .addOnSuccessListener {
                        // Handle success
                        Log.d(TAG, "Event added successfully")

                        SingletonData.shared.eventsList.add(0, newEvent)

                        // Notify the adapter that the dataset has changed
                        adminAdapter.notifyItemInserted(0)

                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        Log.w(TAG, "Error adding event", e)
                    }


            } else {
                // Handle case when any field is blank
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

