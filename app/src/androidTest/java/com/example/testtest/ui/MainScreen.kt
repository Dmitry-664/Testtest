package com.example.testtest.ui

import com.kaspersky.kaspresso.screens.KScreen
import com.example.testtest.R
import io.github.kakaocup.kakao.bottomnav.KBottomNavigationView

/**
 * Объект MainScreen описывает элементы интерфейса главного экрана приложения
 * для тестирования с использованием Kaspresso
 */
object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    // Элемент нижней навигации
    val bottomNavigation = KBottomNavigationView { withId(R.id.bottom_navigation) }

    /**
     * Выбирает вкладку в нижней навигации по её идентификатору
     * @param id идентификатор вкладки (R.id.navigation_*)
     */
    fun selectTab(id: Int) {
        bottomNavigation {
            setSelectedItem(id)
        }
    }

    /**
     * Возвращает идентификатор вкладки по её типу
     * @param tabType тип вкладки (Заметки, Поиск, Настройки)
     * @return идентификатор вкладки (R.id.navigation_*)
     */
    fun getTabIdByTab(tabType: TabType): Int {
        return when (tabType) {
            TabType.Заметки -> R.id.navigation_notes
            TabType.Поиск -> R.id.navigation_search
            TabType.Настройки -> R.id.navigation_settings
        }
    }
}

/**
 * Перечисление типов вкладок в нижней навигации
 */
enum class TabType {
    Заметки,
    Поиск,
    Настройки
}