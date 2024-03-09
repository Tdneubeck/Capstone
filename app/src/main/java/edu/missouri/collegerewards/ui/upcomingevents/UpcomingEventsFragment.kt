package edu.missouri.collegerewards.ui.upcomingevents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.databinding.FragmentUpcomingEventsBinding

class UpcomingEventsFragment : Fragment() {

    private var _binding: FragmentUpcomingEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.upcomingEventsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val myDataSet = UpcomingEventDataSource().loadUpcomingEvents()
        recyclerView.adapter = UpcomingEventItemAdapter(requireContext(), myDataSet)

        recyclerView.setHasFixedSize(true)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}