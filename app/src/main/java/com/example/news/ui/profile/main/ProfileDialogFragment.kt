package com.example.news.ui.profile.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.news.R

class ProfileDialogFragment(
    private val message: String,
    private val okAction: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext(), R.style.Dialog)
            .setMessage(message)
            .setPositiveButton(getString(R.string.profile_dialog_positive)) { _, _ ->
                okAction.invoke()
            }
            .setNegativeButton(getString(R.string.profile_dialog_negative)) { _, _ ->
                dialog?.cancel()
            }
            .create()

    companion object {
        const val TAG = "com.example.news.ui.profile.main.ProfileDialogFragment"
    }
}