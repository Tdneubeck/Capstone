
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import edu.missouri.collegerewards.databinding.FragmentAdminBinding
import edu.missouri.collegerewards.ui.admin.AdminAdapter
import edu.missouri.collegerewards.ui.admin.AdminViewModel



class AdminFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adminAdapter: AdminAdapter

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val AdminViewModel=
            ViewModelProvider(this)[AdminViewModel::class.java]
        _binding = FragmentAdminBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase


        // Initialize RecyclerView and its adapter
        adminAdapter = AdminAdapter(emptyList(), requireContext())
        recyclerView = binding.AdminEventsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter=adminAdapter

        // Set click listener for the "Add Event" button
        addEventButton.setOnClickListener {
            addEvent()
        }

        // Load events into RecyclerView
        loadEvents()
    }

    private fun addEvent() {
        val eventName = event_name.text.toString()
        val eventDate = event_date.text.toString()
        val eventLocation = event_location.text.toString()

        // Assuming you have a data class Event
        val event = Event(eventName, eventDate, eventLocation)
        val eventKey = database.child("events").push().key
        if (eventKey != null) {
            database.child("events").child(eventKey).setValue(event)
            // Clear EditTexts after adding event
            event_name.text.clear()
            event_date.text.clear()
            event_location.text.clear()
        }
    }

    private fun loadEvents() {
        // Listen for changes in the Firebase database under "events" node
        database.child("events").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventsList = mutableListOf<Event>()
                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java)
                    event?.let {
                        eventsList.add(it)
                    }
                }
                eventAdapter.submitList(eventsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}


