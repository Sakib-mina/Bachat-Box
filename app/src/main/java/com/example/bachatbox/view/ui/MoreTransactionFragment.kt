package com.example.bachatbox.view.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachatbox.databinding.FragmentMoreTransactionBinding
import com.example.bachatbox.view.adapter.TransactionAdapter
import com.example.bachatbox.view.viewModels.TransactionViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MoreTransactionFragment : Fragment() {
    private lateinit var binding: FragmentMoreTransactionBinding
    private lateinit var adapter: TransactionAdapter
    private val viewModels: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreTransactionBinding.inflate(inflater, container, false)

        binding.apply {
            earnBtn.setOnClickListener {
                observeTransactionsByType("Earn")
            }
            spendBtn.setOnClickListener {
                observeTransactionsByType("Spend")
            }
            All.setOnClickListener {
                observeAllTransactions()
            }
        }

        setupRecyclerView()
        observeAllTransactions()
        observeData()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(emptyList())
        binding.moreTransactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.moreTransactionRecyclerView.adapter = adapter
    }

    private fun observeAllTransactions() {
        viewModels.getAllTransaction.observe(viewLifecycleOwner) { list ->
            adapter = TransactionAdapter(list.reversed())
            binding.moreTransactionRecyclerView.adapter = adapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModels.totalEarn.observe(viewLifecycleOwner) { earn ->
            val totalEarn = earn ?: 0
            val totalSpend = viewModels.totalSpend.value ?: 0

            binding.earned.text = "$ $totalEarn"
            setupPieChart(totalEarn, totalSpend)
        }

        viewModels.totalSpend.observe(viewLifecycleOwner) { spend ->
            val totalEarn = viewModels.totalEarn.value ?: 0
            val totalSpend = spend ?: 0

            binding.spend.text = "$ $totalSpend"
            setupPieChart(totalEarn, totalSpend)
        }
    }

    private fun setupPieChart(earn: Int, spend: Int) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(earn.toFloat(), "Earn"))
        entries.add(PieEntry(spend.toFloat(), "Spend"))

        val colors = listOf(
            Color.rgb(76, 175, 80),
            Color.rgb(244, 67, 54)
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors

        val data = PieData(dataSet)

        binding.pieChart.apply {
            this.data = data
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            holeRadius = 60f
            transparentCircleRadius = 65f
            legend.isEnabled = false
            invalidate()
        }
    }

    private fun observeTransactionsByType(type: String) {
        viewModels.getTransactionsByType(type).observe(viewLifecycleOwner) { list ->
            adapter = TransactionAdapter(list.reversed())
            binding.moreTransactionRecyclerView.adapter = adapter
        }
    }
}