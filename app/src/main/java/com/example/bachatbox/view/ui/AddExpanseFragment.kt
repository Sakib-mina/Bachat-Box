package com.example.bachatbox.view.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bachatbox.data.model.Transaction
import com.example.bachatbox.databinding.FragmentAddExpanseBinding
import com.example.bachatbox.view.viewModels.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import kotlin.getValue

@AndroidEntryPoint
class AddExpanseFragment : Fragment() {

    private lateinit var binding: FragmentAddExpanseBinding
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionType: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddExpanseBinding.inflate(inflater, container, false)

        // Get transaction type from arguments (default "Spend")
        transactionType = arguments?.getString("type") ?: "Spend"

        // Category list for Spinner
        val categoryList = listOf(
            "Travel", "Entertainment", "Real-Estate", "Shopping", "Food",
            "Transport", "Health", "Salary", "Gift", "Others"
        )

        // Set up the category spinner with the list
        setupSpinner(categoryList)

        // Set up the input fields and button click
        setupInput()

        setupDatePicker()

        return binding.root
    }

    // Set up the spinner with category list
    private fun setupSpinner(categoryList: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
    }

    // Set up the button click action for adding a transaction
    private fun setupInput() {
        binding.add.setOnClickListener {
            val name = binding.expName.text.toString()
            val amount = binding.icAmount.text.toString().toDoubleOrNull() ?: 0.0
            val category = binding.spinner.selectedItem.toString()
            val date = binding.expDate.text.toString()

            // Check if name and date are provided
            if (name.isNotEmpty() && date.isNotEmpty()) {
                val transaction = Transaction(
                    name = name,
                    amount = amount,
                    category = category,
                    transactionDate = date,
                    type = "Spend"// Use dynamic transaction type

                )

                // Insert transaction into database
                viewModel.insertTransaction(transaction)

                // Navigate back to previous fragment
                findNavController().popBackStack()

                // Show success message
                Toast.makeText(requireContext(), "Transaction added successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Show error if fields are not filled
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupDatePicker() {
        binding.expDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay.toString().padStart(2, '0')}/" +
                        "${(selectedMonth + 1).toString().padStart(2, '0')}/" +
                        "$selectedYear"
                binding.expDate.setText(selectedDate)
            }, year, month, day)

            datePicker.show()
        }
    }
}
