package com.example.testtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtest.model.Note
import com.example.testtest.model.NoteCategory
import com.example.testtest.repository.NotesRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel для управления данными приложения
 * Обрабатывает операции с заметками, настройками и поиском
 */
class MainViewModel(private val repository: NotesRepository) : ViewModel() {
    // LiveData для списка всех заметок
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    // LiveData для результатов поиска
    private val _searchResults = MutableLiveData<List<Note>>()
    val searchResults: LiveData<List<Note>> = _searchResults

    // LiveData для заметок по категориям
    private val _categoryNotes = MutableLiveData<List<Note>>()
    val categoryNotes: LiveData<List<Note>> = _categoryNotes

    // LiveData для настройки темной темы
    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    // LiveData для метода сортировки
    private val _sortingMethod = MutableLiveData<SortingMethod>()
    val sortingMethod: LiveData<SortingMethod> = _sortingMethod

    /**
     * Перечисление методов сортировки заметок
     */
    enum class SortingMethod {
        DATE,    // По дате
        TITLE,   // По заголовку
        CATEGORY // По категории
    }

    /**
     * Инициализация ViewModel
     * Загружает заметки и настройки при создании
     */
    init {
        loadNotes()
        loadSettings()
    }

    /**
     * Загружает все заметки из репозитория и сортирует их
     */
    private fun loadNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collect { notesList ->
                _notes.value = sortNotes(notesList)
            }
        }
    }

    /**
     * Загружает настройки темы и метода сортировки из репозитория
     */
    private fun loadSettings() {
        viewModelScope.launch {
            _isDarkTheme.value = repository.isDarkTheme()
            _sortingMethod.value = repository.getSortingMethod()
        }
    }

    /**
     * Сохраняет заметку в репозитории
     * @param note заметка для сохранения
     */
    fun saveNote(note: Note) {
        viewModelScope.launch {
            repository.saveNote(note).collect { notes ->
                _notes.value = sortNotes(notes)
            }
        }
    }

    /**
     * Удаляет заметку из репозитория
     * @param note заметка для удаления
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note).collect { notes ->
                _notes.value = sortNotes(notes)
            }
        }
    }

    /**
     * Выполняет поиск заметок по запросу
     * @param query строка поиска
     */
    fun searchNotes(query: String) {
        val allNotes = _notes.value ?: emptyList()
        val results = if (query.isBlank()) {
            allNotes
        } else {
            allNotes.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                note.content.contains(query, ignoreCase = true)
            }
        }
        _searchResults.value = results
    }

    /**
     * Фильтрует заметки по категории
     * @param category категория для фильтрации
     */
    fun filterByCategory(category: NoteCategory) {
        val allNotes = _notes.value ?: emptyList()
        _categoryNotes.value = allNotes.filter { it.category == category }
    }

    /**
     * Устанавливает темную тему
     * @param isDark true для включения темной темы
     */
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            repository.setDarkTheme(isDark)
            _isDarkTheme.value = isDark
        }
    }

    /**
     * Устанавливает метод сортировки заметок
     * @param method метод сортировки
     */
    fun setSortingMethod(method: SortingMethod) {
        viewModelScope.launch {
            repository.setSortingMethod(method)
            _sortingMethod.value = method
            _notes.value?.let { notes ->
                _notes.value = sortNotes(notes)
            }
        }
    }

    /**
     * Сортирует список заметок согласно выбранному методу
     * @param notes список заметок для сортировки
     * @return отсортированный список заметок
     */
    private fun sortNotes(notes: List<Note>): List<Note> {
        return when (_sortingMethod.value) {
            SortingMethod.DATE -> notes.sortedByDescending { it.createdAt }
            SortingMethod.TITLE -> notes.sortedBy { it.title }
            SortingMethod.CATEGORY -> notes.sortedBy { it.category }
            else -> notes
        }
    }

    /**
     * Экспортирует заметки в файл
     */
    fun exportNotes() {
        viewModelScope.launch {
            repository.exportNotes()
        }
    }

    /**
     * Импортирует заметки из файла
     */
    fun importNotes() {
        viewModelScope.launch {
            repository.importNotes().collect { notes ->
                _notes.value = sortNotes(notes)
            }
        }
    }
} 