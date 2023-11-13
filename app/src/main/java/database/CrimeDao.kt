package database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.criminal_intent.Crime
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime") // sql is not case sensitive --> this is sql lite
    fun getCrimes() : Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id = (:id)")
    suspend fun getCrime(id: UUID) : Crime

    @Update
    suspend fun updateCrime(crime: Crime)
}