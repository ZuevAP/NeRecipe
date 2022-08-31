package ru.netology.nerecipe.dto

data class Stage(
    val id: Int,
    val idRecipe: Long,
    var pos: Int,
    val name: String,
    val description: String,
    val pictureId: Int
)