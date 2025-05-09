package com.example.bachatbox.view.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachatbox.R
import com.example.bachatbox.data.model.User
import com.example.bachatbox.databinding.FragmentDashboardBinding
import com.example.bachatbox.view.adapter.TransactionAdapter
import com.example.bachatbox.view.viewModels.TransactionViewModel
import com.example.bachatbox.view.viewModels.UserViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: TransactionAdapter
    private var isFabOpen = false
    private val viewModels: TransactionViewModel by viewModels()
    private val viewModel: UserViewModel by viewModels()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = Color.TRANSPARENT
        user = arguments?.getSerializable("user") as? User
        user?.let {
            binding.userName.text = it.name
            viewModel.insert(it)
        }

        binding.apply {
            settings.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_settingsFragment)
            }
            viewMore.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_moreTransactionFragment)
            }
        }

        setupRecyclerView()
        observeTransactions()
        setupFab()
        observeData()

        return binding.root
    }

    private fun setupFab() {
        binding.apply {
            fabMain.setOnClickListener {
                if (isFabOpen) closeFabMenu() else openFabMenu()
            }
            fabEarn.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_addIncomeFragment)
                closeFabMenu()
            }
            fabSpend.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_addExpanseFragment)
                closeFabMenu()
            }
        }
    }

    private fun openFabMenu() {
        binding.apply {
            fabEarn.visibility = View.VISIBLE
            fabSpend.visibility = View.VISIBLE
            isFabOpen = true
        }
    }

    private fun closeFabMenu() {
        binding.apply {
            fabEarn.visibility = View.GONE
            fabSpend.visibility = View.GONE
            isFabOpen = false
        }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(emptyList())
        binding.RCV.layoutManager = LinearLayoutManager(requireContext())
        binding.RCV.adapter = adapter
    }

    private fun observeTransactions() {
        viewModels.getAllTransaction.observe(viewLifecycleOwner) { list ->
            val latestFive = if (list.size > 5) list.takeLast(5) else list
            adapter = TransactionAdapter(latestFive.reversed())
            binding.RCV.adapter = adapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModels.totalEarn.observe(viewLifecycleOwner) { earn ->
            val totalEarn = earn ?: 0
            val totalSpend = viewModels.totalSpend.value ?: 0
            val balance = totalEarn - totalSpend

            binding.earned.text = "$ $totalEarn"
            binding.avBalance.text = "$ $balance"

            setupPieChart(totalEarn, totalSpend)
        }

        viewModels.totalSpend.observe(viewLifecycleOwner) { spend ->
            val totalEarn = viewModels.totalEarn.value ?: 0
            val totalSpend = spend ?: 0
            val balance = totalEarn - totalSpend
            binding.spend.text = "$ $totalSpend"
            binding.avBalance.text = "$ $balance"

            setupPieChart(totalEarn, totalSpend)
        }

        viewModels.balance.observe(viewLifecycleOwner) {
            binding.avBalance.text = "$ ${it ?: 0}"
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

    override fun onResume() {
        super.onResume()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun showExitDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().finishAffinity()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
