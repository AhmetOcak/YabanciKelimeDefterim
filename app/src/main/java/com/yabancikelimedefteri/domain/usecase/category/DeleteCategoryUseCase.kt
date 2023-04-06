package com.yabancikelimedefteri.domain.usecase.category

import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(categoryId: Int): Flow<Response<Unit>> = flow {
        try {
            emit(Response.Loading)

            emit(Response.Success(data = repository.deleteCategory(categoryId)))
        } catch (e: Exception) {
            emit(Response.Error(message = e.message ?: e.localizedMessage))
        }
    }
}