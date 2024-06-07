package ru.netology.mapshw.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.mapshw.utils.OnInteractionListener
import ru.netology.mapshw.databinding.PlaceItemBinding
import ru.netology.mapshw.dto.Place
import ru.netology.mapshw.utils.DiffCallback

typealias OnClickListener = (place: Place) -> Unit
typealias OnDeleteListener = (place: Place) -> Unit
typealias OnEditListener = (place: Place) -> Unit

class PlaceAdapter(  private val onInteractionListener: OnInteractionListener
) : ListAdapter<Place, PlaceViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = PlaceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding, onInteractionListener)
    }


    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}