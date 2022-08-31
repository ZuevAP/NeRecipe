package ru.netology.nerecipe.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nerecipe.dto.Stage

@Entity
data class StageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idRecipe: Long,
    val pos: Int,
    val name: String,
    val description: String,
    val pictureId: Int
) {
    fun toDto() = Stage(id, idRecipe, pos, name, description, pictureId)

    companion object {
        fun fromDto(dto: Stage) =
            StageEntity(dto.id, dto.idRecipe, dto.pos, dto.name, dto.description, dto.pictureId)
    }
}