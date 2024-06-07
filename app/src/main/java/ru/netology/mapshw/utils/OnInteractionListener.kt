package ru.netology.mapshw.utils

import ru.netology.mapshw.dto.Place

interface OnInteractionListener {

    fun onClick(place: Place)
    fun onDelete(place: Place)
    fun onEdit(place: Place)
}