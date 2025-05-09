package com.example.bachatbox.view.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bachatbox.data.model.Transaction
import com.example.bachatbox.databinding.FragmentAddIncomeBinding
import com.example.bachatbox.view.viewModels.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddIncomeFragment : Fragment() {

    private lateinit var binding: FragmentAddIncomeBinding
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionType: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddIncomeBinding.inflate(inflater, container, false)

        transactionType = arguments?.getString("type") ?: "Earn"

        setupSpinner()
        setupInput()
        setupDatePicker()

        return binding.root
    }

    private fun setupSpinner() {
        val categoryList = listOf(
            "Travel",
            "Entertainment",
            "Real-Estate",
            "Shopping",
            "Food",
            "Transport",
            "Health",
            "Salary",
            "Gift",
            "Others"
        )
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.icCategory.adapter = adapter
    }

    private fun setupInput() {
        binding.add.setOnClickListener {
            val name = binding.icName.text.toString()
            val amount = binding.icAmount.text.toString().toDoubleOrNull() ?: 0.0
            val category = binding.icCategory.selectedItem.toString()
            val date = binding.icDate.text.toString()

            if (name.isNotEmpty() && date.isNotEmpty()) {
                val transaction = Transaction(
                    name = name,
                    amount = amount,
                    category = category,
                    transactionDate = date,
                    type = "Earn"
                )

                viewModel.insertTransaction(transaction)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupDatePicker() {
        binding.icDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "${selectedDay.toString().padStart(2, '0')}/" +
                            "${(selectedMonth + 1).toString().padStart(2, '0')}/" +
                            "$selectedYear"
                    binding.icDate.setText(selectedDate)
                }, year, month, day)

            datePicker.show()
        }
    }
}