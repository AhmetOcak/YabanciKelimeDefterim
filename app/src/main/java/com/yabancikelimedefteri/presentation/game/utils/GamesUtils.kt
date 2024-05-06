package com.yabancikelimedefteri.presentation.game.utils

fun calculateCorrectRate(correctAnswerCount: Int, wrongAnswerCount: Int): Double {
    return (correctAnswerCount.toDouble() / (correctAnswerCount + wrongAnswerCount)) * 100
}