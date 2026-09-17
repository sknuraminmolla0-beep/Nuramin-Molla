package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ShandarIndigo
import com.example.ui.viewmodel.ShandarNavTab

@Composable
fun ShandarBottomNav(
  selectedTab: ShandarNavTab,
  onTabSelected: (ShandarNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier.navigationBarsPadding().testTag("shandar_bottom_nav"),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp
  ) {
    ShandarNavTab.entries.forEach { tab ->
      val isSelected = selectedTab == tab
      val (iconSelected, iconUnselected) = when (tab) {
        ShandarNavTab.HOME -> Pair(Icons.Default.Home, Icons.Outlined.Home)
        ShandarNavTab.TASKS -> Pair(Icons.Default.CheckCircle, Icons.Outlined.CheckCircle)
        ShandarNavTab.EXPENSES -> Pair(Icons.Default.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet)
        ShandarNavTab.FOCUS -> Pair(Icons.Default.SelfImprovement, Icons.Outlined.SelfImprovement)
        ShandarNavTab.TOOLS -> Pair(Icons.Default.Build, Icons.Outlined.Build)
      }

      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = if (isSelected) iconSelected else iconUnselected,
            contentDescription = tab.title
          )
        },
        label = {
          Text(
            text = tab.title,
            fontSize = 11.sp,
            maxLines = 1
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
          indicatorColor = MaterialTheme.colorScheme.primaryContainer,
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
      )
    }
  }
}
