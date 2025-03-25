package com.example.testtest.model

import android.graphics.Color
import com.example.testtest.R
import java.util.Date

// Модель данных для заметки
data class Note(
    val id: Long = 0, // Уникальный идентификатор заметки
    val title: String, // Заголовок заметки
    val content: String, // Содержание заметки
    val category: NoteCategory = NoteCategory.DEFAULT, // Категория заметки
    val createdAt: Date = Date() // Дата создания заметки
)

// Перечисление категорий заметок с соответствующими цветами
enum class NoteCategory(val colorResId: Int) {
    DEFAULT(R.color.note_gray), // Категория по умолчанию
    PERSONAL(R.color.note_blue), // Личные заметки
    WORK(R.color.note_green), // Рабочие заметки
    SHOPPING(R.color.note_yellow), // Список покупок
    IMPORTANT(R.color.note_red), // Важные заметки
    IDEAS(R.color.note_purple), // Идеи
    TODOS(R.color.note_orange) // Задачи
} 