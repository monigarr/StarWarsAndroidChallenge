package com.test.starwarsandroidchallenge.model

data class PeopleList(
    var count: Int? = 0,
    var next: String? = null,
    var results: ArrayList<People>? = null
)