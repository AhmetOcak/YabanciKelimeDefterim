package com.yabancikelimedefteri.domain.usecase.word

import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllWordsUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(): Flow<Response<List<WordWithId>>> = flow {
        try {
            emit(Response.Loading)

            emit(Response.Success(data = repository.getAllWords()))
        } catch (e: Exception) {
            emit(Response.Error(message = e.message ?: e.localizedMessage))
        }
    }
}