package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class MainViewModel : ViewModel() {

    private val _drawerSelectedIndex = MutableStateFlow<Int>(0)
    val drawerSelectedIndex: StateFlow<Int> = _drawerSelectedIndex

    // Function to update the selected index
    fun updateSelectedIndex(index: Int) {
        _drawerSelectedIndex.value = index
    }


}
//    private val _isSearching = MutableStateFlow(false)
//    val isSearching = _isSearching.asStateFlow()
//
//    //second state the text typed by the user
//    private val _searchText = MutableStateFlow("")
//    val searchText = _searchText.asStateFlow()
//    private val _countriesList = MutableStateFlow(countries)
//    val countriesList = searchText
//        .combine(_countriesList) { text, countries ->
//            if (text.isBlank()) {
//                countries
//            }
//            countries.filter { country ->
//                country.uppercase().contains(text.trim().uppercase())
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = _countriesList.value
//        )
//
//    fun onSearchTextChange(text: String) {
//        _searchText.value = text
//    }
//
//    fun onToogleSearch() {
//        _isSearching.value = !_isSearching.value
//        if (!_isSearching.value) {
//            onSearchTextChange("")
//        }
//    }
//}