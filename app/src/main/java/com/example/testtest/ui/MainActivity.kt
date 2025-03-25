package com.example.testtest.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testtest.R
import com.example.testtest.databinding.ActivityMainBinding
import com.example.testtest.model.Note
import com.example.testtest.ui.edit.EditNoteActivity
import com.example.testtest.ui.notes.NotesFragment
import com.example.testtest.ui.search.SearchFragment
import com.example.testtest.ui.settings.SettingsFragment
import com.example.testtest.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Главная активность приложения, управляющая навигацией между фрагментами
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    /**
     * Регистрирует обработчик результата запуска EditNoteActivity
     * При успешном результате создает новую заметку или обновляет существующую
     */
    private val editNoteResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val noteId = data.getLongExtra("note_id", -1)
            val title = data.getStringExtra("note_title") ?: ""
            val content = data.getStringExtra("note_content") ?: ""

            val note = Note(
                id = if (noteId != -1L) noteId else System.currentTimeMillis(),
                title = title,
                content = content
            )
            viewModel.saveNote(note)
        }
    }

    /**
     * Инициализирует активность и настраивает начальный фрагмент
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        
        if (savedInstanceState == null) {
            replaceFragment(NotesFragment())
        }
    }

    /**
     * Настраивает нижнюю навигацию и обработку выбора вкладок
     */
    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notes -> {
                    replaceFragment(NotesFragment())
                    true
                }
                R.id.navigation_search -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.navigation_settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Заменяет текущий фрагмент на указанный
     * @param fragment фрагмент для отображения
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    /**
     * Запускает активность редактирования заметки
     * @param note заметка для редактирования (null для создания новой)
     */
    fun launchEditNote(note: Note? = null) {
        editNoteResult.launch(EditNoteActivity.newIntent(this, note))
    }

    /**
     * Показывает диалог подтверждения удаления заметки
     * @param note заметка для удаления
     */
    fun showDeleteDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Удаление заметки")
            .setMessage("Вы уверены, что хотите удалить эту заметку?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteNote(note)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
} 