package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HumidityAssessment
import com.example.model.LightAssessment
import com.example.model.WateringEvent
import com.example.model.WateringSchedule
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HealthCritical
import com.example.ui.theme.HealthGood
import com.example.ui.theme.HealthWarning
import com.example.ui.theme.MintPale
import com.example.ui.theme.SageGreen
import com.example.ui.theme.TerracottaAccent

@Composable
fun HealthScoreGauge(
    score: Int,
    status: String,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(targetValue = score / 100f, label = "gauge")
    val gaugeColor = when {
        score >= 80 -> HealthGood
        score >= 60 -> HealthWarning
        else -> HealthCritical
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Índice de Salud Foliar",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = gaugeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelLarge,
                        color = gaugeColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (score >= 80) "Excelente desarrollo vegetativo y firmeza."
                    else if (score >= 60) "Requiere ajustes en riego o exposición solar."
                    else "Alerta fitosanitaria: acciones urgentes recomendadas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(80.dp),
                    color = gaugeColor.copy(alpha = 0.2f),
                    strokeWidth = 7.dp
                )
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(80.dp),
                    color = gaugeColor,
                    strokeWidth = 7.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$score%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = gaugeColor
                    )
                }
            }
        }
    }
}

@Composable
fun LightAssessmentCard(
    assessment: LightAssessment,
    modifier: Modifier = Modifier
) {
    AssessmentCard(
        title = "Evaluación de Exposición Solar y Luz",
        icon = Icons.Default.LightMode,
        iconTint = Color(0xFFF4A261),
        status = assessment.status,
        details = assessment.details,
        metricLabel = "Radiación recomendada",
        metricValue = assessment.luxRecommendation,
        tip = assessment.placementTip,
        modifier = modifier
    )
}

@Composable
fun HumidityAssessmentCard(
    assessment: HumidityAssessment,
    modifier: Modifier = Modifier
) {
    AssessmentCard(
        title = "Evaluación de Humedad y Sustrato",
        icon = Icons.Default.WaterDrop,
        iconTint = Color(0xFF2D6A4F),
        status = assessment.status,
        details = assessment.details,
        metricLabel = "Condición de sustrato/medio",
        metricValue = assessment.soilCondition,
        tip = assessment.ambientAdvice,
        modifier = modifier
    )
}

@Composable
private fun AssessmentCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    status: String,
    details: String,
    metricLabel: String,
    metricValue: String,
    tip: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = status,
                        style = MaterialTheme.typography.bodyMedium,
                        color = iconTint,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = details,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Metric chip box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = metricLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        text = metricValue,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tip,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveWateringTable(
    schedule: WateringSchedule,
    onToggleEvent: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Opacity,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Calendario Interactivo de Riego",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = schedule.frequencyDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Water quality recommendation tip
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MintPale,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = EmeraldDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = schedule.waterTypeTip,
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Table Rows
            Text(
                text = "Cronograma Semanal (Toca para marcar como regada):",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            schedule.weeklyCalendar.forEachIndexed { index, event ->
                WateringTableRow(
                    event = event,
                    onToggle = { onToggleEvent(index) }
                )
                if (index < schedule.weeklyCalendar.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun WateringTableRow(
    event: WateringEvent,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (event.isWatered) MintPale else MaterialTheme.colorScheme.surfaceVariant,
        label = "rowBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (event.isWatered) SageGreen else Color.Transparent,
        label = "rowBorder"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onToggle() },
        color = bgColor
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (event.isWatered) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Semana ${event.weekNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (event.isWatered) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.dateLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = event.suggestedAction,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Dosis sugerida: ${event.amount}",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Interactive Checkbox / Toggle Button
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (event.isWatered) EmeraldPrimary else Color.Transparent,
                border = if (!event.isWatered) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline) else null,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("water_check_week_${event.weekNumber}")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (event.isWatered) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Regada",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RemedyChecklistCard(
    urgentActions: List<String>,
    preventiveTips: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = TerracottaAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Plan de Tratamiento y Cuidados Inmediatos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Acciones Prioritarias:",
                style = MaterialTheme.typography.labelLarge,
                color = TerracottaAccent
            )
            Spacer(modifier = Modifier.height(6.dp))

            urgentActions.forEach { action ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = TerracottaAccent,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = action,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Mantenimiento Preventivo:",
                style = MaterialTheme.typography.labelLarge,
                color = EmeraldPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            preventiveTips.forEach { tip ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
