package com.example.bachatbox.view.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bachatbox.R
import com.example.bachatbox.data.model.User
import com.example.bachatbox.databinding.FragmentUserInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = Color.TRANSPARENT

        userLogin()
        return binding.root
    }

    private fun userLogin() {
        binding.letsGoBtn.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val totalBalanceText = binding.totalBalance.text.toString().trim()
            val incomePMText = binding.incomePerM.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                binding.userName.error = "Name is required"
                isValid = false
            } else if (name.length < 3) {
                binding.userName.error = "Name must be at least 3 characters"
                isValid = false
            }

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