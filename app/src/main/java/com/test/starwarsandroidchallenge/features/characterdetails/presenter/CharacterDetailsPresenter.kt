package com.test.starwarsandroidchallenge.features.homescreen.presenter;

import com.test.starwarsandroidchallenge.features.characterdetails.view.CharacterDetailsView

interface CharacterDetailsPresenter {
    fun attachView(characterDetailsView: CharacterDetailsView)
    fun detachView()
    fun sendSpeciesDataRequest(url : String)
    fun sendFilmDataRequest(list: Array<String?>?)
}
