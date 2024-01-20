package com.timesworld.machinetest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.timesworld.machinetest.adapter.FilterExpandableListAdapter
import com.timesworld.machinetest.databinding.ActivityMainBinding
import com.timesworld.machinetest.viewmodel.FilterViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: FilterViewModel
    private lateinit var adapter: FilterExpandableListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FilterViewModel::class.java]

        // Set up ExpandableListView
        viewModel.readFilterDataFromAssets(this@MainActivity)
        viewModel.observeFilterData(this) {
            adapter = FilterExpandableListAdapter(this@MainActivity, it, viewModel)
            binding.expandableListView.setAdapter(adapter)
            // Set the OnChildClickListener directly on the ExpandableListView
            binding.expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                adapter.handleChildClick(groupPosition, childPosition)
                true
            }
        }

        binding.expandableListView.setOnGroupExpandListener { groupPosition ->
            binding.expandableListView.setSelectedChild(groupPosition, 0, true)
        }

        binding.crossButton.setOnClickListener {
            // Clear selected items and update UI
            viewModel.clearSelectedItems()
        }

        // Observe LiveData for selected items changes
        viewModel.selectedItems.observe(this) {
            updateSelectedItemsView(it)
        }
    }

    private fun updateSelectedItemsView(selectedItems: List<String>) {
        if (selectedItems.isNotEmpty()) {
            binding.selectedItemsView.text = selectedItems.joinToString(", ")
            binding.selectedItemsView.visibility = View.VISIBLE
            binding.crossButton.visibility = View.VISIBLE
        }
        else {
            binding.selectedItemsView.visibility = View.GONE
            binding.crossButton.visibility = View.GONE
        }
    }
}

