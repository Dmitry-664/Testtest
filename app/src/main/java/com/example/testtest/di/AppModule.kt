package com.example.testtest.di

import android.content.Context
import com.example.testtest.repository.NotesRepository
import com.example.testtest.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Модуль Koin для внедрения зависимостей
val appModule = module {
    // Предоставляет SharedPreferences для хранения настроек
    single { androidContext().getSharedPreferences("notes_prefs", Context.MODE_PRIVATE) }
    // Предоставляет экземпляр NotesRepository
    single { NotesRepository(get()) }
    // Предоставляет экземпляр MainViewModel
    viewModel { MainViewModel(get()) }
} 