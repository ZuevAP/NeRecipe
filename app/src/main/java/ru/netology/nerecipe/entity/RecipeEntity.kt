package ru.netology.nerecipe.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nerecipe.dto.Recipe

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val pos: Int,
    val author: String,
    val name: String,
    val category: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shared: Int = 0,
    val viewed: Int = 0,
    val video: String? = null
) {
    fun toDto() = Recipe(id, pos, author, name, category, content, likedByMe, likes, shared, viewed, video)

    companion object {
        fun fromDto(dto: Recipe) =
            RecipeEntity(dto.id, dto.pos, dto.author, dto.name, dto.category, dto.content,
                dto.likedByMe, dto.likes, dto.shared, dto.viewed, dto.video)
    }
}