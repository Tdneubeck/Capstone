package edu.missouri.collegerewards.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.databinding.FragmentAdminBinding


class AdminFragment : Fragment() {
    private var _binding: FragmentAdminBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminAdapter: AdminAdapter


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
    }
}

