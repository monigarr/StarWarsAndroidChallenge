package com.test.starwarsandroidchallenge.features.homescreen.view

import com.test.starwarsandroidchallenge.dataholder.PeopleData

interface HomeScreenView {
    fun updateRecyclerView(peopleDataList: ArrayList<PeopleData>)
    fun updateRecyclerViewForSearch(peopleDataList: ArrayList<PeopleData>)
}