package com.rsschool.quiz.activities

import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rsschool.quiz.INVALID_VALUE
import com.rsschool.quiz.R
import com.rsschool.quiz.activities.MainActivity.Companion.EXTRA_ANSWERS
import com.rsschool.quiz.activities.MainActivity.Companion.EXTRA_QUESTIONS
import com.rsschool.quiz.activities.MainActivity.Companion.EXTRA_RESULT
import com.rsschool.quiz.databinding.ActivityResultBinding
import com.rsschool.quiz.exitDialog

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get result from the intent which started this activity
        val result: Int = intent.getIntExtra(EXTRA_RESULT, INVALID_VALUE)
        if (result == INVALID_VALUE) throw Exception("Empty intent")

        binding.apply {
            resultTextview.text = resources.getString(R.string.result_text, result)

            toolbarResultActivity.apply {
                setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
                setNavigationOnClickListener {
                    openMainActivity()
                }
            }

            shareButton.setOnClickListener {
                shareResult()
            }

            backButton.setOnClickListener {
                openMainActivity()
            }

            exitButton.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun shareResult() {
        val questions = intent.getStringArrayListExtra(EXTRA_QUESTIONS)
            ?: throw Exception("Empty questions arg")
        val answers = intent.getStringArrayListExtra(EXTRA_ANSWERS)
            ?: throw Exception("Empty answers arg")
        val result: Int = intent.getIntExtra(EXTRA_RESULT, INVALID_VALUE)
        if (result == INVALID_VALUE) throw Exception("Empty result arg")

        var sendText: String = resources.getString(R.string.result_text, result)
        for (i in answers.indices) {
            sendText = sendText.plus(
                "\n Question ${i + 1}: ${questions[i]}" +
                        "\n Your answer: ${answers[i]}\n"
            )
        }

        startActivity(
            Intent(ACTION_SEND).apply {
                type = "text/plain"
                putExtra(EXTRA_TEXT, sendText.trimMargin())
            }
        )
    }

    private fun openMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
        this.finish()
    }

    override fun onBackPressed() {
        exitDialog()
    }
}