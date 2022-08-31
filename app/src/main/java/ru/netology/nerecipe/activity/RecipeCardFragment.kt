package ru.netology.nerecipe.activity

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.authorArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.nameArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.catArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.textArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.idSubArg
import ru.netology.nerecipe.activity.NewStageFragment.Companion.idSubNewArg
import ru.netology.nerecipe.activity.NewStageFragment.Companion.stagePosArg
import ru.netology.nerecipe.activity.NewStageFragment.Companion.stageNameArg
import ru.netology.nerecipe.activity.NewStageFragment.Companion.stageTextArg
import ru.netology.nerecipe.adapter.*
import ru.netology.nerecipe.databinding.CardRecipeBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Stage
import ru.netology.nerecipe.util.StringArg
import ru.netology.nerecipe.viewmodel.RecipeViewModel



class RecipeCardFragment : Fragment() {
    companion object {
        var Bundle.idArg: String? by StringArg
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private lateinit var stageInn: Stage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardRecipeBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.idArg?.let {
            viewModel.getRecipeById(it.toLong())
        }?.let { it ->

            val onInteractionListener = object : OnInteractionListener {
                private fun toNewRecipeFragment(recipe: Recipe) {
                    findNavController().navigate(
                        R.id.action_recipeCardFragment_to_newRecipeFragment,
                        Bundle().apply {
                            idSubArg = recipe.id.toString()
                            authorArg = recipe.author
                            nameArg = recipe.name
                            catArg = recipe.category
                            textArg = recipe.content
                        }
                    )
                }
                override fun onEdit(recipe: Recipe) {
                    viewModel.edit(recipe)
                    viewModel.updateStages()
                    toNewRecipeFragment(recipe)
                }

                override fun onLike(recipe: Recipe) {
                    viewModel.likeById(recipe.id)
                }

                override fun onRemove(recipe: Recipe) {
                    viewModel.removeById(recipe.id)
                    findNavController().navigateUp()
                }

                override fun onAuthor(recipe: Recipe) { viewModel.updateStages()}

                override fun onName(recipe: Recipe) { viewModel.updateStages()}

                override fun onCategory(recipe: Recipe) { viewModel.updateStages()}

                override fun onContent(recipe: Recipe) { viewModel.updateStages()}


            }

            viewModel.dataRecipe.observe(viewLifecycleOwner) {
                recipeBinding(it, binding, onInteractionListener)

            }

            val stagesAdapter = StagesAdapter(object : OnInteractionStageListener {
                override fun onClicked(stage: Stage) {
                    stageInn = stage

                    if (::stageInn.isInitialized) {
                        viewModel.editStage(stageInn)
                        findNavController().navigate(
                            R.id.action_recipeCardFragment_to_newStageFragment,
                            Bundle().apply {
                                idSubNewArg = stage.idRecipe.toString()
                                stagePosArg = stage.pos.toString()
                                stageNameArg = stage.name
                                stageTextArg = stage.description
                            }
                        )
                    }
                }
            })

            viewModel.edit(it)
            viewModel.updateStages()
            viewModel.dataStages.observe(viewLifecycleOwner) { stages ->
                if (stages.isNotEmpty()) {
                    binding.stageContent.adapter = stagesAdapter
                    stagesAdapter.submitList(stages)

                    binding.root.findViewById<TextView>(R.id.empty_view_stage).visibility = View.INVISIBLE
                    binding.root.findViewById<RecyclerView>(R.id.stageContent).visibility = View.VISIBLE
                } else {
                    binding.root.findViewById<TextView>(R.id.empty_view_stage).visibility = View.VISIBLE
                    binding.root.findViewById<RecyclerView>(R.id.stageContent).visibility = View.INVISIBLE
                }

                val itemTouchHelper by lazy {
                    val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

                        override fun onMove(recyclerView: RecyclerView,
                                            viewHolder: RecyclerView.ViewHolder,
                                            target: RecyclerView.ViewHolder): Boolean {
                            val from = viewHolder.bindingAdapterPosition
                            val to = target.bindingAdapterPosition

                            stagesAdapter.notifyItemMoved(from, to)
                            return true
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        }

                    }

                    ItemTouchHelper(simpleItemTouchCallback)
                }
                itemTouchHelper.attachToRecyclerView(binding.stageContent)



            }
        }

        binding.stageContent.visibility = View.VISIBLE
        binding.msvRecipe.updateLayoutParams { height = -1 }

        return binding.root
    }
}