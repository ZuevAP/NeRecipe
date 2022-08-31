package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.databinding.StageItemBinding
import ru.netology.nerecipe.dto.Stage

interface OnInteractionStageListener {
    fun onClicked(stage: Stage)
}

class StagesAdapter(
    private val onInteractionStageListener: OnInteractionStageListener,
) : ListAdapter<Stage, StageViewHolder>(StageDiffCallback()) {

    val idRecipe = 1L


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StageItemBinding.inflate(inflater, parent, false)
        return StageViewHolder(binding, onInteractionStageListener)
    }

    override fun onBindViewHolder(holder: StageViewHolder, position: Int) {
        val stage = getItem(position)
        holder.bind(stage)
    }

}

class StageViewHolder(
    private val binding: StageItemBinding,
    private val onInteractionStageListener: OnInteractionStageListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(stage: Stage) {
        stageBinding(stage, binding, onInteractionStageListener)
    }
}

fun stageBinding(
    stage: Stage,
    binding: StageItemBinding,
    onInteractionStageListener: OnInteractionStageListener,
) {
    binding.apply {
        with(stageCardView) {
            tag = stage.pos
        }

        with(stageTextView) {
            text = "Шаг ${stage.pos}\n${stage.name}\n${stage.description}"

            setOnClickListener {
                stageImageView.visibility = View.VISIBLE
                onInteractionStageListener.onClicked(stage)
            }
        }



        
    }
}

class StageDiffCallback : DiffUtil.ItemCallback<Stage>() {
    override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean {
        return oldItem == newItem
    }
}