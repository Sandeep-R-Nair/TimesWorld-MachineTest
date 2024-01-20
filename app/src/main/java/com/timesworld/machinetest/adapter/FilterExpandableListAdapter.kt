package com.timesworld.machinetest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.timesworld.machinetest.R
import com.timesworld.machinetest.model.FilterCategory
import com.timesworld.machinetest.model.FilterDataResponse
import com.timesworld.machinetest.model.Taxonomy
import com.timesworld.machinetest.viewmodel.FilterViewModel

class FilterExpandableListAdapter(
    private val context: Context,
    private val filterData: FilterDataResponse,
    private val viewModel: FilterViewModel
) :
    BaseExpandableListAdapter() {

    private val selectedChildMap: MutableMap<Int, Int> = HashMap()
    private val selectedItems = mutableListOf<String>()

    init {
        // Observe changes in selected items
        viewModel.selectedItems.observeForever { newSelectedItems ->
            selectedItems.clear()
            selectedItems.addAll(newSelectedItems)
            notifyDataSetChanged()
        }
    }

    override fun getGroupCount(): Int {
        return filterData.data.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return filterData.data[groupPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val groupView: View = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)

        val textViewGroup: TextView = groupView.findViewById(R.id.textViewGroup)
        val selectedCountView: TextView = groupView.findViewById(R.id.selectItemCount)
        val groupIndicator: ImageView = groupView.findViewById(R.id.group_indicator)

        val groupName = filterData.data[groupPosition].name
        textViewGroup.text = groupName

        // Don't need to show selected items count for "Sort by" and "Price Ranges" groups
        if (groupName != "Sort by" && groupName != "Price Ranges"
        ) {
            val selectedCount =
                selectedChildMap[groupPosition]?.let { selectedChildMap[groupPosition] } ?: 0
            selectedCountView.text = "($selectedCount)"

            if (selectedCount != 0) {
                selectedCountView.visibility = View.VISIBLE
            }
        } else {
            selectedCountView.visibility = View.GONE
        }

        groupIndicator.setImageResource(
            if (isExpanded)
                R.drawable.ic_arrow_up
            else
                R.drawable.ic_arrow_down
        )


        return groupView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val childView: View = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_child, parent, false)

        val radioButton: RadioButton = childView.findViewById(R.id.radioButtonChild)

        val taxonomy: Taxonomy = filterData.data[groupPosition].taxonomies[childPosition]
        val taxonomyName = taxonomy.name

        radioButton.text = taxonomyName
        radioButton.isChecked = selectedItems.contains(taxonomyName)

        // Handle child click
        radioButton.setOnClickListener {
            handleChildClick(groupPosition, childPosition)
        }

        return childView
    }

    fun handleChildClick(groupPosition: Int, childPosition: Int) {
        val taxonomy: Taxonomy = filterData.data[groupPosition].taxonomies[childPosition]
        val taxonomyName = taxonomy.name
if(taxonomyName!=null) {
    // Check if the item is already selected
    if (viewModel.getSelectedItems().contains(taxonomyName)) {
        // Item is already selected, remove it
        viewModel.removeSelectedItem(taxonomyName)
        selectedChildMap[groupPosition] = selectedChildMap[groupPosition]?.minus(1) ?: 0

    } else {
        // Item is not selected, add it
        viewModel.addSelectedItem(taxonomyName)
        selectedChildMap[groupPosition] = selectedChildMap[groupPosition]?.plus(1) ?: 1

    }
    notifyDataSetChanged()
}

    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return filterData.data[groupPosition].taxonomies.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return filterData.data[groupPosition].taxonomies[childPosition]
    }

    override fun getGroupTypeCount(): Int {
        return 1
    }

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}
