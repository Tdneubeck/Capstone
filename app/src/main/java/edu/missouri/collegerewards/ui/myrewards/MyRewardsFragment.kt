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
import edu.missouri.collegerewards.data.SingletonData
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
        rewardAdapter = RewardAdapter(emptyList(), requireContext())
        recyclerView.adapter = rewardAdapter

        val rewardTiles = SingletonData.shared.rewardsList.sortedBy { it.cost }.map { reward ->
            val title = reward.title
            val cost = reward.cost
            val imgUrl = reward.imgUrl
            RewardTile(imgUrl, title, cost)
        }

        recyclerView.adapter = RewardAdapter(rewardTiles, requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
