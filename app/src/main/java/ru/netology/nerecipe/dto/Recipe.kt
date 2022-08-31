package ru.netology.nerecipe.dto

data class Recipe(
    val id: Long,
    val pos: Int,
    val author: String,
    val name: String,
    val category: String,
    val content: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shared: Int = 0,
    val viewed: Int = 0,
    val video: String? = null
)
