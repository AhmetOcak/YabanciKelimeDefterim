package com.yabancikelimedefteri.domain.usecase.category

import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class ObserveCategoriesUseCase @Inject constructor(private val repository: WordsRepository) {

    operator fun invoke() = repository.observeCategories()
}