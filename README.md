# StarWarsAndroidChallenge

StarWars Android Challenge.

## Description
This project aims at getting StarWars characters details using their REST APIs.
Kotlin as the programming language. RxKotlin, Retrofit is used to perform networking operation. MVP design pattern used as project architecture. 

1. In first page, the list is loaded with StarWars character names and birthday. Scrolling down to the bottom of the list triggers further data load if available.
During data loading, search option is disabled and during searching further data loading is prevented if scrolled. Clicking on any item initiates the the details page of corresponding character.

2. In character details page, the required fields are shown.

3. Network error handled. Also other error case such as timeout or any other is handled with proper dialog having information specifically. Also if any data fetching request is made but due to having no network, that request re-initiated upon getting network connection.

4. UI has been kept simple and user friendly.

5. Please use Gradle version lower than 6.0 to build it properly.

## License
[Open Source](https://opensource.org/licenses)
