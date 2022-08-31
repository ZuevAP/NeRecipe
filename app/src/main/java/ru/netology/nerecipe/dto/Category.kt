package ru.netology.nerecipe.dto

data class Category(
    val id: Int,
    val title: String,
    val titleRu: String,
    var selected: Boolean = false
)