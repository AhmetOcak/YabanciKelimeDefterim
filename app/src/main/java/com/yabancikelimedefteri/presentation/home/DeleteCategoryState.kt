package com.yabancikelimedefteri.presentation.home

sealed interface DeleteCategoryState {
    object Nothing : DeleteCategoryState
    object Loading : DeleteCategoryState
    data class Success(val data: Unit) : DeleteCategoryState
    data class Error(val message: String) : DeleteCategoryState
}