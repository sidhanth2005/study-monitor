package com.guru.app.data.repository

import com.guru.app.data.database.dao.TaskDao
import com.guru.app.data.database.entity.TaskEntity
import com.guru.app.domain.model.Task
import com.guru.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getPendingTasks(): Flow<List<Task>> {
        return taskDao.getPendingTasks().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)?.toDomain()
    }

    override suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(TaskEntity.fromDomain(task))
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(TaskEntity.fromDomain(task))
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(TaskEntity.fromDomain(task))
    }

    override suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId)
    }
}
