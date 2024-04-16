package com.yabancikelimedefteri.domain.usecase.category

import com.yabancikelimedefteri.domain.model.Category
import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(category: Category) = repository.addCategory(category)
}