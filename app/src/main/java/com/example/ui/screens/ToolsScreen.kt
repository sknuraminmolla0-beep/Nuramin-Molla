package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ShandarCoral
import com.example.ui.theme.ShandarEmerald
import com.example.ui.theme.ShandarGold
import com.example.ui.theme.ShandarIndigo
import com.example.ui.viewmodel.ShandarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
  viewModel: ShandarViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val quickNotes by viewModel.quickNotes.collectAsState()
  var scratchpadText by remember(quickNotes) { mutableStateOf(quickNotes) }

  // Split Bill State
  var billAmountStr by remember { mutableStateOf("1200") }
  var peopleCount by remember { mutableIntStateOf(3) }
  var tipPercent by remember { mutableIntStateOf(10) }

  val billAmount = billAmountStr.toDoubleOrNull() ?: 0.0
  val tipAmount = (billAmount * tipPercent) / 100.0
  val totalWithTip = billAmount + tipAmount
  val perPerson = if (peopleCount > 0) totalWithTip / peopleCount else 0.0

  // Discount Calculator State
  var originalPriceStr by remember { mutableStateOf("2499") }
  var discountPercentStr by remember { mutableStateOf("30") }

  val originalPrice = originalPriceStr.toDoubleOrNull() ?: 0.0
  val discountPercent = discountPercentStr.toDoubleOrNull() ?: 0.0
  val savings = (originalPrice * discountPercent) / 100.0
  val finalPrice = maxOf(0.0, originalPrice - savings)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Column {
        Text(
          text = "शानदार टूल्स (Smart Utilities)",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Dainik jivan ke zaroori hisaab aur tools",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Tool 1: Bill & Tip Splitter
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("bill_split_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(ShandarIndigo.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                tint = ShandarIndigo,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "बिल विभाजन (Split Bill)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Doston ke beech hisaab aasaani se baantein",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Result Pill
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "प्रति व्यक्ति हिस्सा (Per Person)",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                  text = "₹${perPerson.toInt()}",
                  style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                  )
                )
              }
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "Kul: ₹${totalWithTip.toInt()}",
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                  text = "Tip: ₹${tipAmount.toInt()} (${tipPercent}%)",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = billAmountStr,
            onValueChange = { billAmountStr = it },
            label = { Text("कुल बिल राशि (Total Bill ₹)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(12.dp))

          // People Selector
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Logon ki sankhya (People):",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Button(
                onClick = { if (peopleCount > 1) peopleCount-- },
                shape = CircleShape,
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
              ) {
                Text("-", fontWeight = FontWeight.Bold, fontSize = 16.sp)
              }
              Text(
                text = "$peopleCount",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 12.dp)
              )
              Button(
                onClick = { peopleCount++ },
                shape = CircleShape,
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
              ) {
                Text("+", fontWeight = FontWeight.Bold, fontSize = 16.sp)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Tip chips
          Text(text = "Tip / Baksheesh:", style = MaterialTheme.typography.labelMedium)
          Spacer(modifier = Modifier.height(6.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(0, 5, 10, 15).forEach { tip ->
              FilterChip(
                selected = tipPercent == tip,
                onClick = { tipPercent = tip },
                label = { Text("$tip%") },
                shape = RoundedCornerShape(8.dp)
              )
            }
          }
        }
      }
    }

    // Tool 2: Discount & Savings Calculator
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("discount_calc_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(ShandarEmerald.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Discount,
                contentDescription = null,
                tint = ShandarEmerald,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "छूट और बचत (Discount & Bachat)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Shopping par kitni bachat hogi jaanien",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Surface(
            shape = RoundedCornerShape(16.dp),
            color = ShandarEmerald.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "अंतिम मूल्य (Final Price)",
                  style = MaterialTheme.typography.labelSmall,
                  color = ShandarEmerald
                )
                Text(
                  text = "₹${finalPrice.toInt()}",
                  style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = ShandarEmerald
                  )
                )
              }
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "बचत (You Save):",
                  style = MaterialTheme.typography.labelSmall,
                  color = ShandarGold
                )
                Text(
                  text = "₹${savings.toInt()} OFF",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = ShandarGold
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = originalPriceStr,
              onValueChange = { originalPriceStr = it },
              label = { Text("MRP Price (₹)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
              value = discountPercentStr,
              onValueChange = { discountPercentStr = it },
              label = { Text("Chhoot (%)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    // Tool 3: Scratchpad / Quick Notes
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("scratchpad_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .background(ShandarGold.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.EditNote,
                  contentDescription = null,
                  tint = ShandarGold,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "शानदार रफ पैड (Quick Memo)",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Zaroori baatein ya number likh kar rakhein",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            IconButton(
              onClick = {
                viewModel.updateQuickNotes(scratchpadText)
                Toast.makeText(context, "Note Save Ho Gaya! 📝", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.testTag("save_scratchpad_button")
            ) {
              Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save Note",
                tint = ShandarIndigo
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = scratchpadText,
            onValueChange = {
              scratchpadText = it
              viewModel.updateQuickNotes(it)
            },
            placeholder = { Text("Yahan kuch bhi likhein...") },
            minLines = 4,
            maxLines = 8,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().testTag("scratchpad_input")
          )
        }
      }
    }
  }
}
