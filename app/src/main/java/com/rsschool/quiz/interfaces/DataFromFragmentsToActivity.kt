package com.rsschool.quiz.interfaces

interface DataFromFragmentsToActivity {
    fun onOptionSelected(questionNumber: Int, answerId: Int)
    fun onSubmitButtonClick()
    fun viewPager2Move(position: Int)
}