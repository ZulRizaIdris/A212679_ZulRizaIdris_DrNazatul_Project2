package com.example.a212679_zulrizaidris_drnazatul_project2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JobEntity::class], version = 1, exportSchema = false)
abstract class CareerPulseDatabase : RoomDatabase() {

    abstract fun applicationDao(): ApplicationDao

    companion object {
        @Volatile
        private var Instance: CareerPulseDatabase? = null

        fun getDatabase(context: Context): CareerPulseDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CareerPulseDatabase::class.java, "careerpulse_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}