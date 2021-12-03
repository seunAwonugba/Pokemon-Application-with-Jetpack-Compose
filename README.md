# Pokemon-Application-with-Jetpack-Compose

Pokemon Application Using Jetpack Compose

A pokemon aplication that fetches all the available pokemons from "https://pokeapi.co/" API

It has two screns 

1. The list of all pokemon screens : 

This screen contains image and names of all pokemons fetched, it implements pagination to load and display small chunks of data at a time, to
reduce usage of network bandwidth and system resources.

Each pokemon displayed can also be clicked on to take users to the details screen, by doing this, the pokemon name is passed as an argument to load the details
of the prticular pokemon

2. Clicked pokemon details

Upon clicking from the pokemon list screen, user is directed to the pokemon details page

Technologies used
1. Kotlin
2. Android
3. Jetpack compose
4. Dagger-Hilt
5. Retrofit
