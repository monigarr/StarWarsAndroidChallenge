package com.test.starwarsandroidchallenge.features.homescreen.presenter;

import com.test.starwarsandroidchallenge.dataholder.PeopleData
import com.test.starwarsandroidchallenge.features.homescreen.view.HomeScreenView

interface HomeScreenPresenter {
    fun sendPeopleListRequest(url: String)
    fun attachView(homeScreenView: HomeScreenView)
    fun detachView()
    fun searchCharacterByNameOrBirthday(query: String, peopleDataList: ArrayList<PeopleData>)
}
