package com.example.movietrailer.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
class History(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var filmID: Int,
    var filmTitle: String? = null,
    var filmImage: String? = null,
    var filmGenres: String? = null,
    var filmRating: Double? = null
)