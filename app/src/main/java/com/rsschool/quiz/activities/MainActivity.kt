package com.rsschool.quiz.activities

import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rsschool.quiz.adapters.QuizAdapter
import com.rsschool.quiz.databinding.ActivityMainBinding
import com.rsschool.quiz.interfaces.DataFromFragmentsToActivity
import kotlin.math.min
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), DataFromFragmentsToActivity {
    private lateinit var adapter: QuizAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = QuizAdapter(this)
        viewPager2 = binding.pager
        viewPager2.isUserInputEnabled = false
        viewPager2.isSaveFromParentEnabled = false
        viewPager2.adapter = adapter
    }

    override fun onSubmitButtonClick() {
        var score = 0
        for (i in answers.indices) {
            if (answers[i] == rightAnswers[i]) {
                score += 1
            }
        }
        val result: Int = (100 / (rightAnswers.size.toFloat() / score.toFloat())).toInt()
        //Calculating right answers percent
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(EXTRA_RESULT, result)
        }
        startActivity(intent)
        //Open result activity
        this.finish()
    }

    override fun onOptionSelected(questionNumber: Int, answer: Int) {
        answers[questionNumber - 1] = answer
    }

    //getItem(-1) for previous
    override fun viewPager2Move(move: Int) {
        viewPager2.apply {
            setCurrentItem(currentItem + move, true)
        }
    }

    override fun onBackPressed() {
        if (viewPager2.currentItem <= 0) {
            val alertDialog = MaterialAlertDialogBuilder(this)
                .setTitle("Do you want to exit?")
                .setCancelable(false)
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("Yes") { _, _ ->
                    moveTaskToBack(true)
                    Process.killProcess(Process.myPid())
                    exitProcess(1)
                }
                .create()
            alertDialog.show()
        } else {
            viewPager2.currentItem -= 1
        }
    }

    companion object {
        const val EXTRA_RESULT = "com.rsschool.quiz.RESULT"
        val questions: Array<String?> =
            arrayOf(
                "What can you benefit from using Kotlin for Android development?",
                "What is the best programming language for everything?",
                "What is something you can never seem to finish?",
                "Pizza or tacos?",
                "What's the worst movie?"
            )
        val options: Array<Array<String?>?> = arrayOf(
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
        val rightAnswers = arrayOf(2, 4, 2, 4, 6)

        ////NULL SAFETY CHECK
        //val questions: Array<String?>? = null
        //val options: Array<Array<String?>?>? = null

        val itemsCount = questions?.size?.let { options?.let { it1 -> min(it, it1.size) } }

        @JvmStatic
        private var answers = arrayOfNulls<Int>(itemsCount)
    }
}