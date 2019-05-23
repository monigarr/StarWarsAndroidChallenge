package com.test.starwarsandroidchallenge.model

data class People(
    var name: String? = null,
    var height: String? = null,
    var birth_year: String? = null,
    var species: ArrayList<String>? = null,
    var films: ArrayList<String>? = null
) {
    override fun toString(): String {
        return "PeopleInformation(name=$name, height=$height, birth_year=$birth_year, species=$species, films=$films)"
    }
}