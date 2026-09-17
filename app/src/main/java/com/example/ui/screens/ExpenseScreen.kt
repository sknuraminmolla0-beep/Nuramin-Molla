package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExpenseEntity
import com.example.ui.theme.ShandarCoral
import com.example.ui.theme.ShandarEmerald
import com.example.ui.theme.ShandarGold
import com.example.ui.theme.ShandarIndigo
import com.example.ui.viewmodel.ShandarViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
  viewModel: ShandarViewModel,
  modifier: Modifier = Modifier
) {
  val expenses by viewModel.expenses.collectAsState()
  val totalExpense by viewModel.totalExpense.collectAsState()
  val monthlyBudget by viewModel.monthlyBudget.collectAsState()

  var showAddDialog by remember { mutableStateOf(false) }
  var showBudgetDialog by remember { mutableStateOf(false) }

  val spent = totalExpense ?: 0.0
  val remaining = maxOf(0.0, monthlyBudget - spent)
  val spentRatio = if (monthlyBudget > 0) (spent / monthlyBudget).toFloat().coerceIn(0f, 1f) else 0f

  Scaffold(
    modifier = modifier.fillMaxSize(),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = ShandarCoral,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.testTag("add_expense_fab")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = "Add Expense")
          Spacer(modifier = Modifier.width(6.dp))
          Text("Kharcha Jodein", fontWeight = FontWeight.Bold)
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
      Spacer(modifier = Modifier.height(12.dp))

      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "दैनिक खर्चा डायरी (Expenses)",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Apne kharchon ka sahi hisaab rakhein",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        IconButton(
          onClick = { showBudgetDialog = true },
          modifier = Modifier.testTag("edit_budget_button")
        ) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Budget",
            tint = ShandarIndigo
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Budget & Spend Summary Card
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "कुल खर्चा (Total Spent)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "₹${spent.toInt()}",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = ShandarCoral
                )
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "मासिक बजट (Budget)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "₹${monthlyBudget.toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          LinearProgressIndicator(
            progress = { spentRatio },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = if (spentRatio > 0.85f) ShandarCoral else ShandarGold,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${(spentRatio * 100).toInt()}% kharch ho chuka hai",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Baaki: ₹${remaining.toInt()}",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              color = if (remaining > 0) ShandarEmerald else ShandarCoral
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "हाल के खर्चे (Recent Transactions)",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(8.dp))

      if (expenses.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.ReceiptLong,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
              modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Abhi koi kharcha darj nahi hai.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          items(expenses, key = { it.id }) { expense ->
            ExpenseItemCard(
              expense = expense,
              onDelete = { viewModel.deleteExpense(expense) }
            )
          }
        }
      }
    }
  }

  if (showAddDialog) {
    AddExpenseDialog(
      onDismiss = { showAddDialog = false },
      onConfirm = { title, amount, category ->
        viewModel.addExpense(title, amount, category)
        showAddDialog = false
      }
    )
  }

  if (showBudgetDialog) {
    EditBudgetDialog(
      currentBudget = monthlyBudget,
      onDismiss = { showBudgetDialog = false },
      onConfirm = { newBudget ->
        viewModel.updateMonthlyBudget(newBudget)
        showBudgetDialog = false
      }
    )
  }
}

@Composable
fun ExpenseItemCard(
  expense: ExpenseEntity,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val (categoryIcon, categoryColor) = when (expense.category) {
    "Khaana" -> Pair(Icons.Default.Fastfood, ShandarGold)
    "Safar" -> Pair(Icons.Default.TrendingDown, ShandarIndigo)
    "Shopping" -> Pair(Icons.Default.ShoppingBag, ShandarCoral)
    "Bill" -> Pair(Icons.Default.ReceiptLong, ShandarEmerald)
    else -> Pair(Icons.Default.MoreHoriz, MaterialTheme.colorScheme.secondary)
  }

  val dateStr = remember(expense.timestamp) {
    val formatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    formatter.format(Date(expense.timestamp))
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(categoryColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = categoryIcon,
          contentDescription = expense.category,
          tint = categoryColor,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = expense.title,
          style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = expense.category,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "• $dateStr",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Text(
        text = "-₹${expense.amount.toInt()}",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = ShandarCoral
        )
      )

      Spacer(modifier = Modifier.width(4.dp))

      IconButton(
        onClick = onDelete,
        modifier = Modifier.size(32.dp)
      ) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = "Delete expense",
          tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun AddExpenseDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, amount: Double, category: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var amountStr by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Khaana") }

  val categories = listOf("Khaana", "Safar", "Shopping", "Bill", "Anyatha")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "नया खर्चा जोड़ें (Add Expense)",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = amountStr,
          onValueChange = { amountStr = it },
          label = { Text("रकम (Amount in ₹)") },
          placeholder = { Text("e.g. 150") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth().testTag("expense_amount_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("विवरण (Title / Description)") },
          placeholder = { Text("e.g. Dophar ka khana / Taxi") },
          modifier = Modifier.fillMaxWidth().testTag("expense_title_input"),
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
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val amt = amountStr.toDoubleOrNull() ?: 0.0
          if (title.isNotBlank() && amt > 0) {
            onConfirm(title, amt, category)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = ShandarCoral),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("save_expense_button")
      ) {
        Text("Kharcha Jodein")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Radd Karein")
      }
    }
  )
}

@Composable
fun EditBudgetDialog(
  currentBudget: Double,
  onDismiss: () -> Unit,
  onConfirm: (Double) -> Unit
) {
  var budgetStr by remember { mutableStateOf(currentBudget.toInt().toString()) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "मासिक बजट सेट करें",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column {
        Text(
          text = "Apna monthly spending target tay karein taaki aap kharchon par kaboo rakh sakein.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = budgetStr,
          onValueChange = { budgetStr = it },
          label = { Text("Target Budget (₹)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val b = budgetStr.toDoubleOrNull() ?: currentBudget
          if (b > 0) onConfirm(b)
        },
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Save Karein")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Band Karein")
      }
    }
  )
}
