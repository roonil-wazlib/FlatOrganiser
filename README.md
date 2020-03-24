# FlatOrganiser
Project to organise flat related tasks and data, and sync data between flatmates' devices.

## Authentication
Authenticates using Firebase Auth. Users are stored in Realtime Database, and flat data is stored in Cloud Firestore. No real reason to use both, just wanted to experiment.

Currently users can delete their account, which removes all data about them from both Realtime db and Cloud Firestore. If they are the only person in the flat on delete, the flat data is also deleted.

Editting account info is a work in progress.

Yet to implement: Full functionality for editting user info, forgot password option, and email confirmation on sign up.

KNOWN BUGS: 
 - Very occasionally sign in fails and causes a crash, but I have not yet been able to replicate this error while debugging, and therefore haven't been able to determine the issue.
 - App freezes on splashscreen when opened without internet connection - as expected, just haven't implemented a better alternative yet.

## Flats
Users can either create a new flat or join an already established flat on sign in. App functionality can not be accessed until this has happened.

Upon creating a new flat, this flat is added to Cloud Firestore and assigned a unique flat ID. The user then has the option of sharing this ID with flatmates via email, which their flatmates would then have to copy-paste into the edittext when they click join flat. A less frustrating alternative is a work in progress.

After joining or creating a flat, the user can access the full dashboard.

KNOWN BUGS: Creating a new flat glitches after user has just left an old flat.

## Dashboard Activities:
All flat data is stored in Cloud Firestore under the data collection of the flat.

### Shopping List
This has functionality for adding and deleting items, which updates with the database upon each edit. Syncs between devices, but will not update changes on other devices until activity has been reloaded. Activity is implemented using RecyclerView so built in animations on delete are functional. Would like to add more to enable things like swiping to delete/edit, and press and hold to change item order. Crossed out items should also fly to the bottom.

Yet to implement: Animations, order reassignment on item ticked.

KNOWN BUGS: None.

### Bins
An activity to help flatmates keep track of which colour bins go out when. The first flatmate to access the activity enters information about what colour bins they have in their location, how often they go out (weekly, every second week), and what date is the next time they will go out. I have used DatePickerDialog to speed this process up for the user.

From there, any time the activity is accessed by any flatmate, it displays infomation about which bins are going out in the current week, and which are going out the following week, calculated using the frequency and startingd date information provided. The info can be edited at any time via an edit button which returns the user to the setup page, and fields autopopulate from the database for easy editing.

In the future I want push notifications the night before bins go out, but this is a work in progress. Currently the only working notifications must be assigned directly from Firebase, which is useless.

Yet to implement: An easier to read and understand display for which bins go out when, functioning push notifications.

KNOWN BUGS: None

### Dinner
An activity to store infomation about which flatmate is cooking which night, what they are cooking, and what ingredients are required. Uses Recycler View to populate dinner view boxes. User can edit the chef and meal assigned to each week day, and save this data to Cloud Firestore database. On opening the activity, the activity checks what the date is and auto scrolls to that day within the activity. Yet to implement option to add ingredients, although the box is already there.

Long term goal is link with shopping list activity to allow users to add ingredients directly to the shopping list from the dinner activity without changing screens.

Yet to implement: Link ingredients list with shopping list activity.

KNOWN BUGS: Nothing, yet.


## Planned future dashboard activities:

### Chores
Some form of representation for which chores need doing by whom and when. Eg who's putting the bins out this week. Probably formatted similarly to dinner activity.

### Timetable
Activity to show flatmates who is where and at what times - eg at uni, work etc. Long term would like to sync with the UC Timetable, but will need to find time to scrape that data as well as implementing the UI.

### To do list
Self explanatory. Some kind of algorithm that sorts tasks into priority based on due date and importance would be cool
