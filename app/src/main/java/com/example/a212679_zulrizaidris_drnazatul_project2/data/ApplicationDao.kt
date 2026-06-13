package com.example.a212679_zulrizaidris_drnazatul_project2.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(application: JobEntity)

    @Update
    suspend fun update(application: JobEntity)

    @Delete
    suspend fun delete(application: JobEntity)

    // Flow allows the data to be observed for real-time UI updates
    @Query("SELECT * from applications ORDER BY jobTitle ASC")
    fun getAllApplications(): Flow< List<JobEntity>>
}