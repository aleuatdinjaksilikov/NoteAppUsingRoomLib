package com.example.roomdb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdb.data.dao.NoteDao
import com.example.roomdb.data.entity.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase:RoomDatabase() {

    abstract fun getNoteDao():NoteDao

    companion object{

        const val DATABASE_NAME = "db_name"

        fun getInstance(context: Context):NoteDatabase{
            return Room.databaseBuilder(
                context,
                NoteDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}