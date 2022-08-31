package ru.netology.nerecipe.activity

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.FragmentNewStageBinding
import ru.netology.nerecipe.repository.getConstDraftPostId
import ru.netology.nerecipe.util.AndroidUtils
import ru.netology.nerecipe.util.StringArg
import ru.netology.nerecipe.viewmodel.DraftContentRecipeViewModel
import ru.netology.nerecipe.viewmodel.RecipeViewModel


class NewStageFragment : Fragment() {
    companion object {
        var Bundle.idSubNewArg: String? by StringArg
        var Bundle.stagePosArg: String? by StringArg
        var Bundle.stageNameArg: String? by StringArg
        var Bundle.stageTextArg: String? by StringArg
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val viewModelForDraft: DraftContentRecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private lateinit var draft: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentNewStageBinding.inflate(
            inflater,
            container,
            false
        )

        var draftContent: String? = null


        binding.subPos.text = "${resources.getString(R.string.description_pos_stage)}${arguments?.stagePosArg?.toInt()}"
        arguments?.stageNameArg?.let(binding.stageNameEdit::setText)
        arguments?.stageTextArg?.let(binding.stageEdit::setText)
            ?: binding.stageEdit.setText(
                viewModelForDraft.getRecipeById(getConstDraftPostId())?.content
            ).let {
                draftContent = it.toString()
            }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            draft = binding.stageEdit.text.toString()
            if (::draft.isInitialized)
                viewModelForDraft.save(draft)
            findNavController().navigateUp()
        }

        callback.isEnabled = true

        binding.ok.setOnClickListener {
            if (binding.stageNameEdit.text.isNullOrBlank() ||
                binding.stageEdit.text.isNullOrBlank()
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

            if (!binding.stageNameEdit.text.isNullOrBlank() ||
                !binding.stageEdit.text.isNullOrBlank()
            ) {
                if (draftContent != null && viewModel.edited.value?.id != 0L) {
                    viewModel.toEmpty()
                }
                viewModel.changeStageContent(
                    arguments?.stagePosArg.let { it?.toInt() } ?: 0,
                    binding.stageNameEdit.text.toString(),
                    binding.stageEdit.text.toString()
                )
                viewModel.saveSub()
            }
            viewModelForDraft.save("")
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}