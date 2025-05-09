package com.example.bachatbox.view.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bachatbox.R
import com.example.bachatbox.data.model.User
import com.example.bachatbox.databinding.FragmentUpdateInfoBinding
import com.example.bachatbox.view.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class UpdateInfoFragment : Fragment() {

    private lateinit var binding: FragmentUpdateInfoBinding
    private val viewModel: UserViewModel by viewModels()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateInfoBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = Color.TRANSPARENT

        user = arguments?.getSerializable("user") as? User
        user?.let {
            binding.uName.setText(it.name)
            binding.upTotalBal.setText(it.totalBalance.toString())
            binding.upPerMonths.setText(it.incomePerM.toString())
        }

        updateInfo()
        return binding.root
    }

    private fun updateInfo() {
        binding.update.setOnClickListener {
            val updatedName = binding.uName.text.toString()
            val updatedBalance = binding.upTotalBal.text.toString().toDoubleOrNull() ?: 0.0
            val updatedIncome = binding.upPerMonths.text.toString().toDoubleOrNull() ?: 0.0

            user?.let {
                val updatedUser = User(
                    id = it.id,
                    name = updatedName,
                    totalBalance = updatedBalance,
                    incomePerM = updatedIncome
                )

                viewModel.update(updatedUser)
                val bundle = Bundle().apply {
                    putSerializable("user", updatedUser)
                }
                findNavController().navigate(
                    R.id.action_updateInfoFragment_to_userInfoFragment, bundle
                )
            }
        }
    }
}