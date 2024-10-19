package com.yabancikelimedefteri.data.mapper

import com.yabancikelimedefteri.data.datasource.local.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.entity.WordEntity
import com.yabancikelimedefteri.domain.model.word.Category
import com.yabancikelimedefteri.domain.model.word.CategoryWithId
import com.yabancikelimedefteri.domain.model.word.Word
import com.yabancikelimedefteri.domain.model.word.WordWithId

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(categoryName = categoryName)
}

fun List<CategoryEntity>.toListCategory(): List<CategoryWithId> {
    val category = arrayListOf<CategoryWithId>()

    forEach {
        category.add(
            CategoryWithId(categoryId = it.id, categoryName = it.categoryName)
        )
    }

    return category
}

fun Word.toWordEntity(): WordEntity {
    return WordEntity(
        categoryId = categoryId,
        foreignWord = foreignWord,
        meaning = meaning,
        importanceLevel = importanceLevel
    )
}

fun WordWithId.toWordEntity(): WordEntity {
    return WordEntity(
        id = wordId,
        categoryId = categoryId,
        foreignWord = foreignWord,
        meaning = meaning,
        importanceLevel = importanceLevel
    )
}

fun List<WordEntity>.toListWord(): List<WordWithId> {
    val words = arrayListOf<WordWithId>()

    forEach {
        words.add(
            WordWithId(
                wordId = it.id,
                categoryId = it.categoryId,
                foreignWord = it.foreignWord,
                meaning = it.meaning,
                importanceLevel = it.importanceLevel
            )
        )
    }

    return words
}