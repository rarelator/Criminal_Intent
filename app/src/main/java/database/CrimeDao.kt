package database

import androidx.room.Dao
import androidx.room.Query
import com.example.criminal_intent.Crime
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// Data Access Object allows us to interact with the database

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime") // sql is not case sensitive --> this is sql lite
    // Flow is a coroutine, it is a suspending function so we do not need the suspend keyword
    fun getCrimes() : Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id = (:id)")
    suspend fun getCrime(id: UUID) : Crime
}