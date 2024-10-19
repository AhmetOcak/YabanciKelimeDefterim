package com.yabancikelimedefteri.domain.model.word

data class WordWithId(
    val wordId: Int,
    val categoryId: Int,
    val foreignWord: String,
    val meaning: String,
    val importanceLevel: Int
)
