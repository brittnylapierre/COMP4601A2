Observations:
240 users who have left reviews to various movies, the rest have just viewed the movie pages.
Double checked with a little testing in the reviews folder.
Correct in both the profiling and the testing.

User Profile Datapoints: [genre-1 count, genre-1 aggregate score, genre-1 avg score, genre-1 page views, ...., genre-n count, genre-n aggregate score, genre-n avg score, genre-n page views]

See csv file for user profiles. (Also gets saved in the user objects in the user store)


Remember to change the path to resources in the reader.

To see profile results:

nav to: http://localhost:8080/COMP4601A2/rs/reset/testing this will read in the movies, users, and sentiments
then: http://localhost:8080/COMP4601A2/rs/context will profile the users

To see communities use:
http://localhost:8080/COMP4601A2/rs/community

And to view a page as a user use:
http://localhost:8080/COMP4601A2/rs/fetch/{user}/{page}
ex: http://localhost:8080/COMP4601A2/rs/fetch/A15G70V9OBTOVO/078062565X

