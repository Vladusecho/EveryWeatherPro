package com.vladusecho.everyweatherpro.data.mappers

import com.vladusecho.everyweatherpro.data.local.models.CityDbModel
import com.vladusecho.everyweatherpro.domain.entities.City

fun City.toDbModel(): CityDbModel = CityDbModel(id, name, country)

fun CityDbModel.toEntity(): City = City(id, name, country)

fun List<CityDbModel>.toEntities(): List<City> = this.map { it.toEntity() }