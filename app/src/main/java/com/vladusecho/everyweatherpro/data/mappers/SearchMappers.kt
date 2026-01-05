package com.vladusecho.everyweatherpro.data.mappers

import com.vladusecho.everyweatherpro.data.network.dto.CityDto
import com.vladusecho.everyweatherpro.domain.entities.City

fun CityDto.toEntity(): City = City(id, name, country)

fun List<CityDto>.toEntities(): List<City> = this.map { it.toEntity() }