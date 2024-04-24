package edu.missouri.collegerewards.ui.home

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
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.databinding.FragmentHomeBinding
import edu.missouri.collegerewards.ui.upcomingevents.UpcomingEventItemAdapter
import edu.missouri.collegerewards.objects.User
import edu.missouri.collegerewards.util.NavigationType
import edu.missouri.collegerewards.util.Navigator


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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.Nexteventhomeview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = UpcomingEventItemAdapter(requireContext(), SingletonData.shared.eventsList)

        binding.logoutButton.setOnClickListener {
            User.logOut()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homepointcount.text = "${SingletonData.shared.currentUser.points} points"
        binding.userhello.text = "Hello, ${SingletonData.shared.currentUser.name.split(" ")[0]}"

        if (SingletonData.shared.currentUser.role) {
            binding.adminButton.visibility = View.VISIBLE
        } else {
            binding.adminButton.visibility = View.GONE
        }

        binding.adminButton.setOnClickListener {
            Navigator.navigate(NavigationType.Content, R.id.action_homeFragment_to_adminFragment)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




