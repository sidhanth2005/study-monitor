package com.guru.app.ui.screens.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guru.app.domain.model.Task
import com.guru.app.domain.model.TaskCategory
import com.guru.app.domain.model.TaskPriority
import com.guru.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<TaskCategory?>(null)
    val selectedCategory: StateFlow<TaskCategory?> = _selectedCategory.asStateFlow()

    val taskList: StateFlow<List<Task>> = combine(
        taskRepository.getAllTasks(),
        _searchQuery,
        _selectedCategory
    ) { tasks, query, cat ->
        tasks.filter { task ->
            val matchesQuery = task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)
            val matchesCat = cat == null || task.category == cat
            matchesQuery && matchesCat
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectCategoryFilter(category: TaskCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun addTask(title: String, desc: String, priority: TaskPriority, category: TaskCategory, estPomodoros: Int) {
        viewModelScope.launch {
            val task = Task(
                title = title,
                description = desc,
                priority = priority,
                category = category,
                estPomodoros = estPomodoros
            )
            taskRepository.insertTask(task)
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}
