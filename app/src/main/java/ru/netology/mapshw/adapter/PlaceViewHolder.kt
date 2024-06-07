package ru.netology.mapshw.adapter

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mapshw.R
import ru.netology.mapshw.databinding.PlaceItemBinding
import ru.netology.mapshw.dto.Place
import ru.netology.mapshw.utils.OnInteractionListener

class PlaceViewHolder(
    private val binding: PlaceItemBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Place) {
        binding.apply {
          title.text = place.name

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.place_menu)

                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                onInteractionListener.onDelete(place)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(place)
                                true
                            }
                            else -> false
                        }
                    }

                    show()
                }
            }
        }


    }
}