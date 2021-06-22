package com.rsschool.quiz.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rsschool.quiz.activities.MainActivity
import com.rsschool.quiz.fragments.QuizFragment

class QuizAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = MainActivity.itemsCount ?: 1

    override fun createFragment(position: Int) = QuizFragment().getInstance(
        position + 1,
        MainActivity.questions?.get(position) ?: "Something went wrong",
        (MainActivity.options.takeIf { it.isNotEmpty() }?.get(position)?: arrayOf("Yes")) as Array<String>,
        position + 1 == itemCount
    //MAXIMUM NULL SAFETY
    )
}