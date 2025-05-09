package com.example.bachatbox.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bachatbox.R
import com.example.bachatbox.databinding.FragmentSplashBinding
import com.example.bachatbox.view.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        binding.startBtn.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_userInfoFragment)
        }

        return binding.root
    }
}