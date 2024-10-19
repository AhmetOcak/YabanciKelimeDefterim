package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.domain.model.word.WordWithId
import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class UpdateWordUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(wordWithId: WordWithId) = repository.updateWord(wordWithId)
}