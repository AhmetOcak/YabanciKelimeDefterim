package com.yabancikelimedefteri.domain.usecase.category

import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.repository.WordsRepository
import com.yabancikelimedefteri.domain.utils.errorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(categoryId: Int, newCategoryName: String): Flow<Response<Unit>> =
        flow {
            try {
                emit(Response.Loading)

                emit(Response.Success(data = repository.updateCategoryName(categoryId, newCategoryName)))
            } catch (e: Exception) {
                emit(Response.Error(message = errorMessage))
            }
        }
}