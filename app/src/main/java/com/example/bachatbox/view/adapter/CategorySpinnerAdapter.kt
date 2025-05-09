package com.example.bachatbox.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.bachatbox.data.model.Category
import com.example.bachatbox.databinding.ItemSpinnerCategoryBinding

class CategorySpinnerAdapter(
    private val context: Context,
    private val categories: List<Category>
) : BaseAdapter() {

    override fun getCount() = categories.size
    override fun getItem(position: Int) = categories[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemSpinnerCategoryBinding
        val view: View

        if (convertView == null) {
            binding = ItemSpinnerCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemSpinnerCategoryBinding
        }

        val category = categories[position]

        binding.ivIcon.setImageResource(category.imageRes)
        binding.ivText.text = category.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}
