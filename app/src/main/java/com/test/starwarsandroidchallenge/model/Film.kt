package com.test.starwarsandroidchallenge.model

data class Film(
    var title: String? = null,
    var opening_crawl: String? = null,
    var release_date: String? = null
) {
    override fun toString(): String {
        return "Film(title=$title, opening_crawl=$opening_crawl, release_date=$release_date)"
    }
}