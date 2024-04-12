package edu.missouri.collegerewards.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.databinding.FragmentHomeBinding
import edu.missouri.collegerewards.ui.upcomingevents.UpcomingEventDataSource
import edu.missouri.collegerewards.ui.upcomingevents.UpcomingEventItemAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var logoutButton: Button



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val textView: TextView = binding.homepointcount
        homeViewModel.pointtext.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val usertext: TextView = binding.userhello
        homeViewModel.usertext.observe(viewLifecycleOwner) {
            usertext.text = it
        }
        recyclerView = binding.Nexteventhomeview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val myDataSet = UpcomingEventDataSource().loadUpcomingEvents()
        recyclerView.adapter = UpcomingEventItemAdapter(requireContext(), myDataSet)

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




