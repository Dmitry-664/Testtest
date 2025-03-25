package com.example.testtest

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Инструментальный тест, который будет выполняться на устройстве Android.
 *
 * См. [документацию по тестированию](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    /**
     * Тест проверяет корректность контекста приложения
     * Проверяет, что имя пакета соответствует ожидаемому
     */
    @Test
    fun useAppContext() {
        // Контекст тестируемого приложения
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.testtest", appContext.packageName)
    }
}