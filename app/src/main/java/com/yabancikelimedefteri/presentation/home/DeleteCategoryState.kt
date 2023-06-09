package com.yabancikelimedefteri.presentation.home

sealed interface DeleteCategoryState {
    object Nothing : DeleteCategoryState
    data class Success(val data: Unit) : DeleteCategoryState
    data class Error(val message: String) : DeleteCategoryState
}