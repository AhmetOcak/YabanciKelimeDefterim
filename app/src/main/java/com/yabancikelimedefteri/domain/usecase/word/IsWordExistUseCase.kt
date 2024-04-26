package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.core.helpers.plain
import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class IsWordExistUseCase @Inject constructor(private val wordsRepository: WordsRepository) {

    suspend operator fun invoke(word: String): Boolean {
        return wordsRepository.getAllWords().find { it.foreignWord.plain() == word.plain() } != null
    }
}