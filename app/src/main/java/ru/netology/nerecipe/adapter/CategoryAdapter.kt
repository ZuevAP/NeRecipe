package ru.netology.nerecipe.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.databinding.CategoryItemBinding
import ru.netology.nerecipe.dto.Category

interface OnInteractionCatListener {
    fun onClicked(category: Category)
}

class FeedFragmentCategoryAdapter(
    private val onInteractionCatListener: OnInteractionCatListener,
) : RecyclerView.Adapter<CategoryViewHolder>() {

    val listCategory = listOf(
        Category(0, "All", "Все категории"),
        Category(1, "Eurasian", "Европейская кухня"),
        Category(2, "Asian", "Азиатская кухня"),
        Category(3, "Panasian", "Паназиатская кухня"),
        Category(4, "Eastern", "Восточная кухня"),
        Category(5, "American", "Американская кухня"),
        Category(6, "Russian", "Русская кухня"),
        Category(7, "Mediterranean", "Средиземноморская кухня")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding, onInteractionCatListener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = listCategory[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = listCategory.size
}
class CategoryNewRecipeAdapter(
    private val onInteractionCatListener: OnInteractionCatListener,
) : RecyclerView.Adapter<CategoryViewHolder>() {

    val listCategory = listOf(
        Category(0, "Eurasian", "Европейская кухня"),
        Category(1, "Asian", "Азиатская кухня"),
        Category(2, "Panasian", "Паназиатская кухня"),
        Category(3, "Eastern", "Восточная кухня"),
        Category(4, "American", "Американская кухня"),
        Category(5, "Russian", "Русская кухня"),
        Category(6, "Mediterranean", "Средиземноморская кухня")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding, onInteractionCatListener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = listCategory[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = listCategory.size
}

class CategoryViewHolder(
    private val binding: CategoryItemBinding,
    private val onInteractionCatListener: OnInteractionCatListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category) {
        categoryBinding(category, binding, onInteractionCatListener)
    }
}

fun categoryBinding(
    category: Category,
    binding: CategoryItemBinding,
    onInteractionCatListener: OnInteractionCatListener,
) {
    binding.apply {
        with(catTB) {
            id = category.id
            tag = category.titleRu
            text = category.titleRu
            textOn = category.titleRu
            textOff = category.titleRu
            isChecked = category.selected
            val defaultColor = textColors.defaultColor
            setTextColor(defaultColor)

            setOnClickListener {
                if (isChecked) {
                    textOn = category.titleRu
                    category.selected = true
                    setTextColor(Color.BLUE)
                } else {
                    textOff = category.titleRu
                    category.selected = false
                    setTextColor(defaultColor)
                }
                onInteractionCatListener.onClicked(category)
            }
        }
    }
}