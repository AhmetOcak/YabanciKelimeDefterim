package com.yabancikelimedefteri.presentation.word_categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.UiText
import com.yabancikelimedefteri.domain.model.Category
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.usecase.category.AddCategoryUseCase
import com.yabancikelimedefteri.domain.usecase.category.DeleteCategoryUseCase
import com.yabancikelimedefteri.domain.usecase.category.ObserveCategoriesUseCase
import com.yabancikelimedefteri.domain.usecase.category.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordCategoriesViewModel @Inject constructor(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordCategoriesUiState())
    val uiState: StateFlow<WordCategoriesUiState> = _uiState.asStateFlow()

    init {
        observeCategories()
    }

    // Update CategoryName
    var updatedCategoryName by mutableStateOf("")
        private set

    var updatedCategoryNameFieldError by mutableStateOf(false)
        private set

    private var selectedCatId by mutableIntStateOf(-1)

    // Create Category
    var newCategoryName by mutableStateOf("")
        private set

    var newCategoryFieldError by mutableStateOf(false)
        private set

    fun updateUpdatedCategoryName(newValue: String) {
        updatedCategoryName = newValue
    }

    fun updateSelectedCaId(newValue: Int) {
        selectedCatId = newValue
    }

    fun updateNewCategoryName(newValue: String) {
        newCategoryName = newValue
    }

    private fun observeCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            observeCategoriesUseCase().collect { categories ->
                try {
                    _uiState.update {
                        it.copy(categories = categories, isLoading = false)
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            errorMessages = listOf(UiText.StringResource(R.string.error)),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteCategoryUseCase(categoryId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                }
            }
        }
    }

    fun updateCategoryName() {
        if (updatedCategoryName.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    updateCategoryUseCase(
                        categoryId = selectedCatId,
                        newCategoryName = updatedCategoryName
                    )
                    updatedCategoryName = ""
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                    }
                }
            }
        } else {
            updatedCategoryNameFieldError = true
        }

    }

    fun addCategory() {
        if (newCategoryName.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    addCategoryUseCase(Category(newCategoryName))
                    newCategoryName = ""
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                    }
                }
            }
        } else {
            newCategoryFieldError = true
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = emptyList())
        }
    }

    fun clearUpdateCategoryNameVars() {
        updatedCategoryName = ""
        updatedCategoryNameFieldError = false
    }

    fun clearNewCategoryVars() {
        newCategoryName = ""
        newCategoryFieldError = false
    }
}

data class WordCategoriesUiState(
    val isLoading: Boolean = true,
    val categories: List<CategoryWithId> = emptyList(),
    val errorMessages: List<UiText> = emptyList()
)