package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class DeleteWordUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(wordId: Int) = repository.deleteWord(wordId)
}