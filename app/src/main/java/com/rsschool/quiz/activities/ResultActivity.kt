package com.rsschool.quiz.activities

import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rsschool.quiz.R
import com.rsschool.quiz.activities.MainActivity.Companion.EXTRA_RESULT
import com.rsschool.quiz.databinding.ActivityResultBinding
import kotlin.system.exitProcess


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get result from the intent which started this activity
        val result: Int = intent.getIntExtra(EXTRA_RESULT, -1)
        if (result == -1) throw Exception("Empty intent")
        //Apply result to the text view
        val resultTextView = binding.resultTextview.apply {
            text = resources.getString(R.string.result_text, result)
        }

        val toolbar = binding.toolbarResultActivity
        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
        toolbar.setNavigationOnClickListener {
            //super.onBackPressed()
            openMainActivity()
        }

        //Back button
        binding.backButton.setOnClickListener {
            //super.onBackPressed()
            openMainActivity()
        }

        //Exit button
        binding.exitButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
        //Open main activity
    }

    override fun onBackPressed() {
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
    }
}