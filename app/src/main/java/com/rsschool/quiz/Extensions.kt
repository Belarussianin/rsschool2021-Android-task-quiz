package com.rsschool.quiz

import android.app.Activity
import android.os.Process
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rsschool.quiz.activities.MainActivity
import kotlin.system.exitProcess

const val INVALID_VALUE = -1
private const val BACK_PRESSED_EXIT_STATUS = 228

fun Activity.exitDialog() {
    MaterialAlertDialogBuilder(this)
        .setTitle("Do you want to exit?")
        .setCancelable(false)
        .setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        .setPositiveButton("Yes") { _, _ ->
            moveTaskToBack(true)
            Process.killProcess(Process.myPid())
            exitProcess(BACK_PRESSED_EXIT_STATUS)
        }
        .create()
        .show()
}

fun Fragment.mainActivity() = requireActivity() as MainActivity