package com.timesworld.machinetest.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.timesworld.machinetest.model.FilterDataResponse
import java.io.InputStream
import java.nio.charset.Charset

class FilterViewModel : ViewModel() {

    private val _filterData = MutableLiveData<FilterDataResponse>()
    private val filterData: LiveData<FilterDataResponse> get() = _filterData

    private val _selectedItems = MutableLiveData<List<String>>()
    val selectedItems: LiveData<List<String>> get() = _selectedItems

    init {
        _selectedItems.value = emptyList()
    }

    fun readFilterDataFromAssets(context: Context) {
        try {
            val inputStream: InputStream = context.assets.open("filter_data.rtf")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            // Convert .rtf to JSON and update FilterDataResponse
            val jsonString = String(buffer, Charset.forName("UTF-8"))

            val gson = Gson()
            val filterData = gson.fromJson(jsonString, FilterDataResponse::class.java)

            _filterData.value = filterData
        } catch (e: Exception) {
            // Handle the error
        }
    }


    // Observe filterData to get updates
    fun observeFilterData(owner: LifecycleOwner, observer: Observer<FilterDataResponse>) {
        filterData.observe(owner, observer)
    }

    fun clearSelectedItems() {
        _selectedItems.value = emptyList()
    }

    fun addSelectedItem(item: String) {
        val currentList = _selectedItems.value ?: emptyList()
        _selectedItems.value = currentList + item
    }

    fun removeSelectedItem(item: String) {
        val currentList = _selectedItems.value ?: emptyList()
        _selectedItems.value = currentList - item
    }

    fun getSelectedItems(): List<String> {
        return _selectedItems.value ?: emptyList()
    }

}


