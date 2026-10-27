package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MintPale
import com.example.ui.theme.SageGreen
import com.example.ui.theme.TerracottaAccent

@Composable
fun BotanicalGuideScreen(
    modifier: Modifier = Modifier
) {
    var expandedAgave by remember { mutableStateOf(true) }
    var expandedCactus by remember { mutableStateOf(false) }
    var expandedBamboo by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = MintPale,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Guías Botánicas de Cuidado",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Manual experto para Agaves, Cactus y Bambú",
                                style = MaterialTheme.typography.bodySmall,
                                color = SageGreen
                            )
                        }
                    }
                }
            }
        }

        // Agave Section
        item {
            GuideSectionCard(
                title = "Agaves (Agave tequilana / americana)",
                subtitle = "Xerófitas de sol pleno y sustrato drenante",
                icon = Icons.Default.Yard,
                iconColor = EmeraldPrimary,
                isExpanded = expandedAgave,
                onToggle = { expandedAgave = !expandedAgave }
            ) {
                GuideDetailItem(
                    icon = Icons.Default.LightMode,
                    title = "Luz y Exposición:",
                    content = "Requieren luz solar directa abundante (25,000 a 50,000 Lux). En interiores, deben colocarse junto a ventanas orientadas al sur o poniente. Si la roseta se abre o se alarga, sufre etiolación por falta de sol."
                )
                GuideDetailItem(
                    icon = Icons.Default.Opacity,
                    title = "Riego y Frecuencia:",
                    content = "Regla de oro: regar sólo cuando el sustrato esté 100% seco. Cada 18 a 21 días en primavera-verano; en invierno suspender el riego casi por completo (1 vez cada 4-6 semanas)."
                )
                GuideDetailItem(
                    icon = Icons.Default.Eco,
                    title = "Sustrato Recomendado:",
                    content = "70% inorgánico/mineral (pomina, tezontle o perlita) y 30% tierra negra. Maceta de terracota sin esmaltar con orificio amplio de drenaje."
                )
                GuideDetailItem(
                    icon = Icons.Default.Warning,
                    title = "Errores Comunes:",
                    content = "Dejar agua encharcada en el plato inferior causa asfixia radical inmediata y pudrición de la corona."
                )
            }
        }

        // Cactus Section
        item {
            GuideSectionCard(
                title = "Cactáceas (Echinocactus, Mammillaria)",
                subtitle = "Especialistas en aridez y letargo invernal",
                icon = Icons.Default.Spa,
                iconColor = Color(0xFFE76F51),
                isExpanded = expandedCactus,
                onToggle = { expandedCactus = !expandedCactus }
            ) {
                GuideDetailItem(
                    icon = Icons.Default.LightMode,
                    title = "Luz y Fotosíntesis CAM:",
                    content = "Necesitan de 30,000 a 60,000 Lux. Si el ápice del cactus se torna delgado y pálido, le falta sol urgente."
                )
                GuideDetailItem(
                    icon = Icons.Default.Opacity,
                    title = "Riego por Temporadas:",
                    content = "Cada 14 a 20 días en meses cálidos. Al regar, jamás mojes el cuerpo espinoso; vierte el agua en el borde de la maceta. En invierno se suspende totalmente el riego si la temperatura baja de 15°C."
                )
                GuideDetailItem(
                    icon = Icons.Default.Eco,
                    title = "Sustrato y Aireación:",
                    content = "Mezcla arenosa volcánica con excelente permeabilidad. El sustrato debe secar en menos de 72 horas."
                )
                GuideDetailItem(
                    icon = Icons.Default.Warning,
                    title = "Signos de Pudrición:",
                    content = "Base blanda al tacto o tono marrón acuoso indican daño por exceso de riego. Requiere cortar tejido sano y volver a enraizar."
                )
            }
        }

        // Lucky Bamboo Section
        item {
            GuideSectionCard(
                title = "Bambú de la Suerte (Dracaena sanderiana)",
                subtitle = "Cultivo en agua, luz filtrada y agua sin cloro",
                icon = Icons.Default.Eco,
                iconColor = Color(0xFF00796B),
                isExpanded = expandedBamboo,
                onToggle = { expandedBamboo = !expandedBamboo }
            ) {
                GuideDetailItem(
                    icon = Icons.Default.LightMode,
                    title = "Luz Indirecta Brillante:",
                    content = "5,000 a 10,000 Lux filtrados. NUNCA sol directo. Los rayos directos queman las hojas tiernas dejándolas blancas o secas."
                )
                GuideDetailItem(
                    icon = Icons.Default.Opacity,
                    title = "Sensibilidad al Cloro y Sales:",
                    content = "¡MUY IMPORTANTE! El agua del grifo tiene cloro y fluoruros que provocan puntas amarillas o marrones secas. Deja reposar el agua 24-48 horas o usa agua filtrada."
                )
                GuideDetailItem(
                    icon = Icons.Default.Eco,
                    title = "Mantenimiento del Recipiente:",
                    content = "Mantén entre 3 y 5 cm de agua cubriendo las raíces rojizas. Cambia el agua al 100% cada 7 a 10 días y enjuaga las piedras soporte."
                )
                GuideDetailItem(
                    icon = Icons.Default.Warning,
                    title = "Tallo Amarillo:",
                    content = "Si un tallo se torna completamente amarillo, retíralo del arreglo de inmediato para evitar que pudra a los demás tallos saludables."
                )
            }
        }
    }
}

@Composable
private fun GuideSectionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = iconColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = icon, contentDescription = null, tint = iconColor)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun GuideDetailItem(
    icon: ImageVector,
    title: String,
    content: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
