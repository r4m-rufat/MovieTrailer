package com.example.movietrailer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun getDao(): Dao?

    companion object {
        private var INSTANCE: HistoryDatabase? = null

        fun getHistoryDatabase(context: Context): HistoryDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "HistoryDB"
                ).allowMainThreadQueries()
                    .build()
            }

            return INSTANCE!!

        }

    }

}