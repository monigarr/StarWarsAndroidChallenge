package com.test.starwarsandroidchallenge.features.characterdetails.view

interface CharacterDetailsView {
    fun updateFilmDataView(filmData: String)
    fun updateSpeciesDataView(name: String, language: String)
    fun updatePlanetDataView(name: String, population: String)
}