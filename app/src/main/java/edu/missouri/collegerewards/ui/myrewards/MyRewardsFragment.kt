package edu.missouri.collegerewards.ui.myrewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.missouri.collegerewards.databinding.FragmentMyRewardsBinding

class MyRewardsFragment : Fragment() {

    private var _binding: FragmentMyRewardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var rewardAdapter: RewardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val myRewardsViewModel=
            ViewModelProvider(this)[MyRewardsViewModel::class.java]
        _binding = FragmentMyRewardsBinding.inflate(inflater, container, false)

        val textView: TextView = binding.rewardpointcount
        myRewardsViewModel._text.observe(viewLifecycleOwner){
            textView.text = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.RewardRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        rewardAdapter = RewardAdapter(emptyList(), requireContext()) // Initialize with empty list initially
        recyclerView.adapter = rewardAdapter

        val db = FirebaseFirestore.getInstance()
        val rewardRef = db.collection("Rewards")

        rewardRef.get().addOnSuccessListener {documents ->
            // Sort documents by score in descending order
            val sortedDocuments = documents.sortedBy { it.getLong("cost")?: Long.MAX_VALUE }

            // Map sorted documents to LeaderboardTile objects with correct index
            val rewardTiles = sortedDocuments.mapNotNull { document ->
                val title = document.getString("title")
                val cost = document.getLong("cost") // Assuming points are stored as Long, adjust as needed
                val imgUrl = document.getString("imgUrl")
                // Check if both username and score are not null
                if (title != null && cost != null && imgUrl != null) {
                    RewardTile(imgUrl, title, cost.toInt()) // Convert score to Int if needed
                } else {
                    null // Return null if either username or score is null
                }
            }

            recyclerView.adapter = RewardAdapter(rewardTiles, requireContext())
        }.addOnFailureListener {
            //for error handling
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
