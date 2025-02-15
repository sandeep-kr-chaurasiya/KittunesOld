# Kittunes - Music Streaming App

Kittunes is a feature-rich music streaming application that allows users to search, play, and manage their music collections. It provides seamless functionality for creating playlists, managing song queues, and a user-friendly interface for an enjoyable music experience.

[Install Kittunes](https://github.com/sandeep-kr-chaurasiya/Kittunes/blob/main/app/release/Kittunes.apk)

## Introduction

Kittunes is designed for music lovers, enabling users to easily find their favorite songs, create personalized playlists, and manage playback seamlessly. With features like song queue management and user authentication, Kittunes aims to provide a robust music streaming experience.


## Features

- **Music Playback**: Stream songs with controls for play, pause, skip, and rewind.
- **Search Functionality**: Users can search for songs, artists, and albums.
- **Playlist Management**: Create, modify, and delete playlists.
- **Add to Playlist**: Easily add songs to existing playlists or create new ones.
- **Queue Management**: Add songs to a playback queue for continuous listening.
- **User Authentication**: Secure sign-up and login functionality using Firebase Authentication.
- **Persistent State**: Retains playback state and current song details even when the app is closed and reopened.
- **Dynamic UI Updates**: Reflects the current song and playlist information in real-time.
- **Navigation**: Provides the ability to play the next or previous song in the queue.

## Installation

To get started with Kittunes,Install from here 


Ensure you have the required dependencies in your `build.gradle` file and set up your Firebase project for authentication and Firestore.

## Usage

1. **Sign Up / Login**: Use the app to create a new account or log in with existing credentials.
2. **Search for Songs**: Navigate to the search feature to find your favorite songs.
3. **Play Music**: Click on any song to start playback.
4. **Manage Playlists**: Create or modify playlists from the library section.
5. **Add to Queue**: Use the three-dot menu to add songs to the playback queue.
6. **Access Current Song**: Tap on the current song to view details and control playback.

## App Architecture

Kittunes follows a modular architecture with a clear separation of concerns between UI, business logic, and data layers. The architecture includes Activities, Fragments, ViewModels, and a Firebase backend.

## Key Components

### Activity and Fragment Structure

- **MainActivity**: The primary entry point of the app, which hosts the main user interface and fragments.
- **SearchFragment**: Handles song searches and displays results using a RecyclerView.
- **LibraryFragment**: Displays user playlists and allows for playlist management.
- **SongDetailBottomFragment**: Displays the current song's details and controls playback.

### ViewModel Integration

- **SharedViewModel**: Manages UI-related data in a lifecycle-conscious way. It stores playlists and manages the playback queue. This ViewModel is shared across multiple fragments to ensure data consistency.

### Firebase Integration

- **Authentication**: Users can sign up and log in using Firebase Authentication. This process is handled within the app, allowing for secure user management.
- **Firestore**: User-specific playlists are stored in Firestore. When users add songs to playlists, the app checks for existing playlists and updates them or creates new ones as necessary.

### Media Playback

- **MediaPlayer**: A single instance of `MediaPlayer` is used to handle all music playback. This ensures consistent playback behavior across the app.
- **SeekBar**: Allows users to seek through songs. It updates in real-time based on the current playback position.

## Technologies Used

- **Android Development**: Kotlin, XML
- **Firebase**: Authentication, Firestore for data storage
- **UI Frameworks**: Material Design for a responsive user interface
- **Android Architecture Components**: ViewModel, LiveData, and Fragment

## How It Works

1. **User Authentication**: Users can sign up or log in using Firebase Authentication. Upon successful login, user-specific data is fetched from Firestore.
  
2. **Searching for Songs**: The app utilizes Firestore to fetch songs based on user search queries. The results are displayed in the `SearchFragment` using a `RecyclerView` and `SearchAdapter`.

3. **Playing Music**: When a user clicks on a song, the `SongDetailBottomFragment` displays the song details. The `MediaPlayer` instance starts playback, and the UI updates with the current song details.

4. **Managing Playlists**: Users can create or modify new playlists. The app interacts with Firestore to store playlist data and song additions.

5. **Queue Management**: The `SharedViewModel` maintains a queue of songs for playback. Users can seamlessly navigate to the next or previous song.

6. **Persistent State**: The app uses `SharedPreferences` to store user preferences and the current state of playback. This allows users to resume where they left off when they reopen the app.



## License

Kittunes is licensed under the MIT License. For more details, see the [LICENSE](LICENSE) file.

## Contact

For any questions, suggestions, or feedback, feel free to contact the authors:

- ### Name: Sandeep Kumar
- **Website**: [https://sandeep-kr-chaurasiya.netlify.app](https://sandeep-kr-chaurasiya.netlify.app)
- **Email**: sandeepkr11320@gmail.com

- ### Name: Dinesh Kumar
- **Website**: [https://dineshsingh.me](https://dineshsingh.me)
- **Email**: dineshkumar623092@gmail.com

---
