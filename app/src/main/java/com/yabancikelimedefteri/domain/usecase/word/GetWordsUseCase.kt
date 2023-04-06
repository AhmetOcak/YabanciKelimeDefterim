package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.domain.repository.WordsRepository
import com.yabancikelimedefteri.domain.utils.errorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWordsUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(categoryIds: List<Int>): Flow<Response<List<WordWithId>>> = flow {
        try {
            emit(Response.Loading)

            emit(Response.Success(data = repository.getWords(categoryIds)))
        } catch (e: Exception) {
            emit(Response.Error(message = errorMessage))
        }
    }
}