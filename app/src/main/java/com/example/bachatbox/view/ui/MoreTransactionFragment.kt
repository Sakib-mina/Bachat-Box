package com.example.bachatbox.view.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachatbox.data.model.Transaction
import com.example.bachatbox.databinding.FragmentMoreTransactionBinding
import com.example.bachatbox.view.adapter.TransactionAdapter
import com.example.bachatbox.view.viewModels.TransactionViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MoreTransactionFragment : Fragment() {
    private lateinit var binding: FragmentMoreTransactionBinding
    private lateinit var adapter: TransactionAdapter
    private val viewModels: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreTransactionBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = Color.TRANSPARENT

        setupSpinner()
        setupRecyclerView()
        observeAllTransactions()
        observeData()

        binding.apply {
            earnBtn.setOnClickListener {
                viewModels.getTransactionsByType("Earn").observe(viewLifecycleOwner) { list ->
                    adapter.updateList(list.reversed())
                }
            }
            spendBtn.setOnClickListener {
                viewModels.getTransactionsByType("Spend").observe(viewLifecycleOwner) { list ->
                    adapter.updateList(list.reversed())
                }
            }
            All.setOnClickListener {
                observeAllTransactions()
            }
        }
        return binding.root
    }

    private fun setupSpinner() {
        val months = listOf(
            "All", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.monthlySpinner.adapter = spinnerAdapter

        binding.monthlySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                viewModels.getTransactionsByMonth(position).observe(viewLifecycleOwner) { list ->
                    adapter.updateList(list.reversed())
                    updatePieChart(list)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(emptyList())
        binding.moreTransactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.moreTransactionRecyclerView.adapter = adapter
    }

    private fun observeAllTransactions() {
        viewModels.getAllTransaction.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list.reversed())
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

    private fun updatePieChart(list: List<Transaction>) {
        val totalEarn = list.filter { it.type == "Earn" }.sumOf { it.amount }
        val totalSpend = list.filter { it.type == "Spend" }.sumOf { it.amount }
        setupPieChart(totalEarn.toInt(), totalSpend.toInt())
    }
}
