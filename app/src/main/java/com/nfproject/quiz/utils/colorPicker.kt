package com.nfproject.quiz.utils

object colorPicker{
    val colors = arrayOf(
        "#1E90FF",
        "#B0C4DE",
        "#B0E0E6",
        "#87CEEB",
        "#00BFFF",
        "#0000FF",
        "#000080",
        "#7B68EE",
        "#00CED1"
    )
    var currentColorIndex: Int = 0
    fun getColols():String{
        currentColorIndex = (currentColorIndex +1)% colors.size
        return colors[currentColorIndex]

    }
}