package com.example.testtest.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.testtest.ui.MainScreen.getTabIdByTab
import com.example.testtest.ui.MainScreen.selectTab
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.example.testtest.ui.MainActivity
import org.junit.Rule
import org.junit.Test

/**
 * Класс для тестирования главной активности приложения
 */
class MainActivityTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Тест проверяет базовые элементы интерфейса главного экрана
     * - Проверяет, что нижняя навигация активна и доступна для взаимодействия
     */
    @Test
    fun testMainActivityUI() = run {
        step("Проверяем отображение элементов интерфейса") {
            MainScreen {
                bottomNavigation.isEnabled()
            }
        }
    }

    /**
     * Тест проверяет переключение между вкладками нижней навигации
     * - Переключается на вкладку "Заметки"
     * - Ждет 2 секунды
     * - Переключается на вкладку "Поиск"
     * - Ждет 2 секунды
     * - Переключается на вкладку "Настройки"
     * - Ждет 2 секунды
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
        selectTab(getTabIdByTab(tabType))
    }
}