package com.example.newnotes.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageButton
import com.example.newnotes.R
import com.example.newnotes.data.Note
import com.example.newnotes.data.NotesViewModel

class OptionsDialog(
    context: Context,
    val notesViewModel: NotesViewModel,
    val note: Note
) : Dialog(context) {
    override fun onStart() {
        super.onStart()
        this.apply {
            setContentView(R.layout.note_options)
            findViewById<ImageButton>(R.id.red_delete_button).setOnClickListener {
                this.cancel()
                notesViewModel.delete(note)
            }
            findViewById<ImageButton>(R.id.important_button).setOnClickListener {
                this.cancel()
//                note.isImportant = true
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}