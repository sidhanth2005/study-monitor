package com.guru.app.domain.model

enum class TaskPriority { LOW, MEDIUM, HIGH, URGENT }
enum class TaskCategory { STUDY, WORK, HEALTH, PERSONAL, GENERAL }

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: TaskCategory = TaskCategory.GENERAL,
    val dueDate: Long? = null,
    val estPomodoros: Int = 1,
    val completedPomodoros: Int = 0,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
