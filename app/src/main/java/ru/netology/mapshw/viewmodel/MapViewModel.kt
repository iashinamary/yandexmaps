package ru.netology.mapshw.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.mapshw.db.PlaceDatabase
import ru.netology.mapshw.dto.Place
import ru.netology.mapshw.entity.PlaceEntity

class MapViewModel(context: Application): ViewModel() {

    private val dao = PlaceDatabase.getInstance(context).placeDao()

    val places = dao.getAll().map{
        it.map(PlaceEntity::toDto)
    }

    fun insertPlace(place: Place){
        viewModelScope.launch{
            dao.insert(PlaceEntity.fromDto(place))
        }
    }

    fun deletePlaceById(id: Long){
        viewModelScope.launch{
            dao.deleteById(id)
        }
    }
}