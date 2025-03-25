package com.example.testtest.repository

import android.content.SharedPreferences
import com.example.testtest.model.Note
import com.example.testtest.viewmodel.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Репозиторий для работы с заметками
 * Обеспечивает сохранение, загрузку и управление заметками в SharedPreferences
 */
class NotesRepository(private val sharedPreferences: SharedPreferences) {
    private val gson = Gson()
    private val notesKey = "notes"
    private val themeKey = "dark_theme"
    private val sortingKey = "sorting_method"

    /**
     * Возвращает все заметки из SharedPreferences
     * @return Flow со списком заметок
     */
    fun getAllNotes(): Flow<List<Note>> = flow {
        val notesJson = sharedPreferences.getString(notesKey, null)
        val notes = if (notesJson != null) {
            val type = object : TypeToken<List<Note>>() {}.type
            gson.fromJson<List<Note>>(notesJson, type)
        } else {
            emptyList()
        }
        emit(notes)
    }.flowOn(Dispatchers.IO)

    /**
     * Сохраняет заметку в SharedPreferences
     * @param note заметка для сохранения
     * @return Flow с обновленным списком заметок
     */
    suspend fun saveNote(note: Note): Flow<List<Note>> = flow {
        val currentNotes = getCurrentNotesList().toMutableList()
        val index = currentNotes.indexOfFirst { it.id == note.id }
        
        if (index != -1) {
            currentNotes[index] = note
        } else {
            currentNotes.add(0, note)
        }
        
        saveNotesList(currentNotes)
        emit(currentNotes)
    }.flowOn(Dispatchers.IO)

    /**
     * Удаляет заметку из SharedPreferences
     * @param note заметка для удаления
     * @return Flow с обновленным списком заметок
     */
    suspend fun deleteNote(note: Note): Flow<List<Note>> = flow {
        val currentNotes = getCurrentNotesList().toMutableList()
        currentNotes.remove(note)
        saveNotesList(currentNotes)
        emit(currentNotes)
    }.flowOn(Dispatchers.IO)

    /**
     * Получает текущий список заметок из SharedPreferences
     * @return список заметок
     */
    private fun getCurrentNotesList(): List<Note> {
        val notesJson = sharedPreferences.getString(notesKey, null)
        return if (notesJson != null) {
            val type = object : TypeToken<List<Note>>() {}.type
            gson.fromJson(notesJson, type)
        } else {
            emptyList()
        }
    }

    /**
     * Сохраняет список заметок в SharedPreferences
     * @param notes список заметок для сохранения
     */
    private fun saveNotesList(notes: List<Note>) {
        val notesJson = gson.toJson(notes)
        sharedPreferences.edit()
            .putString(notesKey, notesJson)
            .apply()
    }

    /**
     * Проверяет, включена ли темная тема
     * @return true, если темная тема включена
     */
    suspend fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(themeKey, false)
    }

    /**
     * Устанавливает темную тему
     * @param isDark true для включения темной темы
     */
    suspend fun setDarkTheme(isDark: Boolean) {
        sharedPreferences.edit()
            .putBoolean(themeKey, isDark)
            .apply()
    }

    /**
     * Получает текущий метод сортировки заметок
     * @return метод сортировки
     */
    suspend fun getSortingMethod(): MainViewModel.SortingMethod {
        val methodName = sharedPreferences.getString(sortingKey, MainViewModel.SortingMethod.DATE.name)
        return MainViewModel.SortingMethod.valueOf(methodName ?: MainViewModel.SortingMethod.DATE.name)
    }

    /**
     * Устанавливает метод сортировки заметок
     * @param method метод сортировки
     */
    suspend fun setSortingMethod(method: MainViewModel.SortingMethod) {
        sharedPreferences.edit()
            .putString(sortingKey, method.name)
            .apply()
    }

    /**
     * Экспортирует заметки в JSON файл
     * @return Flow с результатом операции (true - успешно)
     */
    suspend fun exportNotes(): Flow<Boolean> = flow {
        try {
            val notesJson = gson.toJson(getCurrentNotesList())
            val file = File(getBackupDir(), "notes_backup.json")
            FileWriter(file).use { writer ->
                writer.write(notesJson)
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Импортирует заметки из JSON файла
     * @return Flow со списком импортированных заметок
     */
    suspend fun importNotes(): Flow<List<Note>> = flow {
        try {
            val file = File(getBackupDir(), "notes_backup.json")
            val notesJson = FileReader(file).readText()
            val type = object : TypeToken<List<Note>>() {}.type
            val importedNotes = gson.fromJson<List<Note>>(notesJson, type)
            saveNotesList(importedNotes)
            emit(importedNotes)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Получает директорию для резервного копирования заметок
     * @return директория для резервных копий
     */
    private fun getBackupDir(): File {
        return File("/storage/emulated/0/Download")
    }
} 