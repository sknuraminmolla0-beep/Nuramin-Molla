package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.MetricCard
import com.example.ui.theme.ShandarCoral
import com.example.ui.theme.ShandarEmerald
import com.example.ui.theme.ShandarGold
import com.example.ui.theme.ShandarIndigo
import com.example.ui.viewmodel.ShandarNavTab
import com.example.ui.viewmodel.ShandarViewModel

@Composable
fun HomeScreen(
  viewModel: ShandarViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val quote by viewModel.currentQuote.collectAsState()
  val tasks by viewModel.tasks.collectAsState()
  val totalExpense by viewModel.totalExpense.collectAsState()
  val completedSessions by viewModel.completedSessions.collectAsState()

  val totalTasks = tasks.size
  val completedTasks = tasks.count { it.isCompleted }
  val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Hero Card with Sunrise Illustration & Motivation
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("home_hero_card")
      ) {
        Column {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(130.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.shandar_hero_banner),
              contentDescription = "Shandar Sunrise",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                  )
                )
            )
            Column(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
            ) {
              Text(
                text = "शानदार दिन, शानदार सोच ✨",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              )
              Text(
                text = "Make today productive, mindful, and joyous.",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = Color.White.copy(alpha = 0.9f)
                )
              )
            }
          }
        }
      }
    }

    // 2. Aaj Ka Vichar (Inspirational Quote Card)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("quote_card")
      ) {
        Column(
          modifier = Modifier.padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = "Quote",
                tint = ShandarGold,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "शानदार विचार • Aaj Ka Vichar",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              )
            }

            Row {
              IconButton(
                onClick = {
                  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  val clip = ClipData.newPlainText("Shandar Quote", "${quote.quoteHindi}\n- ${quote.author}")
                  clipboard.setPrimaryClip(clip)
                  Toast.makeText(context, "Quote Copy Ho Gaya! ✨", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.ContentCopy,
                  contentDescription = "Copy Quote",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
              IconButton(
                onClick = { viewModel.nextQuote() },
                modifier = Modifier.size(36.dp).testTag("next_quote_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Refresh,
                  contentDescription = "Next Quote",
                  tint = ShandarIndigo,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "\"${quote.quoteHindi}\"",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              lineHeight = 24.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = quote.quoteEnglish,
            style = MaterialTheme.typography.bodySmall.copy(
              fontStyle = FontStyle.Italic,
              lineHeight = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = ShandarGold.copy(alpha = 0.15f)
            ) {
              Text(
                text = "#${quote.category}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = ShandarGold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
            Text(
              text = "— ${quote.author}",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // 3. Daily Goals / Habit Progress Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "आज की प्रगति (Daily Progress)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "$completedTasks of $totalTasks लक्ष्य पूरे हुए",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Surface(
              shape = CircleShape,
              color = if (progress >= 1f) ShandarEmerald.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer
            ) {
              Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = if (progress >= 1f) ShandarEmerald else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = if (progress >= 1f) ShandarEmerald else ShandarGold,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          if (progress >= 1f && totalTasks > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "🎉 शानदार! आज के सभी कार्य पूरे हो चुके हैं!",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = ShandarEmerald
            )
          }
        }
      }
    }

    // 4. Key Metrics Grid
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        MetricCard(
          title = "कुल खर्चा",
          value = "₹${(totalExpense ?: 0.0).toInt()}",
          subtitle = "Kharcha Diary",
          icon = Icons.Default.AccountBalanceWallet,
          accentColor = ShandarCoral,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.selectTab(ShandarNavTab.EXPENSES) }
        )
        MetricCard(
          title = "ध्यान सत्र",
          value = "$completedSessions",
          subtitle = "Focus Sessions",
          icon = Icons.Default.Timer,
          accentColor = ShandarIndigo,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.selectTab(ShandarNavTab.FOCUS) }
        )
      }
    }

    // 5. Today's Top Priority Tasks Header & List
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "आज के मुख्य कार्य (Priority Tasks)",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Sabhi Dekhein →",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.clickable { viewModel.selectTab(ShandarNavTab.TASKS) }
        )
      }
    }

    val pendingTasks = tasks.filter { !it.isCompleted }.take(3)
    if (pendingTasks.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Sabhi task pure hain! + button se naya task jodein.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(pendingTasks) { task ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = { viewModel.toggleTask(task) },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = Icons.Default.RadioButtonUnchecked,
                contentDescription = "Complete task",
                tint = ShandarGold
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = ShandarIndigo.copy(alpha = 0.1f)
                ) {
                  Text(
                    text = task.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = ShandarIndigo,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                if (task.streakCount > 0) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "🔥 ${task.streakCount} streak",
                    style = MaterialTheme.typography.labelSmall,
                    color = ShandarGold
                  )
                }
              }
            }
          }
        }
      }
    }

    // 6. Quick Action Shortcuts
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Quick Actions (त्वरित कार्य)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { viewModel.selectTab(ShandarNavTab.TASKS) },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ShandarIndigo),
              modifier = Modifier.weight(1f)
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Task", fontSize = 12.sp)
            }

            Button(
              onClick = { viewModel.selectTab(ShandarNavTab.EXPENSES) },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ShandarCoral),
              modifier = Modifier.weight(1f)
            ) {
              Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Kharcha", fontSize = 12.sp)
            }

            Button(
              onClick = { viewModel.selectTab(ShandarNavTab.FOCUS) },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ShandarEmerald),
              modifier = Modifier.weight(1f)
            ) {
              Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Focus", fontSize = 12.sp)
            }
          }
        }
      }
    }
  }
}
