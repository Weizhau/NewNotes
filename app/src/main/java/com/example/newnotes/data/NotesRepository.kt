package com.example.newnotes.data

import androidx.lifecycle.LiveData

class NotesRepository(private val notesDao: NotesDao) {
    val allNotes: LiveData<List<Note>> = notesDao.getAll()

    suspend fun insert(note: Note) {
        notesDao.insertAll(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    suspend fun deleteById(id: Int) {
        notesDao.deleteById(id)
    }
}