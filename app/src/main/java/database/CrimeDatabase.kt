package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminal_intent.Crime

// This annotation tells Room this class will be our database
@Database(entities = [Crime::class], version = 1)
// The database cannot handle dates so we need to create type converters
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao() : CrimeDao
}