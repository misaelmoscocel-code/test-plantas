package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.model.PlantSample
import com.example.ui.components.HealthScoreGauge
import com.example.ui.components.HumidityAssessmentCard
import com.example.ui.components.InteractiveWateringTable
import com.example.ui.components.LightAssessmentCard
import com.example.ui.components.RemedyChecklistCard
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MintPale
import com.example.ui.theme.SageGreen
import com.example.ui.theme.TerracottaAccent
import com.example.viewmodel.PlantCareViewModel
import com.example.viewmodel.PlantUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiagnosisScreen(
    viewModel: PlantCareViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val selectedUri by viewModel.selectedImageUri.collectAsState()
    val selectedBitmap by viewModel.selectedBitmap.collectAsState()
    val selectedSample by viewModel.selectedSample.collectAsState()

    // 0-permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.selectImageUri(uri, context)
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.captureBitmap(bitmap)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = MintPale,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Diagnóstico Botánico",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Agaves, Cactus y Bambú de la Suerte",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SageGreen
                        )
                    }
                }
            }
        }

        // Section 1: Upload / Capture & Sample Selector (Visible when Idle or Error or selecting)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Sube una fotografía de tu planta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Toma una foto cercana a las hojas, espinas, tallo o sustrato",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pick_photo_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Galería")
                        }

                        OutlinedButton(
                            onClick = { cameraLauncher.launch(null) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("take_photo_button")
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cámara")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sample Plants Carousel
                    Text(
                        text = "O analiza una muestra botánica de prueba:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(viewModel.samplesList) { sample ->
                            SamplePlantChip(
                                sample = sample,
                                isSelected = selectedSample?.id == sample.id,
                                onSelect = { viewModel.selectSample(sample) }
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Image Preview & Trigger Button
        if (selectedBitmap != null || selectedUri != null || selectedSample != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedBitmap != null) {
                                Image(
                                    bitmap = selectedBitmap!!.asImageBitmap(),
                                    contentDescription = "Planta seleccionada",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (selectedUri != null) {
                                AsyncImage(
                                    model = selectedUri,
                                    contentDescription = "Planta seleccionada",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            if (selectedSample != null) {
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = EmeraldDark.copy(alpha = 0.85f)
                                ) {
                                    Text(
                                        text = "${selectedSample?.commonName} (Muestra)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.analyzePlant() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("analyze_plant_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            enabled = uiState !is PlantUiState.Analyzing
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Diagnosticar Salud con IA Multimodal",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Diagnostic States
        when (val state = uiState) {
            is PlantUiState.Analyzing -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = EmeraldPrimary,
                                modifier = Modifier.size(54.dp),
                                strokeWidth = 5.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Analizando Planta…",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.stage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }
            }

            is PlantUiState.Error -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Error en el Diagnóstico",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFB71C1C)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.analyzePlant() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }

            is PlantUiState.Diagnosed -> {
                val d = state.diagnosis

                // Plant Identification Header Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldPrimary)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = d.plantName,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = d.scientificName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MintPale,
                                        fontStyle = FontStyle.Italic
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = d.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Health Score Gauge
                item {
                    HealthScoreGauge(
                        score = d.healthScore,
                        status = d.healthStatus
                    )
                }

                // Observed Visual Symptoms
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Signos y Síntomas Observados:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                d.visualSymptoms.forEach { symptom ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = "• $symptom",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Light Assessment Card
                item {
                    LightAssessmentCard(assessment = d.lightAssessment)
                }

                // Humidity Assessment Card
                item {
                    HumidityAssessmentCard(assessment = d.humidityAssessment)
                }

                // Interactive 4-Week Watering Table
                item {
                    InteractiveWateringTable(
                        schedule = d.wateringSchedule,
                        onToggleEvent = { weekIndex ->
                            viewModel.toggleWateringEvent(weekIndex)
                        }
                    )
                }

                // Treatment & Preventive Checklist
                item {
                    RemedyChecklistCard(
                        urgentActions = d.urgentActions,
                        preventiveTips = d.preventiveTips
                    )
                }

                // Actions: Save & Reset
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.saveCurrentDiagnosis() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("save_diagnosis_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.isSaved) SageGreen else EmeraldPrimary
                            )
                        ) {
                            Icon(
                                imageVector = if (state.isSaved) Icons.Default.BookmarkAdded else Icons.Default.Bookmark,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (state.isSaved) "¡Guardada en Mi Jardín!" else "Guardar en Mi Jardín")
                        }

                        OutlinedButton(
                            onClick = { viewModel.resetDiagnosis() },
                            modifier = Modifier
                                .weight(0.6f)
                                .height(48.dp)
                                .testTag("new_diagnosis_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nuevo")
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun SamplePlantChip(
    sample: PlantSample,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val bgColor = if (isSelected) MintPale else MaterialTheme.colorScheme.surfaceVariant

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .testTag("sample_${sample.id}"),
        color = bgColor
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (sample.category) {
                        "AGAVE" -> Icons.Default.Yard
                        "CACTUS" -> Icons.Default.Spa
                        "BAMBU" -> Icons.Default.Eco
                        else -> Icons.Default.Eco
                    },
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = sample.commonName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sample.scientificName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
