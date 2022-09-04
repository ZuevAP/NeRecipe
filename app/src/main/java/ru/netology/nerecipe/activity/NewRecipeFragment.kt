package ru.netology.nerecipe.activity

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.NewStageFragment.Companion.idSubNewArg
import ru.netology.nerecipe.activity.NewStageFragment.Companion.stagePosArg
import ru.netology.nerecipe.activity.NewStageFragment.Companion.stageNameArg
import ru.netology.nerecipe.activity.NewStageFragment.Companion.stageTextArg
import ru.netology.nerecipe.adapter.*
import ru.netology.nerecipe.databinding.FragmentNewRecipeBinding
import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.dto.Stage
import ru.netology.nerecipe.repository.getConstDraftPostId
import ru.netology.nerecipe.util.AndroidUtils
import ru.netology.nerecipe.util.StringArg
import ru.netology.nerecipe.viewmodel.DraftContentRecipeViewModel
import ru.netology.nerecipe.viewmodel.RecipeViewModel


class NewRecipeFragment : Fragment() {
    companion object {
        var Bundle.idSubArg: String? by StringArg
        var Bundle.idPosArg: String? by StringArg
        var Bundle.authorArg: String? by StringArg
        var Bundle.nameArg: String? by StringArg
        var Bundle.catArg: String? by StringArg
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val viewModelForDraft: DraftContentRecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private lateinit var draft: String
    private lateinit var stageInn: Stage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentNewRecipeBinding.inflate(
            inflater,
            container,
            false
        )

        var draftContent: String? = null

        val idRecipe = arguments?.idSubArg?.toLong() ?: 0


        val categoryAdapter = CategoryNewRecipeAdapter(object : OnInteractionCatListener {

            override fun onClicked(category: Category) {

               binding.catEdit.text = category.titleRu.trim()

            }
        })
        binding.catEditList.adapter = categoryAdapter




        val stagesAdapter = StagesAdapter(object : OnInteractionStageListener {
            override fun onClicked(stage: Stage) {
                stageInn = stage
                binding.edit.setText("Шаг ${stage.pos}\n${stage.name}\n${stage.description}")
            }
        })
        binding.stageEditList.adapter = stagesAdapter
        viewModel.dataStages.observe(viewLifecycleOwner) { stages ->
            if (idRecipe != 0L && stages.isNotEmpty()) {
                stagesAdapter.submitList(stages)
                binding.root.findViewById<TextView>(R.id.empty_view).visibility = View.INVISIBLE
                binding.root.findViewById<RecyclerView>(R.id.stageEditList).visibility = View.VISIBLE
            } else {
                binding.root.findViewById<TextView>(R.id.empty_view).visibility = View.VISIBLE
                binding.root.findViewById<RecyclerView>(R.id.stageEditList).visibility = View.INVISIBLE
            }
        }

        arguments?.authorArg?.let(binding.authorEdit::setText)


        arguments?.nameArg?.let(binding.nameEdit::setText)


        arguments?.catArg?.let(binding.catEdit::setText)


        arguments?.textArg?.let(binding.edit::setText)
            ?: binding.edit.setText(
                viewModelForDraft.getRecipeById(getConstDraftPostId())?.content
            ).let {
                draftContent = it.toString()
            }

        arguments?.stageTextArg?.let(binding.edit::setText)
            ?: binding.edit.setText(R.string.reminder_stage_edit)


        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            draft = binding.edit.text.toString()
            if (::draft.isInitialized)
                viewModelForDraft.save(draft)
            findNavController().navigateUp()
        }

        callback.isEnabled = true

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.add -> {
                            if (idRecipe == 0L) {
                                Snackbar.make(
                                    binding.root, R.string.error_parent_object,
                                    BaseTransientBottomBar.LENGTH_INDEFINITE
                                )
                                    .setAction(android.R.string.ok) {

                                    }
                                    .show()
                            } else {
                                findNavController().navigate(
                                    R.id.action_newRecipeFragment_to_newStageFragment,
                                    Bundle().apply {
                                        idSubNewArg = idRecipe.toString()
                                        val stagesCount = viewModel.dataStages.value
                                        stagePosArg =
                                            if (stagesCount != null)
                                                (stagesCount.count() + 1).toString()
                                            else 1.toString()
                                        stageTextArg = ""
                                        stageNameArg = ""
                                    }
                                )
                            }
                            true
                        }
                        R.id.edit -> {
                            if (::stageInn.isInitialized) {
                                viewModel.editStage(stageInn)
                                findNavController().navigate(
                                    R.id.action_newRecipeFragment_to_newStageFragment,
                                    Bundle().apply {
                                        idSubNewArg = idRecipe.toString()
                                        stagePosArg = stageInn.pos.toString()
                                        stageNameArg = stageInn.name
                                        stageTextArg = stageInn.description
                                    }
                                )
                            }
                            true
                        }
                        R.id.remove -> {
                            if (::stageInn.isInitialized) {
                                if (idRecipe == 0L) {
                                    Snackbar.make(
                                        binding.root, R.string.error_parent_object,
                                        BaseTransientBottomBar.LENGTH_INDEFINITE
                                    )
                                        .setAction(android.R.string.ok) {
                                            //findNavController().navigateUp()
                                        }
                                        .show()
                                } else {
                                    viewModel.removeSubById(stageInn)
                                    viewModel.updateStages()
                                    binding.edit.setText(R.string.reminder_stage_edit)
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }

        binding.ok.setOnClickListener {
            val cat = binding.catEdit.text.trim().toString()
            if (
                binding.authorEdit.text.isNullOrBlank() ||
                binding.nameEdit.text.isNullOrBlank() ||
                cat == categoryAdapter.listCategory[0].titleRu.trim()
            ) {
                Snackbar.make(
                    binding.root, R.string.error_empty_content,
                    BaseTransientBottomBar.LENGTH_INDEFINITE
                )
                    .setAction(android.R.string.ok) {

                    }
                    .show()
                return@setOnClickListener
            }

            if (!binding.edit.text.isNullOrBlank()) {
                if (draftContent != null && viewModel.edited.value?.id != 0L) {
                    viewModel.toEmpty()
                }
                viewModel.changeContent(
                    arguments?.idPosArg.let { it?.toInt() } ?: 0,
                    binding.authorEdit.text.toString(),
                    binding.nameEdit.text.toString(),
                    binding.catEdit.text.toString(),
                    binding.edit.text.toString()
                )
                viewModel.save()
            }
            viewModelForDraft.save("")
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }
}