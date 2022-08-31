package ru.netology.nerecipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.RecipesFilled
import ru.netology.nerecipe.dto.Stage
import ru.netology.nerecipe.repository.*

private val empty = RecipesFilled.empty
private val emptyStage = RecipesFilled.emptyStage

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository = RecipeRepositoryImpl(
        AppDb.getInstance(context = application).recipeDao(),
        AppDb.getInstance(context = application).stageDao()
    )
    val data = repository.getAll()
    val dataRecipe = repository.getRecipe()
    val edited = MutableLiveData(empty)

    val dataStages = repository.getAllStages()
    val editedSub = MutableLiveData(emptyStage)


    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        toEmpty()
    }

    fun toEmpty() {
        edited.value = empty
    }

    fun edit(recipe: Recipe) {
        edited.value = recipe
    }

    fun changeContent(pos: Int, author: String, name: String, category: String, content: String) {
        val textAuthor = author.trim()
        val textName = name.trim()
        val textCategory = category.trim()
        val text = content.trim()
        if (edited.value?.author == textAuthor &&
            edited.value?.name == textName &&
            edited.value?.category == textCategory &&
            edited.value?.content == text
        ) {
            return
        }
        edited.value = edited.value?.copy(
            pos = pos,
            author = textAuthor,
            name = textName,
            category = textCategory,
            content = text)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun getRecipeById(id: Long) = repository.getRecipeById(id)
    fun getByFilter(author: String, name: String, category: String, likedByMe: Boolean) =
        repository.getByFilter(author, name, category, likedByMe)

    fun getByFilterOnName(name: String) = repository.getByFilterOnName(name)
    fun getByFilterOnCat(category: String) = repository.getByFilterOnCat(category)
    fun getByFilterOnLike(likedByMe: Boolean) = repository.getByFilterOnLike(likedByMe)


    fun editStage(stage: Stage) {
        editedSub.value = stage
    }

    fun updateStages() {
        edited.value.let {
            it?.id
        }?.let {
            edited.value?.let {
                getSubById(it.id)
            }
        }
    }

    fun saveSub() {
        editedSub.value?.let {
            repository.saveSub(it)
        }
        updateStages()
        editedSub.value = emptyStage
    }

    fun changeStageContent(pos: Int, name: String, description: String) {
        val textName = name.trim()
        val text = description.trim()
        if (editedSub.value?.name == textName &&
            editedSub.value?.description == text
        ) {
            return
        }
        editedSub.value = edited.value.let {
            it?.id
        }?.let {
            editedSub.value?.copy(
                idRecipe = it,
                pos = pos,
                name = textName,
                description = text
            )
        }
    }

    private fun getSubById(idRecipe: Long) = repository.getSubById(idRecipe)
    fun removeSubById(stage: Stage) = repository.removeSubById(stage)
}


class DraftContentRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository = RecipeRepositorySharedPrefsImpl(application)

    fun save(content: String) {
        val draftRecipe = Recipe(
            id = getConstDraftPostId(),
            pos = 0,
            author = "",
            name = "",
            category = "",
            content = content,
            likedByMe = false,
        )
        repository.save(draftRecipe)
    }

    fun getRecipeById(id: Long) = repository.getRecipeById(id)
}