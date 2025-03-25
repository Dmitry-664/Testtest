package com.example.testtest.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtest.ui.MainActivity
import com.example.testtest.databinding.FragmentSearchBinding
import com.example.testtest.model.Note
import com.example.testtest.ui.adapter.NotesAdapter
import com.example.testtest.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Фрагмент для поиска заметок
 * Позволяет искать заметки по заголовку и содержимому
 */
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var notesAdapter: NotesAdapter

    /**
     * Создает и возвращает представление фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Инициализирует фрагмент после создания представления
     * Настраивает RecyclerView и поле поиска
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
    }

    /**
     * Настраивает RecyclerView для отображения результатов поиска
     * Устанавливает адаптер и обработчики кликов
     */
    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onNoteClick = { note -> (activity as MainActivity).launchEditNote(note) },
            onNoteLongClick = { note -> (activity as MainActivity).showDeleteDialog(note) }
        )
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter
        }
    }

    /**
     * Настраивает поле поиска и наблюдает за изменениями текста
     * При изменении текста обновляет результаты поиска
     */
    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener { text ->
            viewModel.searchNotes(text?.toString() ?: "")
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { notes ->
            notesAdapter.updateNotes(notes)
        }
    }

    /**
     * Очищает привязку при уничтожении представления
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 