package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ShandarEmerald
import com.example.ui.theme.ShandarGold
import com.example.ui.theme.ShandarIndigo
import com.example.ui.viewmodel.ShandarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
  viewModel: ShandarViewModel,
  modifier: Modifier = Modifier
) {
  val focusMinutes by viewModel.focusMinutes.collectAsState()
  val secondsRemaining by viewModel.secondsRemaining.collectAsState()
  val isRunning by viewModel.isTimerRunning.collectAsState()
  val completedSessions by viewModel.completedSessions.collectAsState()
  val selectedAmbient by viewModel.selectedAmbient.collectAsState()

  val totalSeconds = focusMinutes * 60
  val progress = if (totalSeconds > 0) (totalSeconds - secondsRemaining).toFloat() / totalSeconds else 0f

  val minutes = secondsRemaining / 60
  val seconds = secondsRemaining % 60
  val formattedTime = String.format("%02d:%02d", minutes, seconds)

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (isRunning) 1.08f else 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400),
      repeatMode = RepeatMode.Reverse
    ),
    label = "breathingScale"
  )

  val ambientPresets = listOf(
    "Prakriti (Forest)",
    "Rimjhim Baarish (Rain)",
    "Mandir Ghanti (Zen Bells)",
    "Shant Sagar (Ocean)"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = "ध्यान और एकाग्रता (Focus Mode)",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Ek samay par ek lakshya, poori shanti ke saath 🧘",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Duration Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        listOf(15, 25, 45, 60).forEach { mins ->
          FilterChip(
            selected = focusMinutes == mins,
            onClick = { viewModel.setTimerDuration(mins) },
            label = { Text("${mins}m", fontWeight = FontWeight.Bold) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = ShandarIndigo,
              selectedLabelColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
          )
        }
      }
    }

    // Glowing Circular Timer Canvas
    item {
      Box(
        modifier = Modifier
          .size(260.dp)
          .padding(12.dp),
        contentAlignment = Alignment.Center
      ) {
        // Subtle breathing ring behind
        if (isRunning) {
          Box(
            modifier = Modifier
              .size(240.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(ShandarEmerald.copy(alpha = 0.08f))
          )
        }

        val primaryColor = if (isRunning) ShandarEmerald else ShandarIndigo
        val trackColor = MaterialTheme.colorScheme.surfaceVariant

        Canvas(modifier = Modifier.size(220.dp)) {
          // Track
          drawCircle(
            color = trackColor,
            radius = size.minDimension / 2 - 12.dp.toPx(),
            style = Stroke(width = 10.dp.toPx())
          )
          // Progress arc
          drawArc(
            color = primaryColor,
            startAngle = -90f,
            sweepAngle = progress * 360f,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
          )
        }

        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = formattedTime,
            style = MaterialTheme.typography.displayMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isRunning) ShandarEmerald.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
          ) {
            Text(
              text = if (isRunning) "Deep Focus • Dhyaan" else "Ready to Focus",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = if (isRunning) ShandarEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // Timer Controls: Play / Pause / Reset
    item {
      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { viewModel.resetTimer() },
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .testTag("reset_timer_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset timer",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }

        Button(
          onClick = { viewModel.toggleTimer() },
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isRunning) ShandarGold else ShandarIndigo
          ),
          modifier = Modifier
            .size(72.dp)
            .testTag("toggle_timer_button")
        ) {
          Icon(
            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isRunning) "Pause" else "Start",
            tint = Color.White,
            modifier = Modifier.size(36.dp)
          )
        }
      }
    }

    // Breathing Guidance Tip
    if (isRunning) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = ShandarEmerald.copy(alpha = 0.1f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.SelfImprovement,
              contentDescription = null,
              tint = ShandarEmerald,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "गहरी सांस लें और शांत मन से काम करें...",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = ShandarEmerald
            )
          }
        }
      }
    }

    // Ambient Sound Mode Selection
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = ShandarIndigo,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "शांत वातावरण (Ambient Sound Aura)",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ambientPresets) { preset ->
              FilterChip(
                selected = selectedAmbient == preset,
                onClick = { viewModel.setAmbient(preset) },
                label = { Text(preset, fontSize = 12.sp) },
                shape = RoundedCornerShape(10.dp)
              )
            }
          }
        }
      }
    }

    // Today's Focus Stats
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "$completedSessions",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = ShandarIndigo
              )
            )
            Text(
              text = "सत्र पूरे हुए (Sessions)",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Box(
            modifier = Modifier
              .width(1.dp)
              .height(36.dp)
              .background(MaterialTheme.colorScheme.outlineVariant)
          )
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "${completedSessions * 25} min",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = ShandarEmerald
              )
            )
            Text(
              text = "कुल एकाग्रता समय",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
