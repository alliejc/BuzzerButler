# Competition Practice Player


### Overview
Competition Practice Player is an Android application intended for West Coast Swing dancers who are practicing and preparing for competitions.

In West Coast Swing competitions, you receive your partner and song draw when you step onto the competition floor.  In competition, each song is played for either 1min 30sec or 2mins, so practicing to the remaining time in the song is generally undesirable.

For this reason, Competition Practice Player includes these features:

  - Populates with the user's Spotify playlists
  - Gives an option to end the song at 1:30 or 2:00
  - Gives a notification beep 5 seconds before the song ends
  - Gives a pause before playing the next song in the list

### Version
2.2.0

### Tech

Competition Practice Player uses a number of open source projects to work properly:

* [Retrofit 2.0] - A type-safe HTTP client for Android and Java
* [OKHttp] - Request/response API
* [RoboGuice] - Dependency injection


And of course Competition Practice Player itself is open source with a [public repository](https://github.com/alliejc/compeitionpracticeplayer)
 on GitHub.

### Installation

```sh
$ git clone [git-repo-url] Competition Practice Player
```
Open in Android studio

### Development

Want to contribute? Great!

Add your spotify api key to the gradle.properties file. You need the following key:

{ProjectDirectory}/gradle.properties
CLIENT_ID="{your key}"

### Todos

 - Write Tests
 - Add an Artist list and Album List
 - Add a search bar
 - Add customizeable time and beep options
 - Add alphabetization/organization option
  
### Known Issues

  - Only available for paid Spotify accounts (Spotify limitation)
  - The Spotify OAuth token only lasts 1 hour, without a refresh token option for the Android SDK, meaning the user must login every hour (Spotify Limitation)
  - Also see the "Issues" tab in GitHub
  
### Privacy Policy

  - Any information provided by a user, collected about a user, and collected about a user’s use of the app or device is not stored or kept for any purpose, and is only aquired to provide a personalized experience within the app (ie. displaying your name and e-mail address)


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [OkHttp]: http://square.github.io/okhttp/
   [Retrofit 2.0]: http://square.github.io/retrofit/
   [RoboGuice]: https://github.com/roboguice/roboguice