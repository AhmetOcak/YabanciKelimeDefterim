package com.yabancikelimedefteri.presentation.home

sealed interface HomeState {
    object Loading : HomeState
    data class Success(val data: MutableMap<String, *>?) : HomeState
    data class Error(val message: String) : HomeState
}