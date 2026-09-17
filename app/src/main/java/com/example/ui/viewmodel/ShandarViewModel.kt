package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.ExpenseEntity
import com.example.data.model.QuoteDataSource
import com.example.data.model.QuoteItem
import com.example.data.model.TaskEntity
import com.example.data.repository.ShandarRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ShandarNavTab(val title: String, val titleHindi: String) {
  HOME("Today", "आज का दिन"),
  TASKS("Habits", "आदतें व लक्ष्य"),
  EXPENSES("Expenses", "खर्चा डायरी"),
  FOCUS("Focus", "ध्यान व एकाग्रता"),
  TOOLS("Tools", "शानदार टूल्स")
}

class ShandarViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ShandarRepository

  init {
    val db = AppDatabase.getDatabase(application)
    repository = ShandarRepository(db.taskDao(), db.expenseDao())
    seedInitialDataIfEmpty()
  }

  // Navigation State
  private val _selectedTab = MutableStateFlow(ShandarNavTab.HOME)
  val selectedTab: StateFlow<ShandarNavTab> = _selectedTab.asStateFlow()

  fun selectTab(tab: ShandarNavTab) {
    _selectedTab.value = tab
  }

  // Quote State
  private val _currentQuote = MutableStateFlow(QuoteDataSource.quotes.first())
  val currentQuote: StateFlow<QuoteItem> = _currentQuote.asStateFlow()

  fun nextQuote() {
    val current = _currentQuote.value
    val available = QuoteDataSource.quotes.filter { it.id != current.id }
    _currentQuote.value = available.random()
  }

  // Tasks / Habits
  val tasks: StateFlow<List<TaskEntity>> = repository.allTasks
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _taskFilter = MutableStateFlow("All")
  val taskFilter: StateFlow<String> = _taskFilter.asStateFlow()

  fun setTaskFilter(filter: String) {
    _taskFilter.value = filter
  }

  fun addTask(title: String, category: String, priority: String) {
    if (title.isBlank()) return
    viewModelScope.launch {
      repository.addTask(
        TaskEntity(
          title = title.trim(),
          category = category,
          priority = priority,
          streakCount = 0
        )
      )
    }
  }

  fun toggleTask(task: TaskEntity) {
    viewModelScope.launch {
      repository.toggleTaskCompletion(task)
    }
  }

  fun deleteTask(task: TaskEntity) {
    viewModelScope.launch {
      repository.deleteTask(task)
    }
  }

  fun deleteCompletedTasks() {
    viewModelScope.launch {
      repository.deleteCompletedTasks()
    }
  }

  // Expenses
  val expenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val totalExpense: StateFlow<Double?> = repository.totalExpenses
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

  private val _monthlyBudget = MutableStateFlow(15000.0)
  val monthlyBudget: StateFlow<Double> = _monthlyBudget.asStateFlow()

  fun updateMonthlyBudget(newBudget: Double) {
    _monthlyBudget.value = newBudget
  }

  fun addExpense(title: String, amount: Double, category: String) {
    if (title.isBlank() || amount <= 0) return
    viewModelScope.launch {
      repository.addExpense(
        ExpenseEntity(
          title = title.trim(),
          amount = amount,
          category = category
        )
      )
    }
  }

  fun deleteExpense(expense: ExpenseEntity) {
    viewModelScope.launch {
      repository.deleteExpense(expense)
    }
  }

  // Focus Timer
  private val _focusMinutes = MutableStateFlow(25)
  val focusMinutes: StateFlow<Int> = _focusMinutes.asStateFlow()

  private val _secondsRemaining = MutableStateFlow(25 * 60)
  val secondsRemaining: StateFlow<Int> = _secondsRemaining.asStateFlow()

  private val _isTimerRunning = MutableStateFlow(false)
  val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

  private val _completedSessions = MutableStateFlow(2)
  val completedSessions: StateFlow<Int> = _completedSessions.asStateFlow()

  private val _selectedAmbient = MutableStateFlow("Prakriti (Forest)")
  val selectedAmbient: StateFlow<String> = _selectedAmbient.asStateFlow()

  private var timerJob: Job? = null

  fun setTimerDuration(minutes: Int) {
    if (_isTimerRunning.value) return
    _focusMinutes.value = minutes
    _secondsRemaining.value = minutes * 60
  }

  fun toggleTimer() {
    if (_isTimerRunning.value) {
      pauseTimer()
    } else {
      startTimer()
    }
  }

  private fun startTimer() {
    _isTimerRunning.value = true
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_secondsRemaining.value > 0 && _isTimerRunning.value) {
        delay(1000L)
        _secondsRemaining.value -= 1
      }
      if (_secondsRemaining.value <= 0) {
        _isTimerRunning.value = false
        _completedSessions.value += 1
        _secondsRemaining.value = _focusMinutes.value * 60
      }
    }
  }

  fun pauseTimer() {
    _isTimerRunning.value = false
    timerJob?.cancel()
  }

  fun resetTimer() {
    _isTimerRunning.value = false
    timerJob?.cancel()
    _secondsRemaining.value = _focusMinutes.value * 60
  }

  fun setAmbient(ambient: String) {
    _selectedAmbient.value = ambient
  }

  // Scratchpad quick note
  private val _quickNotes = MutableStateFlow(
    "Shandar Vichar: Har din ek nayi shuruaat hai!\n• Subah jaldi utho aur taaza hawa lo\n• Apne laksya par dhyan kendrit karo"
  )
  val quickNotes: StateFlow<String> = _quickNotes.asStateFlow()

  fun updateQuickNotes(notes: String) {
    _quickNotes.value = notes
  }

  // Pre-seed initial sample items if user opens app for the first time
  private fun seedInitialDataIfEmpty() {
    viewModelScope.launch {
      // Small sample tasks to make the initial experience immediately vibrant
      val initialTasks = listOf(
        TaskEntity(
          title = "🌅 Subah jaldi uthna aur 2 glass paani peena",
          category = "Swasthya",
          priority = "High",
          streakCount = 5,
          isCompleted = true
        ),
        TaskEntity(
          title = "🧘 15 Minute Dhyaan / Meditation",
          category = "Swasthya",
          priority = "High",
          streakCount = 3,
          isCompleted = false
        ),
        TaskEntity(
          title = "📖 20 Minute Padhai ya Kitab padhna",
          category = "Padhaai",
          priority = "Medium",
          streakCount = 4,
          isCompleted = false
        ),
        TaskEntity(
          title = "💪 Daily Shaam ki Walk / Kasrat",
          category = "Swasthya",
          priority = "Medium",
          streakCount = 7,
          isCompleted = false
        )
      )

      val initialExpenses = listOf(
        ExpenseEntity(title = "Chai & Nashta", amount = 85.0, category = "Khaana"),
        ExpenseEntity(title = "Metro / Bus Pass", amount = 150.0, category = "Safar"),
        ExpenseEntity(title = "Nayi Kitab / Stationery", amount = 299.0, category = "Padhaai")
      )

      // Seed only if db has no tasks
      dbCheckAndSeed(initialTasks, initialExpenses)
    }
  }

  private suspend fun dbCheckAndSeed(tasks: List<TaskEntity>, expenses: List<ExpenseEntity>) {
    val existingTasks = repository.allTasks.firstOrNull() ?: emptyList()
    if (existingTasks.isEmpty()) {
      tasks.forEach { repository.addTask(it) }
    }
    val existingExpenses = repository.allExpenses.firstOrNull() ?: emptyList()
    if (existingExpenses.isEmpty()) {
      expenses.forEach { repository.addExpense(it) }
    }
  }
}
