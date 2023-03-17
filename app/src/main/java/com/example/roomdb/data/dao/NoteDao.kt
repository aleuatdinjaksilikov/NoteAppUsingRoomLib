package com.example.roomdb.data.dao

import androidx.room.*
import com.example.roomdb.data.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM my_table")
    suspend fun getAllNotes():List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM my_table WHERE title LIKE '%' || :name || '%'")
    suspend fun searchNotesByTitle(name:String): List<Note>

    @Delete(entity = Note::class)
    suspend fun deleteNote(note: Note)

}