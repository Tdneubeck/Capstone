package edu.missouri.collegerewards.ui.leaderboard

import LeaderboardAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.missouri.collegerewards.databinding.FragmentLeaderboardBinding

class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.LeaderboardRecyclerView // Update this id to your RecyclerView id
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // Get User DB from firebase
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("Users")

        usersRef.get().addOnSuccessListener { documents ->
            val leaderboardTiles = documents.mapNotNull { document ->
                val username = document.getString("name") // Make sure the field name matches with Firestore
                username?.let { LeaderboardTile(0, it, 0) } // Assuming rank and score are not used for now
            }.sortedBy { it.username } //TODO Sort by username; need to replace with Score or Point total

            recyclerView.adapter = LeaderboardAdapter(leaderboardTiles, requireContext())
        }.addOnFailureListener {
            //for error handling
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
