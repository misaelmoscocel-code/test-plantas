package com.example.data

import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {
    val allDiagnoses: Flow<List<PlantDiagnosisEntity>> = plantDao.getAllDiagnoses()

    suspend fun insertDiagnosis(entity: PlantDiagnosisEntity): Long {
        return plantDao.insertDiagnosis(entity)
    }

    suspend fun toggleWateredStatus(id: Long, isWatered: Boolean) {
        plantDao.updateWateredStatus(id, isWatered)
    }

    suspend fun deleteDiagnosis(id: Long) {
        plantDao.deleteDiagnosis(id)
    }
}
