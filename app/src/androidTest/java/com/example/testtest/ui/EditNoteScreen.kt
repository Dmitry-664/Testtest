package com.example.testtest.ui

import com.kaspersky.kaspresso.screens.KScreen
import com.example.testtest.R
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import androidx.test.espresso.matcher.ViewMatchers.withId

/**
 * Экран редактирования заметки
 */
class EditNoteScreen : KScreen<EditNoteScreen>() {

    override val layoutId: Int = R.layout.activity_edit_note
    override val viewClass: Class<*>? = null

    val titleEditText = KEditText { withId(R.id.titleEditText) }
    val contentEditText = KEditText { withId(R.id.contentEditText) }
    val saveButton = KButton { withId(R.id.saveButton) }

    /**
     * Создает новую заметку с указанным заголовком и содержимым
     */
    fun createNote(title: String, content: String) {
        titleEditText {
            isDisplayed()
            replaceText(title)
        }
        contentEditText {
            isDisplayed()
            replaceText(content)
        }
        saveButton {
            isDisplayed()
            click()
        }
    }
} 