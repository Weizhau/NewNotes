package com.example.newnotes.listeners

import com.example.newnotes.data.Note

interface NoteClickListener {
    fun onNoteLongClick(note: Note)
    fun onNoteClick(note: Note)
}