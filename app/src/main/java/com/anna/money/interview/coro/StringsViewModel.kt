package com.anna.money.interview.coro

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StringsViewModel(private val repository: StringsRepository) : ViewModel() {

    private val _strings = mutableStateOf<List<String>>(emptyList()) // State holding a List
    val strings: State<List<String>> = _strings // Expose to View as immutable State

    private var currentPage = 0

    fun loadInitialData() {
    }

    fun loadMore() {
    }
}

interface StringsRepository {

    suspend fun fetchStrings(page: Int): List<String>
}
