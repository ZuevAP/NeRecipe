package ru.netology.nerecipe.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nerecipe.dao.RecipeDao
import ru.netology.nerecipe.dao.StageDao
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Stage
import ru.netology.nerecipe.entity.RecipeEntity
import ru.netology.nerecipe.dto.RecipesFilled
import ru.netology.nerecipe.entity.StageEntity

class RecipeRepositoryImpl(
    private val dao: RecipeDao,
    private val daoStage: StageDao,
) : RecipeRepository {

    private val data: MutableLiveData<List<Recipe>> = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    } as MutableLiveData<List<Recipe>> 

    private val dataRecipe = MutableLiveData(RecipesFilled.empty)
    private var dataStages: MutableLiveData<List<Stage>> = MutableLiveData(emptyList())


    override fun getAll() = data
    override fun getAllStages() = dataStages

    override fun getByFilter(
        author: String,
        name: String,
        category: String,
        likedByMe: Boolean,
    ) {
        val isSetFilter = author != "" || name != "" || category != "" || likedByMe
        data.value =
            dao.getByFilter(isSetFilter, author, name, category, likedByMe).map { it.toDto() }
    }

    override fun getByFilterOnAuthor(
        author: String,
    ) {
        val isSetFilter = author != ""
        data.value =
            dao.getByFilterOnAuthor(isSetFilter, author).map { it.toDto() }
    }

    override fun getByFilterOnName(
        name: String,
    ) {
        val isSetFilter = name != ""
        data.value =
            dao.getByFilterOnName(isSetFilter, name).map { it.toDto() }
    }

    override fun getByFilterOnCat(
        category: String,
    ) {
        val isSetFilter = category != ""
        data.value =
            dao.getByFilterOnCat(isSetFilter, category).map { it.toDto() }
    }

    override fun getByFilterOnLike(
        likedByMe: Boolean,
    ) {
        data.value =
            dao.getByFilterOnLike(likedByMe, likedByMe).map { it.toDto() }
    }

    override fun getRecipe(): LiveData<Recipe> = dataRecipe
    override fun getRecipeById(id: Long): Recipe? {
        dataRecipe.value = dao.getById(id).toDto()
        getSubById(id)
        return dataRecipe.value
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        getRecipeById(id)
    }

    override fun save(recipe: Recipe) {
        dao.save(RecipeEntity.fromDto(recipe))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        daoStage.removeByRecipeId(id)

        val recipesEntity = dao.getAll().value
        if (recipesEntity != null) {
            for ((index) in recipesEntity.withIndex()) {
                val oldPos = recipesEntity[index].pos
                dao.updatePosById(id, oldPos, index + 1)
            }
        }
    }


    override fun getSubById(idRecipe: Long) {
        dataStages.value = daoStage.getSubById(idRecipe).map {
            it.toDto()
        }
    }

    override fun saveSub(stage: Stage) {
        daoStage.save(StageEntity.fromDto(stage))
        getSubById(stage.idRecipe)
    }

    override fun removeSubById(stage: Stage) {
        daoStage.removeById(stage.id)

        val stagesEntity = daoStage.getSubById(stage.idRecipe)
        for ((index) in stagesEntity.withIndex()) {
            val oldPos = stagesEntity[index].pos
            daoStage.updatePosById(stage.idRecipe, oldPos, index + 1)
        }
        getSubById(stage.idRecipe)
    }
}