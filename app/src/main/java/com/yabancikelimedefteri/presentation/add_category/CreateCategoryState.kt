package com.yabancikelimedefteri.presentation.add_category

sealed interface CreateCategoryState {
    object Nothing : CreateCategoryState
    object Loading : CreateCategoryState
    object Success : CreateCategoryState
    data class Error(val message: String) : CreateCategoryState
}