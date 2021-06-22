package com.rsschool.quiz.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.interfaces.DataFromFragmentsToActivity

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
        val toolbar = binding.toolbarQuizFragment
        val radioGroup = binding.radioGroup
        val previousButton = binding.previousButton
        val nextButton = binding.nextButton

        //Using data from arguments
        val questionNumber: Int =
            arguments?.getInt(QUESTION_NUMBER_KEY) ?: throw Exception("ARGUMENTS not passed")
        val questionText: String? = arguments?.getString(QUESTION_KEY)
        val optionsArray = arguments?.getStringArray(OPTIONS_ARRAY_KEY) as Array<String>
            ?: throw Exception("ARGUMENTS not passed")
        val isLastElement = arguments?.getBoolean(IS_LAST_KEY) ?: false

        //Previous button
        if (questionNumber == 1) {
            previousButton.isEnabled = false
        } else {
            previousButton.setOnClickListener {
                dataPasser.viewPager2Move(-1)
            }
        }

        //Next/Submit button
        if (isLastElement) {
            nextButton.text = resources.getString(R.string.button_submit_text)
            nextButton.setOnClickListener {
                dataPasser.onSubmitButtonClick()
            }
        } else {
            nextButton.setOnClickListener {
                dataPasser.viewPager2Move(1)
            }
        }

        //Toolbar title
        toolbar.title = resources.getString(R.string.quiz_toolbar_text, questionNumber)

        //Add back arrow to fragment if it's not the first
        if (questionNumber > 1) {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
            toolbar.setNavigationOnClickListener {
                dataPasser.viewPager2Move(-1)
            }
        }

        //Question text
        binding.question.textSize = 35F
        binding.question.text = questionText

        ////Option buttons
        //Make option buttons
        for (i in optionsArray.indices) {
            val newButton = RadioButton(context)
            newButton.id = i + 1
            newButton.textSize = 25F
            newButton.text = optionsArray[i]
            radioGroup.addView(newButton)
        }
        //Pass answer to activity
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            nextButton.isEnabled = true
            dataPasser.onOptionSelected(questionNumber, checkedId)
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
        isLastElement: Boolean = false,
        answer: Int? = null
    ) =
        QuizFragment().apply {
            arguments = bundleOf(
                QUESTION_NUMBER_KEY to questionNumber,
                QUESTION_KEY to question,
                OPTIONS_ARRAY_KEY to optionsArray,
                IS_LAST_KEY to isLastElement,
                ANSWER_KEY to answer
            )
        }

    companion object {
        const val QUESTION_NUMBER_KEY = "QUESTION_NUMBER"
        const val QUESTION_KEY = "QUESTION"
        const val OPTIONS_ARRAY_KEY = "OPTIONS_ARRAY"
        const val ANSWER_KEY = "ANSWER"
        const val IS_LAST_KEY = "LAST"
    }
}