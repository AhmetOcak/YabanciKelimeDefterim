package com.yabancikelimedefteri.domain.usecase.category

import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.model.Category
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.repository.WordsRepository
import com.yabancikelimedefteri.domain.utils.errorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: WordsRepository) {

    suspend operator fun invoke(): Flow<Response<List<CategoryWithId>>> = flow {
        try {
            emit(Response.Loading)

            emit(Response.Success(data = repository.getCategories()))
        } catch (e: Exception) {
            emit(Response.Error(message = errorMessage))
        }
    }
}