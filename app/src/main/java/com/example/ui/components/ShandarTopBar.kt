package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ShandarEmerald
import com.example.ui.theme.ShandarGold
import com.example.ui.theme.ShandarIndigo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ShandarTopBar(
  modifier: Modifier = Modifier,
  totalStreak: Int = 5,
) {
  val calendar = remember { Calendar.getInstance() }
  val hour = calendar.get(Calendar.HOUR_OF_DAY)

  val (greeting, hindiGreeting) = when {
    hour in 4..11 -> Pair("Good Morning", "सुप्रभात!")
    hour in 12..16 -> Pair("Good Afternoon", "शुभ दोपहर!")
    hour in 17..21 -> Pair("Good Evening", "शुभ संध्या!")
    else -> Pair("Peaceful Night", "शुभ रात्रि!")
  }

  val dateFormat = remember { SimpleDateFormat("EEEE, dd MMM", Locale.getDefault()) }
  val dateStr = remember { dateFormat.format(calendar.time) }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("shandar_top_bar"),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(
                  listOf(ShandarIndigo, ShandarGold)
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Stars,
              contentDescription = "Shandar App",
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "$hindiGreeting Shandar",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.3.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = dateStr,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // Streak Pill
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.testTag("streak_pill")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = "Streak",
            tint = ShandarGold,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "$totalStreak Din Streak",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer
          )
        }
      }
    }
  }
}
