package com.example.newnotes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAll(): LiveData<List<Note>>

    @Insert
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note WHERE id = :id")
    fun deleteById(id: Int)
}