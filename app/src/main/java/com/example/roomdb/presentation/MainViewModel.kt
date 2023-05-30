package com.example.roomdb.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roomdb.data.NoteDatabase
import com.example.roomdb.data.entity.Note
import com.example.roomdb.domain.MainRepository
git add README.md
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MainRepository(NoteDatabase.getInstance(application).getNoteDao())

    private val _getAllNotesLiveData = MutableLiveData<List<Note>>()
    val getAllNotesLiveData: LiveData<List<Note>> get() = _getAllNotesLiveData

    suspend fun getAllNotes() {
        _getAllNotesLiveData.value = repo.getAllNotes()
    }

    suspend fun addNote(note: Note) {
        repo.addNote(note = note)
    }

    suspend fun updateNote(note: Note) {
        repo.updateNote(note)

    }

    suspend fun searchNoteByTitle(name: String) {
        _getAllNotesLiveData.value = repo.searchNotesByTitle(name)

    }

    suspend fun deleteNote(note: Note) {
        repo.deleteNote(note)

    }

}