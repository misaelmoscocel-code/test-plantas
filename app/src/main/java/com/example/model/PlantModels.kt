package com.example.model

data class PlantSample(
    val id: String,
    val commonName: String,
    val scientificName: String,
    val category: String,
    val description: String,
    val iconName: String,
    val typicalIssue: String
)

data class LightAssessment(
    val status: String,
    val details: String,
    val luxRecommendation: String,
    val placementTip: String
)

data class HumidityAssessment(
    val status: String,
    val details: String,
    val soilCondition: String,
    val ambientAdvice: String
)

data class WateringEvent(
    val weekNumber: Int,
    val dateLabel: String,
    val suggestedAction: String,
    val amount: String,
    val isWatered: Boolean = false
)

data class WateringSchedule(
    val frequencyDescription: String,
    val recommendedAmount: String,
    val waterTypeTip: String,
    val weeklyCalendar: List<WateringEvent>
)

data class PlantDiagnosis(
    val plantName: String,
    val scientificName: String,
    val category: String,
    val healthScore: Int,
    val healthStatus: String,
    val visualSymptoms: List<String>,
    val lightAssessment: LightAssessment,
    val humidityAssessment: HumidityAssessment,
    val wateringSchedule: WateringSchedule,
    val urgentActions: List<String>,
    val preventiveTips: List<String>,
    val imageUri: String? = null,
    val samplePlantId: String? = null
)
