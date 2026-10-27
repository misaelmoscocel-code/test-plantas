package com.example.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PlantDiagnosisEntity
import com.example.data.PlantRepository
import com.example.model.PlantDiagnosis
import com.example.model.PlantSample
import com.example.network.PlantAnalyzerService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject

sealed interface PlantUiState {
    data object Idle : PlantUiState
    data class Analyzing(val stage: String) : PlantUiState
    data class Diagnosed(val diagnosis: PlantDiagnosis, val isSaved: Boolean = false) : PlantUiState
    data class Error(val message: String) : PlantUiState
}

class PlantCareViewModel(
    private val repository: PlantRepository,
    private val analyzerService: PlantAnalyzerService = PlantAnalyzerService()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlantUiState>(PlantUiState.Idle)
    val uiState: StateFlow<PlantUiState> = _uiState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    private val _selectedSample = MutableStateFlow<PlantSample?>(null)
    val selectedSample: StateFlow<PlantSample?> = _selectedSample.asStateFlow()

    val savedPlants: StateFlow<List<PlantDiagnosisEntity>> = repository.allDiagnoses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val samplesList = listOf(
        PlantSample(
            id = "sample_agave",
            commonName = "Agave Azul",
            scientificName = "Agave tequilana",
            category = "AGAVE",
            description = "Suculenta xerófita con pencas gruesas y espinas marginales.",
            iconName = "agave",
            typicalIssue = "Signos de leve estrés lumínico y sed en las puntas"
        ),
        PlantSample(
            id = "sample_cactus",
            commonName = "Cactus Erizo",
            scientificName = "Echinocactus grusonii",
            category = "CACTUS",
            description = "Cactus globular con espinas doradas y areolas prominentes.",
            iconName = "cactus",
            typicalIssue = "Sustrato con humedad residual en la base de la maceta"
        ),
        PlantSample(
            id = "sample_bamboo",
            commonName = "Bambú de la Suerte",
            scientificName = "Dracaena sanderiana",
            category = "BAMBU",
            description = "Tallos verdes anillados en agua o hidrogel.",
            iconName = "bamboo",
            typicalIssue = "Puntas amarillentas por sensibilidad a sales o cloro"
        ),
        PlantSample(
            id = "sample_indoor",
            commonName = "Planta de Interior Tropical",
            scientificName = "Monstera / Ficus",
            category = "INTERIOR",
            description = "Follaje verde ornamental en maceta de interior.",
            iconName = "tropical",
            typicalIssue = "Bordes ligeramente secos por baja humedad ambiental"
        )
    )

    fun selectSample(sample: PlantSample) {
        _selectedSample.value = sample
        _selectedImageUri.value = null
        val bitmap = createSampleBitmap(sample)
        _selectedBitmap.value = bitmap
    }

    fun selectImageUri(uri: Uri, context: Context) {
        _selectedImageUri.value = uri
        _selectedSample.value = null
        try {
            @Suppress("DEPRECATION")
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            _selectedBitmap.value = bitmap
        } catch (e: Exception) {
            Log.e("PlantCareVM", "Failed to load bitmap from uri", e)
        }
    }

    fun captureBitmap(bitmap: Bitmap) {
        _selectedBitmap.value = bitmap
        _selectedImageUri.value = null
        _selectedSample.value = null
    }

    fun analyzePlant() {
        val bitmap = _selectedBitmap.value
        val sample = _selectedSample.value
        if (bitmap == null && sample == null) {
            _uiState.value = PlantUiState.Error("Por favor sube una foto o selecciona una planta de muestra.")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = PlantUiState.Analyzing("Identificando morfología botánica y especie…")
                delay(700)
                _uiState.value = PlantUiState.Analyzing("Evaluando turgencia foliar, coloración y clorosis…")
                delay(700)
                _uiState.value = PlantUiState.Analyzing("Calculando requerimientos de radiación lumínica y balance hídrico…")

                val targetBitmap = bitmap ?: createSampleBitmap(sample ?: samplesList[0])
                val hint = sample?.let { "${it.commonName} (${it.scientificName}) - ${it.typicalIssue}" }

                val diagnosis = analyzerService.analyzePlantImage(
                    bitmap = targetBitmap,
                    sampleHint = hint
                )

                _uiState.value = PlantUiState.Diagnosed(
                    diagnosis = diagnosis.copy(
                        imageUri = _selectedImageUri.value?.toString(),
                        samplePlantId = sample?.id
                    ),
                    isSaved = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("PlantCareVM", "Diagnosis analysis failed", e)
                _uiState.value = PlantUiState.Error("No se pudo completar el análisis: ${e.localizedMessage ?: "Error desconocido"}")
            }
        }
    }

    fun toggleWateringEvent(eventIndex: Int) {
        val current = _uiState.value
        if (current is PlantUiState.Diagnosed) {
            val oldCalendar = current.diagnosis.wateringSchedule.weeklyCalendar
            if (eventIndex in oldCalendar.indices) {
                val updatedCalendar = oldCalendar.mapIndexed { index, event ->
                    if (index == eventIndex) event.copy(isWatered = !event.isWatered) else event
                }
                val updatedSchedule = current.diagnosis.wateringSchedule.copy(weeklyCalendar = updatedCalendar)
                val updatedDiagnosis = current.diagnosis.copy(wateringSchedule = updatedSchedule)
                _uiState.value = current.copy(diagnosis = updatedDiagnosis)
            }
        }
    }

    fun saveCurrentDiagnosis() {
        val current = _uiState.value
        if (current is PlantUiState.Diagnosed && !current.isSaved) {
            viewModelScope.launch {
                try {
                    val d = current.diagnosis
                    val jsonSummary = JSONObject().apply {
                        put("plantName", d.plantName)
                        put("scientificName", d.scientificName)
                        put("category", d.category)
                        put("healthScore", d.healthScore)
                        put("healthStatus", d.healthStatus)
                        put("lightStatus", d.lightAssessment.status)
                        put("humidityStatus", d.humidityAssessment.status)
                        put("wateringFrequency", d.wateringSchedule.frequencyDescription)
                    }.toString()

                    val entity = PlantDiagnosisEntity(
                        plantName = d.plantName,
                        scientificName = d.scientificName,
                        category = d.category,
                        healthScore = d.healthScore,
                        healthStatus = d.healthStatus,
                        lightStatus = d.lightAssessment.status,
                        humidityStatus = d.humidityAssessment.status,
                        wateringFrequency = d.wateringSchedule.frequencyDescription,
                        urgentActionSummary = d.urgentActions.firstOrNull() ?: "Verificar drenaje del sustrato",
                        rawJson = jsonSummary,
                        imageUri = d.imageUri,
                        samplePlantId = d.samplePlantId,
                        isWateredThisWeek = d.wateringSchedule.weeklyCalendar.firstOrNull()?.isWatered ?: false
                    )

                    repository.insertDiagnosis(entity)
                    _uiState.value = current.copy(isSaved = true)
                } catch (e: Exception) {
                    Log.e("PlantCareVM", "Failed to save diagnosis", e)
                }
            }
        }
    }

    fun toggleSavedPlantWatered(id: Long, currentValue: Boolean) {
        viewModelScope.launch {
            repository.toggleWateredStatus(id, !currentValue)
        }
    }

    fun deleteSavedPlant(id: Long) {
        viewModelScope.launch {
            repository.deleteDiagnosis(id)
        }
    }

    fun resetDiagnosis() {
        _uiState.value = PlantUiState.Idle
        _selectedBitmap.value = null
        _selectedImageUri.value = null
        _selectedSample.value = null
    }

    private fun createSampleBitmap(sample: PlantSample): Bitmap {
        val width = 400
        val height = 400
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint().apply {
            color = when (sample.category) {
                "AGAVE" -> AndroidColor.parseColor("#E8F5E9")
                "CACTUS" -> AndroidColor.parseColor("#FFF3E0")
                "BAMBU" -> AndroidColor.parseColor("#E0F2F1")
                else -> AndroidColor.parseColor("#F1F8E9")
            }
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val circlePaint = Paint().apply {
            color = when (sample.category) {
                "AGAVE" -> AndroidColor.parseColor("#2E7D32")
                "CACTUS" -> AndroidColor.parseColor("#E65100")
                "BAMBU" -> AndroidColor.parseColor("#00796B")
                else -> AndroidColor.parseColor("#33691E")
            }
            isAntiAlias = true
        }
        canvas.drawCircle(width / 2f, height / 2f, 130f, circlePaint)

        val textPaint = Paint().apply {
            color = AndroidColor.WHITE
            textSize = 28f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText(sample.commonName, width / 2f, height / 2f + 10f, textPaint)

        return bitmap
    }
}

class PlantCareViewModelFactory(
    private val repository: PlantRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantCareViewModel::class.java)) {
            return PlantCareViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
