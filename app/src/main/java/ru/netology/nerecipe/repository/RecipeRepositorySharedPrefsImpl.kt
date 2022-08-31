package ru.netology.nerecipe.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.RecipesFilled
import ru.netology.nerecipe.dto.Stage

const val DRAFT_POST_ID = 1L

class RecipeRepositorySharedPrefsImpl(
    context: Context,
) : RecipeRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Recipe::class.java).type
    private val key = "posts"
    private var nextId = 1L
    private var recipes = emptyList<Recipe>()
    private val data = MutableLiveData(recipes)
    private val dataPost = MutableLiveData(RecipesFilled.empty)

    init {
        prefs.getString(key, null)?.let {
            recipes = gson.fromJson(it, type)
            data.value = recipes
        }
    }

    override fun getAll(): LiveData<List<Recipe>> = data
    

    override fun getByFilter(author: String, name: String, category: String, likedByMe: Boolean) {}
    override fun getByFilterOnAuthor(author: String) {}
    override fun getByFilterOnName(name: String) {}
    override fun getByFilterOnCat(category: String) {}
    override fun getByFilterOnLike(likedByMe: Boolean) {}

    override fun getAllStages(): LiveData<List<Stage>> {
        return MutableLiveData(emptyList())
    }

    override fun getSubById(idRecipe: Long) {}

    override fun saveSub(stage: Stage) {}

    override fun removeSubById(stage: Stage) {}


    override fun getRecipe(): LiveData<Recipe> = dataPost
    override fun getRecipeById(id: Long): Recipe? {
        dataPost.value = recipes.find {
            it.id == id
        }
        return dataPost.value
    }

    override fun save(recipe: Recipe) {
        if (recipe.id == DRAFT_POST_ID) {
            recipes = listOf(
                recipe.copy(
                    id = recipe.id,
                    content = recipe.content,
                    author = "Me",
                    likedByMe = false,
                    name = "now"
                )
            ) + recipes
            data.value = recipes
            sync()
            return
        }

        if (recipe.id == 0L) {

            recipes = listOf(
                recipe.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    name = "now"
                )
            ) + recipes
            data.value = recipes
            sync()
            return
        }

        recipes = recipes.map {
            if (it.id != recipe.id)
                it
            else
                it.copy(content = recipe.content)
        }
        data.value = recipes
        sync()
    }

    override fun likeById(id: Long) {
        recipes = recipes.map {
            if (it.id != id)
                it
            else
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                )
        }
        data.value = recipes
        sync()
    }

    override fun removeById(id: Long) {
        recipes = recipes.filter { it.id != id }
        data.value = recipes
        sync()
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(recipes))
            apply()
        }
    }
}

fun getConstDraftPostId(): Long = DRAFT_POST_ID
