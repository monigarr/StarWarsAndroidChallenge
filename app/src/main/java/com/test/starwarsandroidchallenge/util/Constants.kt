package com.test.starwarsandroidchallenge.util

class Constants {
    private constructor()

    companion object {
        const val PEOPLE_LIST_INITIAL_URL: String = "https://swapi.co/api/people"
        const val SHARED_PREFERENCE_NAME: String = "star_war_shared_preference"
        const val NEXT_URL_PEOPLE_LIST: String = "next_url_people_list"
        const val EMPTY_VIEW_TYPE = 0
        const val NORMAL_VIEW_TYPE = 1
        const val KEY_PEOPLE_DATA = "people_data"
    }
}