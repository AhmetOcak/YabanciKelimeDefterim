package com.yabancikelimedefteri.presentation.home

sealed interface UpdateCategoryState {
    object Nothing : UpdateCategoryState
    data class Success(val data: Unit) : UpdateCategoryState
    data class Error(val message: String) : UpdateCategoryState
}