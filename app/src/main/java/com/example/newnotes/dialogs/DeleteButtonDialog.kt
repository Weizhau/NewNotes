package com.example.newnotes.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.newnotes.R

class DeleteButtonDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val dialog = AlertDialog.Builder(it)
            dialog.setView(R.layout.note_options)

            dialog.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}