package com.yabancikelimedefteri.domain.model.word

data class Word(
    val categoryId: Int,
    val foreignWord: String,
    val meaning: String,
    val importanceLevel: Int
)
