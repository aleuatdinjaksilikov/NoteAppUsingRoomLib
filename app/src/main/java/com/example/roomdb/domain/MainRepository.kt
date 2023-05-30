package com.example.roomdb.domain

import com.example.roomdb.data.dao.NoteDao
import com.example.roomdb.data.entity.Note

class MainRepository(private val dao:NoteDao) {

    suspend fun getAllNotes() = dao.getAllNotes()

    suspend fun addNote(note: Note) = dao.addNote(note)

    suspend fun updateNote(note: Note) = dao.updateNote(note)

    suspend fun searchNotesByTitle(title:String) = dao.searchNotesByTitle(title)

    suspend fun deleteNote(note: Note) = dao.deleteNote(note)
}