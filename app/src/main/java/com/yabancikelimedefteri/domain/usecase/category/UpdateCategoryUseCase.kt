package com.yabancikelimedefteri.domain.usecase.category

import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(categoryId: Int, newCategoryName: String) =
        repository.updateCategory(categoryId, newCategoryName)
}