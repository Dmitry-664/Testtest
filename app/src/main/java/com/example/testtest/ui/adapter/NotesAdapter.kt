package com.example.testtest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testtest.databinding.ItemNoteBinding
import com.example.testtest.model.Note
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * Адаптер для отображения списка заметок в RecyclerView
 */
class NotesAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onNoteLongClick: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    /**
     * Создает и возвращает ViewHolder для элемента списка
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    /**
     * Привязывает данные заметки к ViewHolder
     */
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Обновляет список заметок
     * @param notes новый список заметок
     */
    fun updateNotes(notes: List<Note>) {
        println("Обновление списка заметок. Количество: ${notes.size}")
        submitList(notes)
    }

    /**
     * ViewHolder для элемента списка заметок
     */
    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Привязывает данные заметки к элементам интерфейса
         * @param note заметка для отображения
         */
        fun bind(note: Note) {
            binding.apply {
                noteTitle.text = note.title
                noteContent.text = note.content
                val color = ContextCompat.getColor(root.context, note.category.colorResId)
                noteCard.setCardBackgroundColor(color)
            }

            binding.root.setOnClickListener { onNoteClick(note) }
            binding.root.setOnLongClickListener {
                onNoteLongClick(note)
                true
            }
        }
    }

    /**
     * Callback для сравнения заметок при обновлении списка
     */
    private class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
} 