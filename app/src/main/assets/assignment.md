Assignment
==========

Create an Android application named MovieApp, which supports Android API level 19 and above. 
This application will show information about movies shown in cinemas. 
The movie data that is being displayed is fetched from the API provided by 
https://www.themoviedb.org. 
The API documentation could be found on this URL 
https://developers.themoviedb.org/3 
and you must register to get the API Key for your app.

This application will have two screens :

List of Movies

This screen displays the list of movies in form of list or grid. 
There are 3 categories of movies list that user could select 
(using tabs/navigation drawer) : Now Playing, Upcoming, and Popular.
Each movie item displayed in the list/grid must at least contain the movie poster, 
movie title, and rating score (based on votes). 
The application must load the next page of data when user scrolls to the end of the list. 
And if user clicks an item in the list, it will display the Movie Detail screen for 
the selected movie.

Optional/Bonus Feature : Movie search based on movie title

Movie Detail

This screen displays the detail information of a movie. 
The information that must be available are :

- Movie title (DONE)
- Movie poster (DONE)
- Running time (DONE)
- Release date (DONE)
- Original Languages (DONE)
- Genres (DONE)
- Rating score and number of votes (DONE)
- List of actor/actress (limit to 5 people) including the role name (DONE)
- Synopsis (DONE - Overview)
- Videos/Trailers (user should be able to play the video) - if exists (WIP)
- Aside from the app, please also add unit and UI tests for at least one screen 
(details screen or main screen) in the project to test the feature.

Notes :

The project must be created using Android Studio

You may use any third party libraries for UI, connection, parser, storage, etc

Feel free to create the app design of your own, with your selected layouts, 
themes, icons, etc but keep in mind that the data/components mentioned above must be 
available in your app

You may add extra functionalities/information on the screen that you think are good to be have.

Please upload the full project source code to github and send the link