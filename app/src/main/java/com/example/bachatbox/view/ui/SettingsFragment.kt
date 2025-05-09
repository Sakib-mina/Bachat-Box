package com.example.bachatbox.view.ui


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bachatbox.R
import com.example.bachatbox.databinding.FragmentSettingsBinding
import com.example.bachatbox.view.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.apply {
            aboutUs.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_aboutUsFragment)
            }
            updateInfo.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_updateInfoFragment)
            }
            logOut.setOnClickListener {
                showLogoutDialog()
            }
        }
        return binding.root
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                userViewModel.logoutUser()

                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT)
                    .show()

                findNavController().navigate(R.id.action_settingsFragment_to_userInfoFragment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
