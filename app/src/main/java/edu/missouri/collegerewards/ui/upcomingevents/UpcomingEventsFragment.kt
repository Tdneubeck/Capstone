package edu.missouri.collegerewards.ui.upcomingevents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.databinding.FragmentUpcomingEventsBinding

class UpcomingEventsFragment : Fragment() {

    private var _binding: FragmentUpcomingEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarView: CalendarView

    private lateinit var switchViewButton: Button

    private var isCalendarViewVisible = false



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val db = FirebaseFirestore.getInstance()
        val eventsRef = db.collection("Events")


        recyclerView = binding.upcomingEventsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = UpcomingEventItemAdapter(requireContext(), eventsList)
        recyclerView.setHasFixedSize(true)


        eventsRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val event = document.toObject<Event>(Event::class.java)
                    eventsList.add(event)
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }


        calendarView = binding.upcomingEventsCalendarView
        calendarView.visibility = View.GONE

        switchViewButton = binding.eventButton
        switchViewButton.setOnClickListener{
            toggleView()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toggleView() {
        if (isCalendarViewVisible) {
            calendarView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            switchViewButton.text = getString(R.string.switch_to_calendar)
        } else {
            recyclerView.visibility = View.GONE
            calendarView.visibility = View.VISIBLE
            switchViewButton.text = getString(R.string.switch_to_list)
        }
        isCalendarViewVisible = !isCalendarViewVisible
    }
}

class FragmentUpcomingEventsBinding {

}
