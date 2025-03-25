package com.example.testtest.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.testtest.databinding.FragmentSettingsBinding
import com.example.testtest.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Фрагмент настроек приложения
 * Позволяет управлять темой, сортировкой и резервным копированием заметок
 */
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModel()

    /**
     * Создает и возвращает представление фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Инициализирует фрагмент после создания представления
     * Настраивает переключатели и кнопки
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupThemeSwitch()
        setupSortingOptions()
        setupExportImport()
    }

    /**
     * Настраивает переключатель темы
     * При изменении состояния переключателя обновляет тему приложения
     */
    private fun setupThemeSwitch() {
        viewModel.isDarkTheme.observe(viewLifecycleOwner) { isDark ->
            binding.themeSwitch.isChecked = isDark
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkTheme(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    /**
     * Настраивает опции сортировки заметок
     * При выборе метода сортировки обновляет настройки
     */
    private fun setupSortingOptions() {
        viewModel.sortingMethod.observe(viewLifecycleOwner) { method ->
            binding.sortingRadioGroup.check(
                when (method) {
                    MainViewModel.SortingMethod.DATE -> binding.sortByDateRadio.id
                    MainViewModel.SortingMethod.TITLE -> binding.sortByTitleRadio.id
                    MainViewModel.SortingMethod.CATEGORY -> binding.sortByCategoryRadio.id
                    else -> binding.sortByDateRadio.id
                }
            )
        }

        binding.sortingRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val method = when (checkedId) {
                binding.sortByDateRadio.id -> MainViewModel.SortingMethod.DATE
                binding.sortByTitleRadio.id -> MainViewModel.SortingMethod.TITLE
                binding.sortByCategoryRadio.id -> MainViewModel.SortingMethod.CATEGORY
                else -> MainViewModel.SortingMethod.DATE
            }
            viewModel.setSortingMethod(method)
        }
    }

    /**
     * Настраивает кнопки экспорта и импорта заметок
     */
    private fun setupExportImport() {
        binding.exportButton.setOnClickListener {
            viewModel.exportNotes()
        }

        binding.importButton.setOnClickListener {
            viewModel.importNotes()
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