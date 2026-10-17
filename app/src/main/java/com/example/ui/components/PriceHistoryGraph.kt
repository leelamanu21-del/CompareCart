package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PriceHistoryPoint
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PriceHistoryGraph(
    priceHistory: List<PriceHistoryPoint>,
    historicalLow: Double,
    historicalHigh: Double,
    currentPrice: Double,
    modifier: Modifier = Modifier
) {
    if (priceHistory.isEmpty()) return

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 0
        }
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val displayPoint = selectedIndex?.let { priceHistory.getOrNull(it) } ?: priceHistory.last()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderSubtle, BorderActive.copy(alpha = 0.3f))))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Selected / Current Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = "Price History",
                            tint = BrandCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "90-Day Price Trend",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Touch & drag across chart to inspect real prices",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }

                // Selected point callout
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = currencyFormat.format(displayPoint.price),
                        style = MaterialTheme.typography.titleLarge,
                        color = BrandCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = displayPoint.dateLabel + if (displayPoint.isSaleEvent) " • ${displayPoint.saleEventName ?: "Sale"}" else "",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (displayPoint.isSaleEvent) AsliGreen else TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Chart
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val width = constraints.maxWidth.toFloat()
                val height = constraints.maxHeight.toFloat()
                val paddingBottom = 40f
                val paddingTop = 20f

                val prices = priceHistory.map { it.price }
                val minP = (prices.minOrNull() ?: historicalLow) * 0.96
                val maxP = (prices.maxOrNull() ?: historicalHigh) * 1.04
                val priceRange = if (maxP - minP > 0) maxP - minP else 1.0

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { offset ->
                                    val step = width / (priceHistory.size - 1).coerceAtLeast(1)
                                    val idx = (offset.x / step).toInt().coerceIn(0, priceHistory.size - 1)
                                    selectedIndex = idx
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val step = width / (priceHistory.size - 1).coerceAtLeast(1)
                                    val idx = (offset.x / step).toInt().coerceIn(0, priceHistory.size - 1)
                                    selectedIndex = idx
                                },
                                onDrag = { change, _ ->
                                    val step = width / (priceHistory.size - 1).coerceAtLeast(1)
                                    val idx = (change.position.x / step).toInt().coerceIn(0, priceHistory.size - 1)
                                    selectedIndex = idx
                                }
                            )
                        }
                ) {
                    val stepX = size.width / (priceHistory.size - 1).coerceAtLeast(1)
                    val chartHeight = size.height - paddingBottom - paddingTop

                    // Draw Horizontal Grid Guide Lines
                    val gridLines = 3
                    for (i in 0..gridLines) {
                        val y = paddingTop + (chartHeight / gridLines) * i
                        drawLine(
                            color = BorderSubtle.copy(alpha = 0.5f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    }

                    // Build Curve Points
                    val points = priceHistory.mapIndexed { index, item ->
                        val x = index * stepX
                        val normalizedPrice = (item.price - minP) / priceRange
                        val y = paddingTop + (chartHeight * (1f - normalizedPrice.toFloat()))
                        Offset(x, y)
                    }

                    // Draw Gradient Fill beneath curve
                    if (points.size >= 2) {
                        val fillPath = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 1 until points.size) {
                                val prev = points[i - 1]
                                val curr = points[i]
                                val midX = (prev.x + curr.x) / 2f
                                cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
                            }
                            lineTo(points.last().x, size.height - paddingBottom)
                            lineTo(points.first().x, size.height - paddingBottom)
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    BrandCyan.copy(alpha = 0.35f),
                                    BrandBlue.copy(alpha = 0.10f),
                                    Color.Transparent
                                ),
                                startY = paddingTop,
                                endY = size.height - paddingBottom
                            )
                        )

                        // Draw Curve Line
                        val strokePath = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 1 until points.size) {
                                val prev = points[i - 1]
                                val curr = points[i]
                                val midX = (prev.x + curr.x) / 2f
                                cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
                            }
                        }

                        drawPath(
                            path = strokePath,
                            color = BrandCyan,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Draw Data Dots & Active scrub indicator
                    points.forEachIndexed { index, point ->
                        val isSelected = (index == selectedIndex)
                        val isSale = priceHistory[index].isSaleEvent

                        if (isSelected) {
                            // Vertical scrub line
                            drawLine(
                                color = BrandCyan.copy(alpha = 0.8f),
                                start = Offset(point.x, paddingTop),
                                end = Offset(point.x, size.height - paddingBottom),
                                strokeWidth = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                            )
                            // Outer glowing ring
                            drawCircle(
                                color = BrandCyan.copy(alpha = 0.4f),
                                radius = 10.dp.toPx(),
                                center = point
                            )
                        }

                        drawCircle(
                            color = if (isSale) AsliGreen else (if (isSelected) BrandCyan else SurfaceDark),
                            radius = if (isSelected) 6.dp.toPx() else 4.dp.toPx(),
                            center = point
                        )
                        drawCircle(
                            color = if (isSale) Color.White else BrandCyan,
                            radius = if (isSelected) 3.dp.toPx() else 2.dp.toPx(),
                            center = point
                        )
                    }
                }
            }

            // X-Axis Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                priceHistory.forEachIndexed { index, point ->
                    // Show every other label on small screens or key dates
                    if (index == 0 || index == priceHistory.size - 1 || index == priceHistory.size / 2) {
                        Text(
                            text = point.dateLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Historical Stats Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip(
                    label = "Lowest Ever",
                    value = currencyFormat.format(historicalLow),
                    color = AsliGreen,
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    label = "90-Day High",
                    value = currencyFormat.format(historicalHigh),
                    color = FakeWarningOrange,
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    label = "Current Price",
                    value = currencyFormat.format(currentPrice),
                    color = BrandCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatChip(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = SurfaceVariantDark
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
