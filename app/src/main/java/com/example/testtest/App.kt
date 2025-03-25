package com.example.testtest

import android.app.Application
import com.example.testtest.repository.NotesRepository
import com.example.testtest.viewmodel.MainViewModel
import com.example.testtest.viewmodel.MainViewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Основной класс приложения
 * Инициализирует Koin для внедрения зависимостей
 */
class App : Application() {

    /**
     * Инициализация приложения
     * Запускает Koin и настраивает модули
     */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    /**
     * Модуль Koin для внедрения зависимостей
     * Определяет зависимости для репозитория и ViewModel
     */
    private val appModule = module {
        // Создание экземпляра репозитория
        single { NotesRepository(getSharedPreferences("notes", MODE_PRIVATE)) }
        
        // Создание фабрики для MainViewModel
        factory { MainViewModelFactory(get()) }
        
        // Создание экземпляра MainViewModel
        viewModel { MainViewModel(get()) }
    }
} 