package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskEntity
import com.example.ui.theme.ShandarCoral
import com.example.ui.theme.ShandarEmerald
import com.example.ui.theme.ShandarGold
import com.example.ui.theme.ShandarIndigo
import com.example.ui.viewmodel.ShandarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
  viewModel: ShandarViewModel,
  modifier: Modifier = Modifier
) {
  val tasks by viewModel.tasks.collectAsState()
  val currentFilter by viewModel.taskFilter.collectAsState()
  var showAddDialog by remember { mutableStateOf(false) }

  val filteredTasks = when (currentFilter) {
    "Pending" -> tasks.filter { !it.isCompleted }
    "Completed" -> tasks.filter { it.isCompleted }
    else -> tasks
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = ShandarIndigo,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.testTag("add_task_fab")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
          Spacer(modifier = Modifier.width(6.dp))
          Text("Nayi Aadat", fontWeight = FontWeight.Bold)
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
    ) {
      // Top header info
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "आदतें और लक्ष्य (Habits & Tasks)",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Har din thoda behtar banein ✨",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        if (tasks.any { it.isCompleted }) {
          IconButton(
            onClick = { viewModel.deleteCompletedTasks() },
            modifier = Modifier.testTag("clear_completed_button")
          ) {
            Icon(
              imageVector = Icons.Default.ClearAll,
              contentDescription = "Clear completed",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Filter Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("All" to "Sabhi (${tasks.size})", "Pending" to "Baaki (${tasks.count { !it.isCompleted }})", "Completed" to "Poore (${tasks.count { it.isCompleted }})").forEach { (key, label) ->
          FilterChip(
            selected = currentFilter == key,
            onClick = { viewModel.setTaskFilter(key) },
            label = { Text(label, fontSize = 13.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = ShandarIndigo,
              selectedLabelColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      if (filteredTasks.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = ShandarEmerald.copy(alpha = 0.5f),
              modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = if (currentFilter == "Completed") "Abhi koi task poora nahi hua hai." else "Koi task nahi mila! Nayi aadat jodein.",
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          items(filteredTasks, key = { it.id }) { task ->
            TaskItemCard(
              task = task,
              onToggle = { viewModel.toggleTask(task) },
              onDelete = { viewModel.deleteTask(task) }
            )
          }
        }
      }
    }
  }

  if (showAddDialog) {
    AddTaskDialog(
      onDismiss = { showAddDialog = false },
      onConfirm = { title, category, priority ->
        viewModel.addTask(title, category, priority)
        showAddDialog = false
      }
    )
  }
}

@Composable
fun TaskItemCard(
  task: TaskEntity,
  onToggle: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val cardBackground by animateColorAsState(
    targetValue = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    else MaterialTheme.colorScheme.surface,
    label = "cardBg"
  )

  val priorityColor = when (task.priority) {
    "High" -> ShandarCoral
    "Medium" -> ShandarGold
    else -> ShandarIndigo
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = cardBackground),
    elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 0.dp else 2.dp),
    border = if (task.isCompleted) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)) else null,
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onToggle,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
          contentDescription = "Toggle task",
          tint = if (task.isCompleted) ShandarEmerald else ShandarGold,
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = task.title,
          style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
          ),
          color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Priority dot & text
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = priorityColor.copy(alpha = 0.15f)
          ) {
            Text(
              text = "${task.priority} Priority",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = priorityColor,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Category pill
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
          ) {
            Text(
              text = task.category,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          if (task.streakCount > 0) {
            Spacer(modifier = Modifier.width(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak",
                tint = ShandarGold,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "${task.streakCount} streak",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = ShandarGold
              )
            }
          }
        }
      }

      IconButton(
        onClick = onDelete,
        modifier = Modifier.size(32.dp)
      ) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = "Delete task",
          tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun AddTaskDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, category: String, priority: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Swasthya") }
  var priority by remember { mutableStateOf("Medium") }

  val categories = listOf("Swasthya", "Padhaai", "Kaam", "Dainik", "Anyatha")
  val priorities = listOf("High", "Medium", "Normal")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "नई आदत या लक्ष्य जोड़ें",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Task / Habit ka naam") },
          placeholder = { Text("e.g. 20 min Yoga ya Walk") },
          modifier = Modifier.fillMaxWidth().testTag("task_title_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        Text(
          text = "श्रेणी (Category):",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          items(categories) { cat ->
            FilterChip(
              selected = category == cat,
              onClick = { category = cat },
              label = { Text(cat, fontSize = 12.sp) },
              shape = RoundedCornerShape(8.dp)
            )
          }
        }

        Text(
          text = "प्राथमिकता (Priority):",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          priorities.forEach { prio ->
            FilterChip(
              selected = priority == prio,
              onClick = { priority = prio },
              label = { Text(prio, fontSize = 12.sp) },
              shape = RoundedCornerShape(8.dp)
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title, category, priority)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = ShandarIndigo),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("save_task_button")
      ) {
        Text("Jodein (Save)")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Radd Karein")
      }
    }
  )
}
