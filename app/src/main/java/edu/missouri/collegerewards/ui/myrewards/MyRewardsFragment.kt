package edu.missouri.collegerewards.ui.myrewards

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.databinding.FragmentMyRewardsBinding
import edu.missouri.collegerewards.ui.home.HomeViewModel

class MyRewardsFragment : Fragment() {

    private var _binding: FragmentMyRewardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var rewardAdapter: RewardAdapter // Assuming you have a RewardAdapter class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val MyRewardsViewModel=
            ViewModelProvider(this)[MyRewardsViewModel::class.java]
        _binding = FragmentMyRewardsBinding.inflate(inflater, container, false)

        val textView: TextView = binding.rewardpointcount
        MyRewardsViewModel._text.observe(viewLifecycleOwner){
            textView.text = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.RewardRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        rewardAdapter = RewardAdapter(emptyList()) // Initialize with empty list initially
        recyclerView.adapter = rewardAdapter


        val rewardsList = mutableListOf<RewardTile>()

        // Assuming you have a reference to your Firebase database
        val databaseReference = FirebaseDatabase.getInstance().getReference("rewards")
        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            for (snapshot in dataSnapshot.children) {
                val reward = snapshot.getValue(RewardTile::class.java)
                reward?.let {
                    rewardsList.add(it)
                }
            }
            Log.d("DataRetrieval", "Retrieved ${rewardsList.size} rewards")
            rewardAdapter.setData(rewardsList)
        }.addOnFailureListener { exception ->
            // Handle failure
            Log.e(TAG, "Error getting data", exception)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
