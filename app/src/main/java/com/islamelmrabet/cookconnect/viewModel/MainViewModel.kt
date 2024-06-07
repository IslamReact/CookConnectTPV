package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Class MainViewModel
 *
 */
class MainViewModel : ViewModel() {

    private val _drawerSelectedIndex = MutableStateFlow(0)
    val drawerSelectedIndex: StateFlow<Int> = _drawerSelectedIndex

    /**
     * Update the selected index of the drawer
     *
     * @param index
     */
    fun updateSelectedIndex(index: Int) {
        _drawerSelectedIndex.value = index
    }


}
