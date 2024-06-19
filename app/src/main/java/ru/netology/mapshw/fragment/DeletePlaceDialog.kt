package ru.netology.mapshw.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.netology.mapshw.viewmodel.MapViewModel

class DeletePlaceDialog : DialogFragment() {
    companion object {
        private const val ID_KEY = "ID_KEY"
        fun newInstance(id: Long): DeletePlaceDialog {
            val args = Bundle()
            args.putLong(ID_KEY, id)
            val fragment = DeletePlaceDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewModel by viewModels<MapViewModel>()
        val view = AppCompatEditText(requireContext())
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Удалить место?")
            .setPositiveButton("Да") { _, _ ->
                viewModel.deletePlaceById(id = requireArguments().getLong(ID_KEY))
            }
            .setNegativeButton("Отмена") { dialog, id ->
                dialog.cancel()
            }
            .create()

    }
}