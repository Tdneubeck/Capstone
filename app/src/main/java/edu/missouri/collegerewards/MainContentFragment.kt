package edu.missouri.collegerewards

import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.missouri.collegerewards.databinding.MainContentFragmentBinding
import edu.missouri.collegerewards.util.NavigationType
import edu.missouri.collegerewards.util.Navigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date


class MainContentFragment: Fragment(R.layout.main_content_fragment) {

    companion object {
        fun newInstance() = MainContentFragment()
    }


    private var _binding: MainContentFragmentBinding? = null
    private val binding get() = _binding!!

    // List of Fragments that don't show bottomNav bar
    private var noBottomNav = listOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainContentFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.contentFragmentNavHost) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(noBottomNav.contains(destination.id)) {
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE
            }
        }


        lifecycleScope.launch {
            Navigator.navigateState.collectLatest { state ->
                state.contentDestination?.let {
                    try {
                        if (state.bundle != null) {
                            if (state.navOptions != null) {
                                navController.navigate(it, state.bundle, state.navOptions)
                            } else {
                                navController.navigate(it, state.bundle)
                            }
                        } else {
                            if (state.navOptions != null) {
                                navController.navigate(it, null, state.navOptions)
                            } else {
                                navController.navigate(it)
                            }
                        }
                        // Reset the nav destination so we can use back btn
                        Navigator.navigate(NavigationType.Content, null)
                    } catch (e: Exception) {
                        Log.e("Skipping Nav", "Double Nav prevented")
                        Log.e("Error", e.toString())
                    }
                }
                state.doPop?.let {
                    navController.popBackStack()
                    Navigator.popBackStack(null)
                }
            }
        }


    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}