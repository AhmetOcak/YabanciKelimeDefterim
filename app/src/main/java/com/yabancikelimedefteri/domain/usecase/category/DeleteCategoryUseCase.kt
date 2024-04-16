package com.yabancikelimedefteri.domain.usecase.category

import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(categoryId: Int) = repository.deleteCategory(categoryId)
}