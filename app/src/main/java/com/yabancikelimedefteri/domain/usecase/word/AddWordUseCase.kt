package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.domain.model.word.Word
import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class AddWordUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(word: Word) = repository.addWord(word)
}