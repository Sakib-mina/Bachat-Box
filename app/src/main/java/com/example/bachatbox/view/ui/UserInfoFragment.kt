package com.example.bachatbox.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bachatbox.R
import com.example.bachatbox.data.model.User
import com.example.bachatbox.databinding.FragmentUserInfoBinding
import com.example.bachatbox.view.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        userLogin()
        return binding.root
    }

    private fun userLogin() {
        binding.letsGoBtn.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val totalBalanceText = binding.totalBalance.text.toString().trim()
            val incomePMText = binding.incomePerM.text.toString().trim()

            var isValid = true

            // Name validation
            if (name.isEmpty()) {
                binding.userName.error = "Name is required"
                isValid = false
            } else if (name.length < 3) {
                binding.userName.error = "Name must be at least 3 characters"
                isValid = false
            }

            // Total Balance validation
            if (totalBalanceText.isEmpty()) {
                binding.totalBalance.error = "Balance is required"
                isValid = false
            } else {
                try {
                    val balance = totalBalanceText.toDouble()
                    if (balance <= 0) {
                        binding.totalBalance.error = "Balance must be greater than 0"
                        isValid = false
                    }
                } catch (e: NumberFormatException) {
                    binding.totalBalance.error = "Enter a valid number"
                    isValid = false
                }
            }

            // Income Per Month validation
            if (incomePMText.isEmpty()) {
                binding.incomePerM.error = "Monthly income is required"
                isValid = false
            } else {
                try {
                    val income = incomePMText.toDouble()
                    if (income <= 0) {
                        binding.incomePerM.error = "Income must be greater than 1"
                        isValid = false
                    }
                } catch (e: NumberFormatException) {
                    binding.incomePerM.error = "Enter a valid number"
                    isValid = false
                }
            }

            // If all valid, proceed to next screen
            if (isValid) {
                val bundle = Bundle().apply {
                    putSerializable(
                        "user",
                        User(
                            name = name,
                            totalBalance = totalBalanceText.toDouble(),
                            incomePerM = incomePMText.toDouble()
                        )
                    )
                }
                findNavController().navigate(
                    R.id.action_userInfoFragment_to_dashboardFragment,
                    bundle
                )
            }
        }
    }
}