package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class ObserveAllWordsUseCase @Inject constructor(private val repository: WordsRepository) {

    operator fun invoke() = repository.observeAllWords()
}