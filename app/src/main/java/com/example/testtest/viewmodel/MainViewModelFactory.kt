package com.example.testtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testtest.repository.NotesRepository

/**
 * Фабрика для создания экземпляров MainViewModel
 * Позволяет внедрять зависимости в ViewModel
 */
class MainViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {

    /**
     * Создает экземпляр MainViewModel с указанным репозиторием
     * @param modelClass класс ViewModel для создания
     * @return экземпляр MainViewModel
     * @throws IllegalArgumentException если запрошенный класс не является MainViewModel
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 