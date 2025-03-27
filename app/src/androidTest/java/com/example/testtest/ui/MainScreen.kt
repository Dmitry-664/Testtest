package com.example.testtest.ui

import com.kaspersky.kaspresso.screens.KScreen
import com.example.testtest.R
import io.github.kakaocup.kakao.bottomnav.KBottomNavigationView
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import android.view.View
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase

/**
 * Главный экран приложения
 */
class MainScreen : KScreen<MainScreen>() {

    override val layoutId: Int? = R.layout.activity_main
    override val viewClass: Class<*>? = null

    val bottomNavigation = KBottomNavigationView { withId(R.id.bottom_navigation) }
    val notesRecyclerView = KRecyclerView(
        builder = { withId(R.id.notesRecyclerView) },
        itemTypeBuilder = {
            itemType(::NoteItem)
        }
    )

    /**
     * Элемент списка заметок
     */
    class NoteItem(parent: Matcher<View>) : KRecyclerItem<NoteItem>(parent) {
        val noteTitle = KTextView(parent) { withId(R.id.noteTitle) }
        val noteContent = KTextView(parent) { withId(R.id.noteContent) }
    }

    val addNoteButton = KButton { withId(R.id.addNoteButton) }

    /**
     * Выбирает вкладку в нижней навигации
     */
    fun selectTab(tabId: Int) {
        bottomNavigation {
            isDisplayed()
            setSelectedItem(tabId)
        }
    }

    /**
     * Открывает экран создания заметки
     */
    fun openCreateNoteScreen() {
        addNoteButton {
            isDisplayed()
            click()
        }
    }

    companion object {
        /**
         * Возвращает ID вкладки по её типу
         */
        fun getTabIdByTab(tabType: TabType): Int {
            return when (tabType) {
                TabType.Заметки -> R.id.navigation_notes
                TabType.Поиск -> R.id.navigation_search
                TabType.Настройки -> R.id.navigation_settings
            }
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