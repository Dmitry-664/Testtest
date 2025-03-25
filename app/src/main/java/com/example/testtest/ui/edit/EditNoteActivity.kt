package com.example.testtest.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testtest.databinding.ActivityEditNoteBinding
import com.example.testtest.model.Note

/**
 * Активность для создания и редактирования заметок
 */
class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    private var noteId: Long = -1

    companion object {
        /**
         * Создает Intent для запуска активности редактирования заметки
         * @param context контекст приложения
         * @param note заметка для редактирования (null для создания новой)
         * @return Intent для запуска активности
         */
        fun newIntent(context: Context, note: Note? = null): Intent {
            return Intent(context, EditNoteActivity::class.java).apply {
                note?.let {
                    putExtra("note_id", it.id)
                    putExtra("note_title", it.title)
                    putExtra("note_content", it.content)
                }
            }
        }
    }

    /**
     * Инициализирует активность и настраивает интерфейс
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteId = intent.getLongExtra("note_id", -1)
        binding.titleEditText.setText(intent.getStringExtra("note_title"))
        binding.contentEditText.setText(intent.getStringExtra("note_content"))

        setupToolbar()
        setupSaveButton()
    }

    /**
     * Настраивает панель инструментов и кнопку "Назад"
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (noteId == -1L) "Новая заметка" else "Редактирование"
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Настраивает кнопку сохранения заметки
     */
    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isNotBlank()) {
                val intent = Intent().apply {
                    putExtra("note_id", noteId)
                    putExtra("note_title", title)
                    putExtra("note_content", content)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
} 