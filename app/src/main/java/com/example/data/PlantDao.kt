package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Query("SELECT * FROM saved_plant_diagnoses ORDER BY createdAt DESC")
    fun getAllDiagnoses(): Flow<List<PlantDiagnosisEntity>>

    @Query("SELECT * FROM saved_plant_diagnoses WHERE id = :id LIMIT 1")
    suspend fun getDiagnosisById(id: Long): PlantDiagnosisEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosis(entity: PlantDiagnosisEntity): Long

    @Query("UPDATE saved_plant_diagnoses SET isWateredThisWeek = :isWatered WHERE id = :id")
    suspend fun updateWateredStatus(id: Long, isWatered: Boolean)

    @Query("DELETE FROM saved_plant_diagnoses WHERE id = :id")
    suspend fun deleteDiagnosis(id: Long)
}
