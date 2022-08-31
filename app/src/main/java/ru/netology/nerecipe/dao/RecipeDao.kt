package ru.netology.nerecipe.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nerecipe.entity.RecipeEntity
import ru.netology.nerecipe.entity.StageEntity

@Dao
interface RecipeDao {


    @Query("SELECT * FROM RecipeEntity ORDER BY pos DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM RecipeEntity WHERE" +
            " CASE WHEN :isSetFilter THEN author IN (:author)" +
            " OR name IN (:name) OR category IN (:category)" +
            " OR likedByMe IN (:likedByMe) ELSE 1 END ORDER BY pos DESC")

    fun getByFilter(
        isSetFilter: Boolean,
        author: String,
        name: String,
        category: String,
        likedByMe: Boolean
    ): List<RecipeEntity>

    @Query("SELECT * FROM RecipeEntity WHERE " +
            "CASE WHEN :isSetFilter THEN author LIKE :author " +
            "END ORDER BY pos DESC")
    fun getByFilterOnAuthor(
        isSetFilter: Boolean,
        author: String
    ): List<RecipeEntity>

    @Query("SELECT * FROM RecipeEntity WHERE " +
            "CASE WHEN :isSetFilter THEN name LIKE :name " +
            "END ORDER BY pos DESC")
    fun getByFilterOnName(
        isSetFilter: Boolean,
        name: String
    ): List<RecipeEntity>

    @Query("SELECT * FROM RecipeEntity WHERE " +
            "CASE WHEN :isSetFilter THEN category LIKE :category " +
            "END ORDER BY pos DESC")
    fun getByFilterOnCat(
        isSetFilter: Boolean,
        category: String
    ): List<RecipeEntity>

    @Query("SELECT * FROM RecipeEntity WHERE " +
            "CASE WHEN :isSetFilter THEN likedByMe IN (:likedByMe) " +
            "END ORDER BY pos DESC")
    fun getByFilterOnLike(
        isSetFilter: Boolean,
        likedByMe: Boolean
    ): List<RecipeEntity>

    @Query("SELECT * FROM RecipeEntity WHERE id = :id")
    fun getById(id: Long): RecipeEntity

    @Insert
    fun insert(recipe: RecipeEntity)

    @Query("UPDATE RecipeEntity SET author = :author, name = :name, category = :category, content = :content WHERE id = :id")
    fun updateById(id: Long, author: String, name: String, category: String, content: String)

    @Query("UPDATE RecipeEntity SET id = :id, pos = :newPos WHERE id = :id AND pos = :oldPos")
    fun updatePosById(id: Long, oldPos: Int, newPos: Int)

    fun save(recipe: RecipeEntity) =
        if (recipe.id == 0L) insert(recipe) else {
            updateById(recipe.id, recipe.author, recipe.name, recipe.category, recipe.content)
        }

    @Query(
        """
        UPDATE RecipeEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun likeById(id: Long)

    @Query("DELETE FROM RecipeEntity WHERE id = :id")
    fun removeById(id: Long)
}

@Dao
interface StageDao {


    @Query("SELECT * FROM StageEntity WHERE idRecipe IN (:idRecipe) ORDER BY pos ASC")
    fun getSubById(idRecipe: Long): List<StageEntity>

    @Insert
    fun insert(stage: StageEntity)

    @Query("UPDATE StageEntity SET idRecipe = :idRecipe, pos = :pos, name = :name, description = :description, pictureId = :pictureId WHERE id = :id")
    fun updateById(id: Int, idRecipe: Long, pos: Int, name: String, description: String, pictureId: Int)

    @Query("UPDATE StageEntity SET idRecipe = :idRecipe, pos = :newPos WHERE idRecipe = :idRecipe AND pos = :oldPos")
    fun updatePosById(idRecipe: Long, oldPos: Int, newPos: Int)

    fun save(stage: StageEntity) =
        if (stage.id == 0) insert(stage) else {
            updateById(stage.id, stage.idRecipe, stage.pos, stage.name, stage.description, stage.pictureId)
        }

    @Query("DELETE FROM StageEntity WHERE id = :id")
    fun removeById(id: Int)

    @Query("DELETE FROM StageEntity WHERE idRecipe = :idRecipe")
    fun removeByRecipeId(idRecipe: Long)
}