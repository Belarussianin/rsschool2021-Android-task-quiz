package com.rsschool.quiz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.rsschool.quiz.INVALID_VALUE
import com.rsschool.quiz.adapters.QuizAdapter
import com.rsschool.quiz.databinding.ActivityMainBinding
import com.rsschool.quiz.exitDialog
import com.rsschool.quiz.interfaces.DataFromFragmentsToActivity
import kotlin.math.min

class MainActivity : AppCompatActivity(), DataFromFragmentsToActivity {
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        answers.fill(INVALID_VALUE)

        quizAdapter = QuizAdapter(this)
        viewPager2 = binding.pager.apply {
            isUserInputEnabled = false
            isSaveFromParentEnabled = false
            adapter = quizAdapter
        }
    }

    override fun onSubmitButtonClick() {
        if (answers.contains(INVALID_VALUE)) {
            viewPager2.currentItem = 0
            Toast.makeText(applicationContext, "Not all questions answered", Toast.LENGTH_SHORT)
                .show()
        } else {
            val chosenAnswers = arrayListOf<String>()
            var score = 0

            //Calculating right answers percent
            for (i in rightAnswersIndices.indices) {
                if (answers[i] == rightAnswersIndices[i]) {
                    score += 1
                }
                chosenAnswers.add(options[i][answers[i]])
            }
            val result: Int = (100 / (rightAnswersIndices.size.toFloat() / score.toFloat())).toInt()

            //Start result activity, finish() this one
            startActivity(
                Intent(this, ResultActivity::class.java).apply {
                    putExtra(EXTRA_RESULT, result)
                    putStringArrayListExtra(EXTRA_QUESTIONS, questions)
                    putStringArrayListExtra(EXTRA_ANSWERS, chosenAnswers)
                }
            )
            this.finish()
        }
    }

    override fun onOptionSelected(questionNumber: Int, answerId: Int) {
        if (questionNumber < 1) throw Exception("questionNumber < 1")
        answers[questionNumber - 1] = answerId
    }

    override fun viewPager2Move(position: Int) {
        viewPager2.setCurrentItem(position, true)
    }

    override fun onBackPressed() {
        if (viewPager2.currentItem <= 0) {
            exitDialog()
        } else {
            viewPager2.currentItem -= 1
        }
    }

    companion object {
        const val EXTRA_RESULT = "com.rsschool.quiz.RESULT"
        const val EXTRA_QUESTIONS = "com.rsschool.quiz.QUESTIONS"
        const val EXTRA_ANSWERS = "com.rsschool.quiz.ANSWERS"

        val questions: ArrayList<String> =
            arrayListOf(
                "What can you benefit from using Kotlin for Android development?",
                "What is the best programming language for everything?",
                "What is something you can never seem to finish?",
                "Pizza or tacos?",
                "What's the worst movie?"
            )
        val options: Array<Array<String>> = arrayOf(
            arrayOf(
                "Nothing", "Something", "Maybe", "I don't know", "Money", "Headache"
            ),
            arrayOf(
                "C", "Python", "C++", "Java", "C#", "Kotlin", "Assembler"
            ),
            arrayOf(
                "Living", "Nothing", "Asking", "Programming", "Fishing", "Cycling", "Sleeping"
            ),
            arrayOf(
                "Yes", "No", "Maybe", "I don't know", "Yellow"
            ),
            arrayOf(
                "Titanic", "Tom&Jerry", "1917", "M.I.B.", "Harry Potter", "Terminator: Dark Fate"
            )
        )
        val rightAnswersIndices = arrayOf(1, 3, 1, 3, 5)

        val itemsCount = min(questions.size, options.size)

        private val answers: Array<Int> = rightAnswersIndices.clone().apply { fill(INVALID_VALUE) }
    }
}