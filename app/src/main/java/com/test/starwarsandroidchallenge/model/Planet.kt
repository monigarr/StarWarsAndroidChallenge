package com.test.starwarsandroidchallenge.model

data class Planet(
    var name: String? = null,
    var population: String? = null
) {
    override fun toString(): String {
        return "Planet(name=$name, population=$population)"
    }
}