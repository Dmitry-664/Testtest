package com.example.testtest.di

import android.app.Application
import com.example.testtest.repository.NotesRepository
import com.example.testtest.viewmodel.MainViewModel
import com.example.testtest.viewmodel.MainViewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun setupTestKoin(application: Application) {
    val testModule = module {
        // Используем application для репозитория
        single<NotesRepository> { NotesRepository(application.getSharedPreferences("test_notes", 0)) }
        factory<MainViewModelFactory> { MainViewModelFactory(get()) }
        single<MainViewModel> { MainViewModel(get()) }
    }
    
    // Добавляем тестовый модуль к существующему Koin
    org.koin.core.context.GlobalContext.get().loadModules(listOf(testModule))
} 