package ru.netology.mapshw.utils

import androidx.recyclerview.widget.DiffUtil
import ru.netology.mapshw.dto.Place

object DiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
        oldItem == newItem
}