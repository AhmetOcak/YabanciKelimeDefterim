package com.yabancikelimedefteri.domain.model

data class WordWithId(
    var wordId: Int,
    var categoryId: Int,
    var foreignWord: String,
    var meaning: String
)
