package com.rsschool.quiz.interfaces

interface DataFromFragmentsToActivity {
    fun onOptionSelected(questionNumber: Int, answer: Int)
    fun onSubmitButtonClick()
    fun viewPager2Move(move: Int)
}