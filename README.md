# **BookTracker App**  
>A mobile application for Android that allows users to set reading goals and to keep track of their progress and the books they read  
>Created as a semester-long final project for a mobile application development class  
>Images and GIFS of use can be found at the bottom of this page

## **Main Features**

**Reading Goal & Estimator**
- Enter a yearly book goal and recieve estimates for how many pages/day, pages/week, and books/month will be needed to meet that goal (Estimates based on an average of 350 pages per book)
- Goals will update / reset as follows:
  - Daily - Every midnight
  - Weekly - Every Sunday at midnight
  - Monthly - Midnight on the last day of the month
  - Yearly - Midnight on the last day of the year
- Goals can be reset in the settings page by double tapping "Reset Reading Goal"

**Book Tracker**
- Add new books to the tracker by simply entering a name, author, and the number of pages
- See everything you're reading and how many pages you've read for each of them, all in one place
- Tap a book in the list to update your pages read or delete the book from the list
- Switch between your daily, weekly, monthly, and yearly goal at the top to quickly check progress
  
**Goal Stats**
- A detailed overview of all your goals
- Includes a percentage-based progress bar for visualization
- See your daily, weekly, monthly, yearly, and lifetime progress for pages read and books finished
- See how many times you've completed each type of goal while the app was installed
  
**Reading History**
- See logs that are automatically made each time you update a book's page count
- Shows the book name, author name, number of pages read, and the date you read them

**Bookshelf**
- See your entire collection of books, in-progress and completed, all in one place
- Shows the book name and author along with the date you started reading it, and if applicable, the date you finished reading it
- Books that have been completed are shown with a gold icon

## **Installation**
- It should be possible to simply download booktrackerapp.apk and load it into or install it onto your Android emulator or device.
  - Should work for Android 7 - 13 (minSdk: 24, targetSdk: 33)
  - Emulator: Tested and functional on emulated Pixel 2 API 28 using Android Studio Electric Eel 2022.1.1 Patch 1
  - Device: Tested and functional on a Motorola Moto E

## **Technical Information**
- Data management done with SQLite and the Room library
- MVVM (Model-View-ViewModel) architecture used for logic and UI handling
- Fragments used for UI
- Developed using Kotlin programming language on Android Studio Electric Eel 2022.1.1 Patch 1
- My code primarily found in app/src/main/java/com/ctannlund/booklog

## **Known Bugs**
- Goal UI doesn't always update immediately - may require a refresh to see progress, like switching tabs or switching to another goal and back (purely visual bug)

## **Images & Media**
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/0c648d8b-c61f-4742-88c6-62822f9f83b0 width="360" height="640" alt="Initial goal setting upon first time opening the app"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/5cff8847-54ca-412d-a6b3-f4f1a1037134 width="360" height="640" alt="Goal estimate calculations"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/4c055aaf-2f0f-4d32-b181-514f2f211949 width="360" height="640" alt="Adding a book to the list"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/4303f384-5f34-4e91-a435-028f11ecdf4d width="360" height="640" alt="Updating a book's page count"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/5820e3f3-0fc0-400d-a65f-0a189a07c42b width="360" height="640" alt="Deleting a book from the list"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/aa3e92cd-f822-47e4-a6b8-5a4a859770be width="360" height="640" alt="Image of the main book page"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/d3941d37-6b20-46ba-bd45-363a83db17d1 width="360" height="640" alt="Showing off the goal stats screen"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/a30e55fe-cca1-4c92-a780-c4edeb17e1c5 width="360" height="640" alt="Image of the goal stats screen"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/b0954d93-60fb-4699-8c61-d3180e3ac67f width="360" height="640" alt="Reading logs from book page count updates"/>
<img src=https://github.com/ctannlund/BookTracker-App/assets/70289151/b221a5ee-f8ba-4869-b663-3ee02369a616 width="360" height="640" alt="The bookshelf"/>
