package com.example.testtest.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.recyclerview.widget.RecyclerView
import com.example.testtest.R
import com.example.testtest.di.setupTestKoin
import com.example.testtest.repository.NotesRepository
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import android.view.View

/**
 * Базовый тест для MainActivity
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest : KoinTest, TestCase() {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private val repository: NotesRepository by inject()
    private var decorView: View? = null
    private val mainScreen = MainScreen()
    private val editNoteScreen = EditNoteScreen()

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupKoin() {
            setupTestKoin(ApplicationProvider.getApplicationContext())
        }
    }

    @Before
    fun setup() {
        // Очищаем все заметки перед тестом
        runBlocking {
            clearAllNotes()
        }
        
        // Запуск MainActivity
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        
        // Получаем decorView для проверок
        activityScenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
    }

    @After
    fun tearDown() {
        // Очищаем все заметки после теста
        runBlocking {
            clearAllNotes()
        }
        
        activityScenario.close()
    }
    
    /**
     * Очищает все заметки из репозитория
     */
    private suspend fun clearAllNotes() {
        val notes = repository.getAllNotes().first()
        notes.forEach { note ->
            repository.deleteNote(note)
        }
    }

    /**
     * Проверяем отображение элементов интерфейса
     */
    @Test
    fun testUIElements() = run {
        step("Проверяем отображение элементов интерфейса") {
            mainScreen {
                bottomNavigation.isEnabled()
            }
        }
    }

    /**
     * Тест проверяет переключение между вкладками нижней навигации
     * - Переключается на вкладку "Заметки"
     * - Переключается на вкладку "Поиск"
     * - Переключается на вкладку "Настройки"
     */
    @Test
    fun clickTabOne() {
        clickTab(TabType.Заметки)
        Thread.sleep(2000)
        clickTab(TabType.Поиск)
        Thread.sleep(2000)
        clickTab(TabType.Настройки)
        Thread.sleep(2000)
    }

    /**
     * Вспомогательный метод для переключения на вкладку
     * @param tabType тип вкладки, на которую нужно переключиться
     */
    private fun clickTab(tabType: TabType) {
        mainScreen.selectTab(MainScreen.getTabIdByTab(tabType))
    }

    /**
     * Проверяем наличие RecyclerView
     */
    @Test
    fun testRecyclerViewExists() = run {
        step("Проверяем вкладку с заметками") {
            mainScreen {
                selectTab(R.id.navigation_notes)
                notesRecyclerView.isDisplayed()
            }
        }
    }
    
    /**
     * Создаем 3 заметки и проверяем их отображение
     */
    @Test
    fun testNotesCreationAndVisibility() = run {
        step("Переключаемся на вкладку с заметками") {
            mainScreen {
                selectTab(R.id.navigation_notes)
                notesRecyclerView.isDisplayed()
            }
        }
        
        step("Создаем 3 тестовые заметки") {
            // Создаем заметки через UI
            for (i in 1..3) {
                mainScreen {
                    openCreateNoteScreen()
                }
                
                editNoteScreen {
                    createNote("Заметка $i", "Содержание заметки $i")
                }
                
                println("Создана заметка $i")
                // Небольшая задержка для стабильности теста
                Thread.sleep(500)
            }
            
            // Проверяем количество заметок в репозитории
            runBlocking {
                val notes = repository.getAllNotes().first()
                println("Количество заметок в репозитории: ${notes.size}")
                assert(notes.size == 3) { "Ожидалось 3 заметки, но найдено ${notes.size}" }
                
                // Выводим заметки для отладки
                notes.forEachIndexed { index, note ->
                    println("Заметка #${index + 1}: ${note.title} - ${note.content}")
                }
            }
        }
        
        step("Проверяем отображение заметок в UI") {
            mainScreen {
                notesRecyclerView.isDisplayed()
                
                // Проверяем первую заметку (новые заметки отображаются первыми, поэтому последняя созданная будет первой)
                onView(withId(R.id.notesRecyclerView))
                    .check(matches(isDisplayed()))
                
                // Проверяем первую заметку (последнюю созданную)
                mainScreen.notesRecyclerView.childAt<MainScreen.NoteItem>(0) {
                    noteTitle {
                        isDisplayed()
                        hasText("Заметка 3")
                    }
                    noteContent {
                        isDisplayed()
                        hasText("Содержание заметки 3")
                    }
                }
                
                // Прокручиваем к третьей заметке (первой созданной)
                onView(withId(R.id.notesRecyclerView))
                    .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
                
                // Проверяем третью заметку (первую созданную)
                mainScreen.notesRecyclerView.childAt<MainScreen.NoteItem>(2) {
                    noteTitle {
                        isDisplayed()
                        hasText("Заметка 1")
                    }
                    noteContent {
                        isDisplayed()
                        hasText("Содержание заметки 1")
                    }
                }
            }
        }
    }
}