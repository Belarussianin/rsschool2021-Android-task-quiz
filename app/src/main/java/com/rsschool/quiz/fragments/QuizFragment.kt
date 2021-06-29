package com.rsschool.quiz.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.R
import com.rsschool.quiz.interfaces.DataFromFragmentsToActivity
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataPasser: DataFromFragmentsToActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as DataFromFragmentsToActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.getInt(QUESTION_NUMBER_KEY) % 2 == 0) {
                requireContext().setTheme(R.style.Theme_Quiz_Second)
            } else {
                requireContext().setTheme(
                    R.style.Theme_Quiz_First
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Using data from arguments
        val questionNumber: Int =
            arguments?.getInt(QUESTION_NUMBER_KEY) ?: throw Exception("questionNumber not passed")
        val optionsArray = arguments?.getStringArray(OPTIONS_ARRAY_KEY) as Array<String>
        val isLastElement = arguments?.getBoolean(IS_LAST_KEY) ?: false
        val previousFragmentPosition = if (questionNumber > 1) questionNumber - 2 else 0

        binding.apply {
            //Question text
            question.apply {
                textSize = 35F
                text = arguments?.getString(QUESTION_KEY)
            }

            //Previous button
            previousButton.apply {
                if (questionNumber == 1) {
                    isEnabled = false
                } else {
                    setOnClickListener {
                        dataPasser.viewPager2Move(previousFragmentPosition)
                    }
                }
            }

            //Next/Submit button
            nextButton.apply {
                if (isLastElement) {
                    text = resources.getString(R.string.button_submit_text)
                    setOnClickListener {
                        dataPasser.onSubmitButtonClick()
                    }
                } else {
                    setOnClickListener {
                        dataPasser.viewPager2Move(questionNumber)
                    }
                }
            }

            //Toolbar
            toolbarQuizFragment.apply {
                title = resources.getString(R.string.quiz_toolbar_text, questionNumber)

                //Add back arrow to fragment if it's not the first one
                if (questionNumber > 1) {
                    setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
                    setNavigationOnClickListener {
                        dataPasser.viewPager2Move(previousFragmentPosition)
                    }
                }
            }

            //RadioGroup
            radioGroup.apply {
                //Make option buttons
                for (i in optionsArray.indices) {
                    addView(
                        RadioButton(requireContext()).apply {
                            id = i
                            textSize = 25F
                            text = optionsArray[i]
                        }
                    )
                }

                //Pass answer to activity
                setOnCheckedChangeListener { _, checkedId ->
                    nextButton.isEnabled = true
                    dataPasser.onOptionSelected(questionNumber, checkedId)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getInstance(
        questionNumber: Int,
        question: String,
        optionsArray: Array<String>,
        isLastElement: Boolean = false
    ) =
        QuizFragment().apply {
            arguments = bundleOf(
                QUESTION_NUMBER_KEY to questionNumber,
                QUESTION_KEY to question,
                OPTIONS_ARRAY_KEY to optionsArray,
                IS_LAST_KEY to isLastElement
            )
        }

    companion object {
        private const val QUESTION_NUMBER_KEY = "QUESTION_NUMBER"
        private const val QUESTION_KEY = "QUESTION"
        private const val OPTIONS_ARRAY_KEY = "OPTIONS_ARRAY"
        private const val IS_LAST_KEY = "LAST"
    }
}