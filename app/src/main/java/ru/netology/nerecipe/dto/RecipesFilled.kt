package ru.netology.nerecipe.dto

object RecipesFilled {

    val empty = Recipe(
        id = 0,
        pos = 0,
        author = "Empty",
        name = "Empty",
        category = "Empty",
        content = "",
        likedByMe = false,
    )

    val emptyStage = Stage(
        id = 0,
        pos = 0,
        idRecipe = 0,
        name = "Empty",
        description = "Empty",
        pictureId = 0
    )


}