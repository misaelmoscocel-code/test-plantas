package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_plant_diagnoses")
data class PlantDiagnosisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val plantName: String,
    val scientificName: String,
    val category: String,
    val healthScore: Int,
    val healthStatus: String,
    val lightStatus: String,
    val humidityStatus: String,
    val wateringFrequency: String,
    val urgentActionSummary: String,
    val rawJson: String,
    val imageUri: String?,
    val samplePlantId: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val isWateredThisWeek: Boolean = false
)
