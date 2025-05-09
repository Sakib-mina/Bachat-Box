package com.example.bachatbox.view.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachatbox.R
import com.example.bachatbox.databinding.FragmentDashboardBinding
import com.example.bachatbox.view.adapter.TransactionAdapter
import com.example.bachatbox.view.viewModels.TransactionViewModel
import com.example.bachatbox.view.viewModels.UserViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: TransactionAdapter
    private var isFabOpen = false
    private val viewModels: TransactionViewModel by viewModels()
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_settingsFragment)
        }

        setupRecyclerView()
        observeTransactions()
        setupFab()
        observeData()

        return binding.root
    }

    private fun setupFab() {
        binding.fabMain.setOnClickListener {
            if (isFabOpen) closeFabMenu() else openFabMenu()
        }

        binding.fabEarn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addIncomeFragment)
            closeFabMenu()
        }

        binding.fabSpend.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addExpanseFragment)
            closeFabMenu()
        }
    }

    private fun openFabMenu() {
        binding.fabEarn.visibility = View.VISIBLE
        binding.fabSpend.visibility = View.VISIBLE
        isFabOpen = true
    }

    private fun closeFabMenu() {
        binding.fabEarn.visibility = View.GONE
        binding.fabSpend.visibility = View.GONE
        isFabOpen = false
    }

    // 👇 Exit Dialog
    private fun showExitDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ -> requireActivity().finishAffinity() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // 👇 RecyclerView Setup
    private fun setupRecyclerView() {
        adapter = TransactionAdapter(emptyList())
        binding.RCV.layoutManager = LinearLayoutManager(requireContext())
        binding.RCV.adapter = adapter
    }

    // 👇 Transaction Data Observe
    private fun observeTransactions() {
        viewModels.getAllTransaction.observe(viewLifecycleOwner) { list ->
            adapter = TransactionAdapter(list)
            binding.RCV.adapter = adapter
        }
    }

    // 👇 Balance, Earn, Spend Observe and Pie Chart Setup
    private fun observeData() {
        viewModels.totalEarn.observe(viewLifecycleOwner) { earn ->
            val totalEarn = earn ?: 0
            val totalSpend = viewModels.totalSpend.value ?: 0
            val balance = totalEarn - totalSpend

            binding.earned.text = "$ $totalEarn"
            binding.avBalance.text = "$ $balance"

            setupPieChart(totalEarn, totalSpend, balance)
        }

        viewModels.totalSpend.observe(viewLifecycleOwner) { spend ->
            val totalEarn = viewModels.totalEarn.value ?: 0
            val totalSpend = spend ?: 0
            val balance = totalEarn - totalSpend

            binding.spend.text = "$ $totalSpend"
            binding.avBalance.text = "$ $balance"

            setupPieChart(totalEarn, totalSpend, balance)
        }

        viewModels.balance.observe(viewLifecycleOwner) {
            binding.avBalance.text = "$ ${it ?: 0}"
        }
    }

    // 👇 Pie Chart Function
    private fun setupPieChart(earn: Int, spend: Int, balance: Int) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(earn.toFloat(), "Earn"))
        entries.add(PieEntry(spend.toFloat(), "Spend"))

        val colors = listOf(
            Color.rgb(76, 175, 80),  // Green for Earn
            Color.rgb(244, 67, 54)   // Red for Spend
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors

        val data = PieData(dataSet)

        binding.pieChart.apply {
            this.data = data
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setHoleRadius(60f)
            setTransparentCircleRadius(65f)
            legend.isEnabled = false
            invalidate()
        }
    }
}
