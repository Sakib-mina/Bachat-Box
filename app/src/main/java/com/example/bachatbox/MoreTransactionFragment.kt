package com.example.bachatbox

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.bachatbox.databinding.FragmentMoreTransactionBinding
import com.example.bachatbox.view.adapter.TransactionAdapter
import com.example.bachatbox.view.viewModels.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MoreTransactionFragment : Fragment() {

    private lateinit var binding: FragmentMoreTransactionBinding
    private lateinit var adapter: TransactionAdapter
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreTransactionBinding.inflate(inflater,container,false)

        viewModel.getAllTransaction.observe(viewLifecycleOwner) { moreTransactions ->
            binding.moreTransactionRecyclerView.adapter = TransactionAdapter(moreTransactions)
            binding.moreTransactionRecyclerView.adapter = adapter
        }
        return binding.root
    }
}