package com.example.testtest.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtest.ui.MainActivity
import com.example.testtest.databinding.FragmentNotesBinding
import com.example.testtest.model.Note
import com.example.testtest.ui.adapter.NotesAdapter
import com.example.testtest.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Фрагмент для отображения списка всех заметок
 * Позволяет просматривать, добавлять, редактировать и удалять заметки
 */
class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
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
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Инициализирует фрагмент после создания представления
     * Настраивает RecyclerView и кнопку добавления заметки
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        observeNotes()
    }

    /**
     * Настраивает RecyclerView для отображения списка заметок
     * Устанавливает адаптер и обработчики кликов
     */
    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onNoteClick = { note -> (activity as MainActivity).launchEditNote(note) },
            onNoteLongClick = { note -> (activity as MainActivity).showDeleteDialog(note) }
        )
        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = notesAdapter
        }
    }

    /**
     * Настраивает кнопку добавления новой заметки
     */
    private fun setupFab() {
        binding.addNoteButton.setOnClickListener {
            (activity as MainActivity).launchEditNote()
        }
    }

    /**
     * Наблюдает за изменениями в списке заметок
     * Обновляет адаптер при изменении списка
     */
    private fun observeNotes() {
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
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