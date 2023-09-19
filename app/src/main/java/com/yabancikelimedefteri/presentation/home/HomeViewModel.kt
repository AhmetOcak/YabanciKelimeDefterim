package com.yabancikelimedefteri.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.usecase.category.DeleteCategoryUseCase
import com.yabancikelimedefteri.domain.usecase.category.GetCategoriesUseCase
import com.yabancikelimedefteri.domain.usecase.category.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) :
    ViewModel() {

    private val _getCategoriesState = MutableStateFlow<GetCategoriesState>(GetCategoriesState.Loading)
    val getCategoriesState = _getCategoriesState.asStateFlow()

    private val _deleteCategoryState = MutableStateFlow<DeleteCategoryState>(DeleteCategoryState.Nothing)
    val deleteCategoryState = _deleteCategoryState.asStateFlow()

    private val _updateCategoryState = MutableStateFlow<UpdateCategoryState>(UpdateCategoryState.Nothing)
    val updateCategoryState = _updateCategoryState.asStateFlow()


    init {
        getCategories()
    }

    var newCategoryName by mutableStateOf("")
        private set
    var newCategoryNameFieldError by mutableStateOf(false)
        private set
    var selectedCatId by mutableStateOf(-1)
        private set

    fun updateNewCategoryName(newValue: String) {
        newCategoryName = newValue
    }

    fun updateSelectedCaId(newValue: Int) {
        selectedCatId = newValue
    }

    fun getCategories() = viewModelScope.launch(Dispatchers.IO) {
        getCategoriesUseCase().collect() {
            when(it) {
                is Response.Loading -> {
                    _getCategoriesState.value = GetCategoriesState.Loading
                }
                is Response.Success -> {
                    _getCategoriesState.value = GetCategoriesState.Success(data = it.data)
                }
                is Response.Error -> {
                    _getCategoriesState.value = GetCategoriesState.Error(message = it.message)
                }
            }
        }
    }

    fun deleteCategories(categoryId: Int) = viewModelScope.launch(Dispatchers.IO) {
        deleteCategoryUseCase(categoryId).collect() {
            when(it) {
                is Response.Loading -> { }
                is Response.Success -> {
                    _deleteCategoryState.value = DeleteCategoryState.Success(it.data)
                }
                is Response.Error -> {
                    _deleteCategoryState.value = DeleteCategoryState.Error(it.message)
                }
            }
        }
    }

    fun updateCategoryName(categoryId: Int, newCategoryName: String) = viewModelScope.launch(Dispatchers.IO) {
        if (newCategoryName.isNotBlank()) {
            updateCategoryUseCase(categoryId, newCategoryName).collect() {
                when(it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        _updateCategoryState.value = UpdateCategoryState.Success(it.data)
                    }
                    is Response.Error -> {
                        _updateCategoryState.value = UpdateCategoryState.Error(it.message)
                    }
                }
            }
        } else {
            newCategoryNameFieldError = true
        }
    }

    fun resetDeleteWordState() {
        _deleteCategoryState.value = DeleteCategoryState.Nothing
    }

    fun resetUpdateCatState() {
        _updateCategoryState.value = UpdateCategoryState.Nothing
    }

    fun resetNewCatName() {
        newCategoryName = ""
    }
}