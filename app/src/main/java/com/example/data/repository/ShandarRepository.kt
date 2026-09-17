package com.example.data.repository

import com.example.data.db.ExpenseDao
import com.example.data.db.TaskDao
import com.example.data.model.ExpenseEntity
import com.example.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow

class ShandarRepository(
  private val taskDao: TaskDao,
  private val expenseDao: ExpenseDao,
) {
  val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
  val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
  val totalExpenses: Flow<Double?> = expenseDao.getTotalExpense()

  suspend fun addTask(task: TaskEntity): Long = taskDao.insertTask(task)

  suspend fun toggleTaskCompletion(task: TaskEntity) {
    val newCompleted = !task.isCompleted
    val newStreak = if (newCompleted) task.streakCount + 1 else maxOf(0, task.streakCount - 1)
    taskDao.updateCompletionStatus(task.id, newCompleted, newStreak)
  }

  suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

  suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()

  suspend fun addExpense(expense: ExpenseEntity): Long = expenseDao.insertExpense(expense)

  suspend fun deleteExpense(expense: ExpenseEntity) = expenseDao.deleteExpense(expense)

  suspend fun clearAllExpenses() = expenseDao.clearAllExpenses()
}
