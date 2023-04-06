package com.yabancikelimedefteri.presentation.add_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.model.Category
import com.yabancikelimedefteri.domain.usecase.category.CreateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val createCategoryUseCase: CreateCategoryUseCase
): ViewModel() {

    private val _createCategoryState = MutableStateFlow<CreateCategoryState>(CreateCategoryState.Nothing)
    val createCategoryState = _createCategoryState.asStateFlow()

    var categoryNameVal by mutableStateOf("")
        private set
    var categoryFieldError by mutableStateOf(false)
        private set

    fun updateCategoryName(newValue: String) { categoryNameVal = newValue }

    fun createCategory() = viewModelScope.launch(Dispatchers.IO) {
        if (categoryNameVal.isNotBlank()) {
            categoryFieldError = false

            createCategoryUseCase(category = Category(categoryName = categoryNameVal)).collect() {
                when(it) {
                    is Response.Loading -> {
                        _createCategoryState.value = CreateCategoryState.Loading
                    }
                    is Response.Success -> {
                        _createCategoryState.value = CreateCategoryState.Success
                        categoryNameVal = ""
                    }
                    is Response.Error -> {
                        _createCategoryState.value = CreateCategoryState.Error(message = it.message)
                    }
                }
            }
        } else {
            categoryFieldError = true
        }
    }

    fun resetCategoryState() { _createCategoryState.value = CreateCategoryState.Nothing }
}