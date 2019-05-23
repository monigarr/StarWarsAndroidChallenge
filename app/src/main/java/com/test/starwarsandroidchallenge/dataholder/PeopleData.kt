package com.test.starwarsandroidchallenge.dataholder

import android.os.Parcel
import android.os.Parcelable
import com.test.starwarsandroidchallenge.model.People

class PeopleData : Parcelable {
    var name: String? = null
    var height: String? = null
    var birth_year: String? = null
    private var species: ArrayList<String>? = null
    private var films: ArrayList<String>? = null
    var speciesArray : Array<String?>?= null
    var filmsArray : Array<String?>?= null

    constructor(people: People) {
        name = people.name
        height = people.height
        birth_year = people.birth_year
        species = people.species
        films = people.films
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(height)
        dest?.writeString(birth_year)
        speciesArray = species?.size?.let { arrayOfNulls(it) }
        species?.toArray(speciesArray)
        dest?.writeArray(speciesArray)
        filmsArray = films?.size?.let { arrayOfNulls(it) }
        films?.toArray(filmsArray)
        dest?.writeArray(filmsArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        height = parcel.readString()
        birth_year = parcel.readString()
        species = parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>?
        films = parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>?
        speciesArray = species?.size?.let { arrayOfNulls(it) }
        species?.toArray(speciesArray)
        filmsArray = films?.size?.let { arrayOfNulls(it) }
        films?.toArray(filmsArray)
    }

    companion object CREATOR : Parcelable.Creator<PeopleData> {
        override fun createFromParcel(parcel: Parcel): PeopleData {
            return PeopleData(parcel)
        }

        override fun newArray(size: Int): Array<PeopleData?> {
            return arrayOfNulls(size)
        }
    }
}