package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.CardRecipeBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Stage

interface OnInteractionListener {
    fun onLike(recipe: Recipe) {}
    fun onEdit(recipe: Recipe) {}
    fun onRemove(recipe: Recipe) {}
    fun onAuthor(recipe: Recipe){}
    fun onName(recipe: Recipe){}
    fun onCategory(recipe: Recipe){}
    fun onContent(recipe: Recipe){}
}

class RecipesAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }


}

class RecipeViewHolder(
    private val binding: CardRecipeBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        recipeBinding(recipe, binding, onInteractionListener)
    }
}

fun recipeBinding(
    recipe: Recipe,
    binding: CardRecipeBinding,
    onInteractionListener: OnInteractionListener
) {
    binding.apply {
        msvRecipe.tag = recipe.pos
        author.text = recipe.author
        name.text = recipe.name
        category.text = recipe.category
        favorite.isChecked = recipe.likedByMe

        when(recipe.category){
            "Европейская кухня"-> recipeView.setImageResource(R.drawable.europe)
            "Азиатская кухня" -> recipeView.setImageResource(R.drawable.asian)
            "Паназиатская кухня"-> recipeView.setImageResource(R.drawable.panasian)
            "Восточная кухня"-> recipeView.setImageResource(R.drawable.east)
            "Американская кухня"-> recipeView.setImageResource(R.drawable.american)
            "Русская кухня"-> recipeView.setImageResource(R.drawable.russian)
            "Средиземноморская кухня"-> recipeView.setImageResource(R.drawable.mediterranean)
        }


        stageContent.adapter = StagesAdapter(onInteractionStageListener = object : OnInteractionStageListener{
            override fun onClicked(stage: Stage) {

            }
        })

        menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.onRemove(recipe)
                            true
                        }
                        R.id.edit -> {
                            onInteractionListener.onEdit(recipe)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        author.setOnClickListener {
            onInteractionListener.onAuthor(recipe)
        }
        name.setOnClickListener {
            onInteractionListener.onName(recipe)
        }
        category.setOnClickListener {
            onInteractionListener.onCategory(recipe)
        }
        favorite.setOnClickListener {
            onInteractionListener.onLike(recipe)
        }
    }

}





class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}