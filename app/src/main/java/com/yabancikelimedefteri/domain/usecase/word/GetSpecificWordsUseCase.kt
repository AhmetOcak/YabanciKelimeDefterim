package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class GetSpecificWordsUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(categoryId: Int) = repository.getSpecificWords(categoryId)
}