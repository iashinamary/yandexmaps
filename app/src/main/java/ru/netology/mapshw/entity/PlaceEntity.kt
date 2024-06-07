package ru.netology.mapshw.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.mapshw.dto.Place

@Entity
data class PlaceEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val lat: Double,
    val long: Double,
    ) {
    fun toDto(): Place =
        Place(
            id = id,
            name = name,
            lat = lat,
            long = long,
        )

    companion object {
        fun fromDto(dto: Place): PlaceEntity =
            PlaceEntity(
                id = dto.id,
                name = dto.name,
                lat = dto.lat,
                long = dto.long,
            )
    }
}

fun List<PlaceEntity>.toDto(): List<Place> = map(PlaceEntity::toDto)
fun List<Place>.toEntity(hidden: Boolean = false): List<PlaceEntity> =
    map { PlaceEntity.fromDto(it) }