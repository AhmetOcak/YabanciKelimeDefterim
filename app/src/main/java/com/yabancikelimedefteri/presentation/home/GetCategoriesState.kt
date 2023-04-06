package com.yabancikelimedefteri.presentation.home

import com.yabancikelimedefteri.domain.model.Category
import com.yabancikelimedefteri.domain.model.CategoryWithId

sealed interface GetCategoriesState {
    object Loading : GetCategoriesState
    data class Success(val data: List<CategoryWithId>) : GetCategoriesState
    data class Error(val message: String) : GetCategoriesState
}