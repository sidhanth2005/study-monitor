package com.guru.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guru.app.domain.model.Task
import com.guru.app.domain.model.TaskCategory
import com.guru.app.domain.model.TaskPriority

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val priority: String,
    val category: String,
    val dueDate: Long?,
    val estPomodoros: Int,
    val completedPomodoros: Int,
    val isCompleted: Boolean,
    val createdAt: Long
) {
    fun toDomain(): Task = Task(
        id = id,
        title = title,
        description = description,
        priority = try { TaskPriority.valueOf(priority) } catch (e: Exception) { TaskPriority.MEDIUM },
        category = try { TaskCategory.valueOf(category) } catch (e: Exception) { TaskCategory.GENERAL },
        dueDate = dueDate,
        estPomodoros = estPomodoros,
        completedPomodoros = completedPomodoros,
        isCompleted = isCompleted,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(task: Task): TaskEntity = TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            priority = task.priority.name,
            category = task.category.name,
            dueDate = task.dueDate,
            estPomodoros = task.estPomodoros,
            completedPomodoros = task.completedPomodoros,
            isCompleted = task.isCompleted,
            createdAt = task.createdAt
        )
    }
}
