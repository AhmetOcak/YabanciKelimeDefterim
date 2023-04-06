package com.yabancikelimedefteri.presentation.game

import com.yabancikelimedefteri.domain.model.CategoryWithId

sealed interface GetGameCategoriesState {
    object Loading : GetGameCategoriesState
    data class Success(val data: List<CategoryWithId>) : GetGameCategoriesState
    data class Error(val message: String) : GetGameCategoriesState
}