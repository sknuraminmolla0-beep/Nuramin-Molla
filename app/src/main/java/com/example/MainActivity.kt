package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ShandarBottomNav
import com.example.ui.components.ShandarTopBar
import com.example.ui.screens.ExpenseScreen
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.theme.ShandarTheme
import com.example.ui.viewmodel.ShandarNavTab
import com.example.ui.viewmodel.ShandarViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ShandarTheme {
        ShandarApp()
      }
    }
  }
}

@Composable
fun ShandarApp(
  viewModel: ShandarViewModel = viewModel()
) {
  val selectedTab by viewModel.selectedTab.collectAsState()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      ShandarTopBar(
        modifier = Modifier.statusBarsPadding()
      )
    },
    bottomBar = {
      ShandarBottomNav(
        selectedTab = selectedTab,
        onTabSelected = { viewModel.selectTab(it) }
      )
    }
  ) { innerPadding ->
    Crossfade(
      targetState = selectedTab,
      animationSpec = tween(250),
      label = "tabTransition",
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) { tab ->
      when (tab) {
        ShandarNavTab.HOME -> HomeScreen(viewModel = viewModel)
        ShandarNavTab.TASKS -> TasksScreen(viewModel = viewModel)
        ShandarNavTab.EXPENSES -> ExpenseScreen(viewModel = viewModel)
        ShandarNavTab.FOCUS -> FocusScreen(viewModel = viewModel)
        ShandarNavTab.TOOLS -> ToolsScreen(viewModel = viewModel)
      }
    }
  }
}
