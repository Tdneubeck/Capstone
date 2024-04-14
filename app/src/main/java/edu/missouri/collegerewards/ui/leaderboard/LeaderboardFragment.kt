package edu.missouri.collegerewards.ui.leaderboard

import LeaderboardAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        recyclerView = binding.LeaderboardRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // Get User DB from firebase
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("Users")

        usersRef.get().addOnSuccessListener { documents ->
            // Sort documents by score in descending order
            val sortedDocuments = documents.sortedByDescending { it.getLong("points") ?: 0 }

            // Map sorted documents to LeaderboardTile objects with correct index
            val leaderboardTiles = sortedDocuments.mapIndexedNotNull { index, document ->
                val username = document.getString("name")
                val score = document.getLong("points") // Assuming points are stored as Long, adjust as needed

                // Check if both username and score are not null
                if (username != null && score != null) {
                    LeaderboardTile(index + 1, username, score.toInt()) // Convert score to Int if needed
                } else {
                    null // Return null if either username or score is null
                }
            }

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
