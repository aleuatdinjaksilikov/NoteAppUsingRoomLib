package com.example.roomdb.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_table")
data class Note (
    @PrimaryKey(autoGenerate = true) val id:Int,
    val title:String,
    val content:String,
    )