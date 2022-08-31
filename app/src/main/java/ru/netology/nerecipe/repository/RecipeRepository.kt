package ru.netology.nerecipe.repository

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Stage

interface RecipeRepository {
    fun getAll(): LiveData<List<Recipe>>
    fun likeById(id: Long)
    fun save(recipe: Recipe)
    fun removeById(id: Long)
    fun getRecipeById(id: Long): Recipe?
    fun getRecipe(): LiveData<Recipe>
    fun getByFilter(author: String, name: String, category: String, likedByMe: Boolean)
    fun getByFilterOnAuthor(author: String)
    fun getByFilterOnName(name: String)
    fun getByFilterOnCat(category: String)
    fun getByFilterOnLike(likedByMe: Boolean)
    fun getAllStages(): LiveData<List<Stage>>
    fun getSubById(idRecipe: Long)
    fun saveSub(stage: Stage)
    fun removeSubById(stage: Stage)
}