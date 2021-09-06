package com.example.movietrailer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getAllHistoryList(): List<History>

    @Insert
    fun insertFilm(history: History)

    @Query("DELETE FROM history_table WHERE filmID = :filmID")
    fun deleteFilm(filmID: Int)

    @Query("DELETE FROM history_table")
    fun deleteAllHistory()

    @Query("SELECT EXISTS (SELECT 1 FROM history_table WHERE filmID = :filmID)")
    fun existsFilm(filmID: Int): Boolean

}