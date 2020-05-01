# kindnessapp
The Kindness Android App
Kindness Charity Android Application

This app is developed to let people who are having problems to be helped by people who can offer helping.

Test Account:
phone number : +201113300680   (Egypt Country)
verification code : 123456
(this account is for testing the app, you can create your own but the app is still under development.)

![](images/imageone.jpeg)
![](images/imagetwo.jpeg)
![](images/imagethree.jpeg)
![](images/imagefour.jpeg)
![](images/imagefive.jpeg)

Instructions and Information:
1. Run the app through MainActivity.
2. You can walk through the onBoarding screen or skip.
3. Put your phone Number into the EditText and select your country's code (It's selected automatically but can be
used manually) note: the country code picker is an open source API not made by the team use testing account given above.
4. The app uses firebase authentication using phone-number so it sends verification code to the user and the app
validates the credentials
5. User must sign up on first time to access the app and its features also in sign up process an account for the
user with registeration info is placed into firebase real time database as an object called User.
6. In registeration process user has to verify his identity by uploading national Id or any identity card.
7. The identity card photo is uploaded to firebase storage.
8. After finishing sign-in or sign-up process categories (fields of help) are downloaded through firebase realtime
 database into a listView using custom list adapter, categories can be updated or modified by adding or removing any
of the categories online from the database and the app updates itself automatically to recieve changes from the data-
base.
9. Each category has category id so when clicking on "post" for posting user's problem or on "view" to view user's 
problems, this id is sent through an intent to the next activity so the problems associated with this category id
are displayed or added (also through realtime firebase database)
10. The app deals with two types of users, one who requests help and the other who offers help in all aspects of life.
11. To post a new problem click on the post button of the field you want to post in.
12. To delete a problem posted by you select "my problems" from navigation bar.
13. The problems are showed as "posts" each is associated with phone-number of the user posted them.
14. You can share a problem through any app or you can contact the user posted the problem by two buttons "share" and "contact"
15. There's a red light for urgent problems to notify that this problem can't wait and is urgent.
16. Problems are displayed in a recycler-view and images are displayed using open source Picasso API
 
Contact:
mostafaibrahimragab@gmail.com
||  salmahamdisaad@gmail.com
