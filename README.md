# Movie Ratings App

A small Android app where you can search for movies/TV series and save your own ratings.

## What it does

- Search movies and TV series from IMDB
- See movie details (plot, actors, ratings from IMDB/Rotten Tomatoes)
- Rate movies yourself (1-10 stars)
- Your ratings are saved locally

## Setup

### 1. Get an API key

The app uses OMDB API to fetch movie data.

1. Go to [omdbapi.com/apikey.aspx](https://www.omdbapi.com/apikey.aspx)
2. Pick the free option and enter your email
3. Check your email and activate the key

### 2. Add the key to the project

Open `local.properties` in the project root and add your key:

```properties
OMDB_API_KEY=your_key_here
```

That's it. The file is already gitignored so your key stays private.

### 3. Run

Open in Android Studio, sync Gradle, and run.

## Tech used

- Kotlin + Jetpack Compose
- Hilt for DI
- Retrofit for networking
- DataStore for local storage

## If something doesn't work

- **"Invalid API key"** → Double check there's no extra spaces in local.properties
- **Build fails** → Try Build → Clean Project → Rebuild
