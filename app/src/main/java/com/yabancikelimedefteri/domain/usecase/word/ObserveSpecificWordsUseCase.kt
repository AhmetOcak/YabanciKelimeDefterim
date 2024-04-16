package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class ObserveSpecificWordsUseCase @Inject constructor(private val repository: WordsRepository) {

    operator fun invoke(categoryIds: Int) = repository.observeSpecificWords(categoryIds)
}