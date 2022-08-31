package ru.netology.nerecipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nerecipe.dao.RecipeDao
import ru.netology.nerecipe.entity.RecipeEntity
import ru.netology.nerecipe.dao.StageDao
import ru.netology.nerecipe.entity.StageEntity

@Database(entities = [RecipeEntity::class, StageEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun stageDao(): StageDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app10.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}