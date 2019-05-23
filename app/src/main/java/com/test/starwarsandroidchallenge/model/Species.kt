package com.test.starwarsandroidchallenge.model

data class Species(
    var name: String? = null,
    var language: String? = null,
    var homeworld: String? = null
) {
    override fun toString(): String {
        return "Species(name=$name, language=$language, homeworld=$homeworld)"
    }
}