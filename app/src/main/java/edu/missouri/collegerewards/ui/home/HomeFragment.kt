package edu.missouri.collegerewards.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import edu.missouri.collegerewards.data.SingletonData
import edu.missouri.collegerewards.databinding.FragmentHomeBinding
import edu.missouri.collegerewards.ui.upcomingevents.UpcomingEventItemAdapter
import edu.missouri.collegerewards.objects.User


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null



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

        val textView: TextView = binding.homepointcount

        SingletonData.shared.userPoints.observe(viewLifecycleOwner, Observer { points ->
            textView.text = getString(edu.missouri.collegerewards.R.string.point_count, points)
        })

        val usertext: TextView = binding.userhello
        homeViewModel.usertext.observe(viewLifecycleOwner) {
            usertext.text = it
        }
        recyclerView = binding.Nexteventhomeview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        UpcomingEventItemAdapter(requireContext(), SingletonData.shared.eventsList)

        binding.logoutButton.setOnClickListener {
            User.logOut()
        }
        //initial update user points
        SingletonData.shared.updatePoints(SingletonData.shared.currentUser.points)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




